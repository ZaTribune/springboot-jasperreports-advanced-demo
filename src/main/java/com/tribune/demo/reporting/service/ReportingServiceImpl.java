package com.tribune.demo.reporting.service;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.tribune.demo.reporting.config.ResourcesLoader;
import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.entity.ReportLocale;
import com.tribune.demo.reporting.error.BadReportEntryException;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportEntry;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.util.processor.DynamicOutputProcessor;
import com.tribune.demo.reporting.util.translator.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.json.data.JsonDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReportingServiceImpl implements ReportingService {

    private final ResourcesLoader resourcesLoader;

    private final DynamicOutputProcessor outputProcessor;

    private final MessageSource messageSource;
    private final Translator translator;

    @Value("${system.default-language:en}")
    private String defaultLanguage;

    /**
     * <pre>
     * to export to paths:
     * <code>exporter.setExporterOutput(new SimpleOutputStreamExporterOutput ( outputPath + " / " + fileName));</code>
     * we can also utilize {@link net.sf.jasperreports.engine.util.JRSaver}
     * <code>JRSaver.saveObject(jasperReport, " employeeReport.jasper ");</code>
     * </pre>
     **/
    @Override
    public StreamingResponseBody generateFromModel(ReportRequest reportRequest, ReportExportType accept)
            throws JRException, UnsupportedItemException {
        log.info("generateFromModel() - XThread: {}", Thread.currentThread().getName());

        //first check for report existence
        Report report = resourcesLoader.getReport(reportRequest.getReportId());

        //then, check for locale match meaning: (a template that supports the language requested by the user).
        ReportLocale reportLocale = report.getLocales().stream()
                .filter(l -> l.getContent().equalsIgnoreCase(reportRequest.getLocale()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedItemException("Locale", reportRequest.getLocale(), report.getReportLocalesValues()));

        Map<String, Object> parametersMap = processReportRequest(report, reportRequest.getData());

        JasperPrint jasperPrint = getPrint(resourcesLoader.getJasperReport(reportLocale.getId())
                , parametersMap
                , new JREmptyDataSource());

        return outputStream -> {
            try {
                outputProcessor.export(accept, jasperPrint, outputStream);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public Map<String, Object> processReportRequest(Report report, Map<String, Object> inputMap) {

        //filled then injected to the report
        Map<String, Object> parametersMap = new HashMap<>();

        final String reportName = "reportName";//to be within constants
        //optional feature to change the default title
        parametersMap.put(reportName, StringUtils.defaultIfBlank(inputMap.get(reportName).toString(), report.getName()));

        // first extract tables
        report.getReportTables().parallelStream().forEach(reportTable -> {
            // initialize a list for each reportTable --> to be injected to the JasperReport
            List<Map<?, ?>> injectedList = new ArrayList<>();
            //get the list using its name defined on DB
            //todo: impose reportTable Name
            List<?> list = Optional.ofNullable((List<?>) inputMap.get(reportTable.getName()))
                    .orElseThrow(() -> new BadReportEntryException(reportTable.getName(), report));

            list.forEach(listItem ->
                    //for each map/entry on the list
                    injectedList.add(((Map<?, ?>) listItem))
            );
            //finally, add the table
            parametersMap.put(reportTable.getName(), new JRBeanCollectionDataSource(injectedList));
        });

        // then extract first level fields
        report.getReportFields().parallelStream()
                .forEach(reportField ->
                        parametersMap.put(
                                reportField.getName(),
                                Optional.ofNullable(inputMap.get(reportField.getName()))//to fix if any ClassCastException: Cannot cast java.lang.Integer to java.lang.String
                                        .orElseThrow(() -> new BadReportEntryException(reportField.getName(), report))
                        )
                );

        report.getImages().parallelStream().forEach(img ->
                parametersMap.put(
                        img.getName(),
                        Optional.ofNullable(resourcesLoader.getImage(img.getId()))
                                .orElseThrow(() -> new BadReportEntryException(img.getName(), report))
                ));

        return parametersMap;
    }

    @Override
    public StreamingResponseBody generateDirect(ObjectNode reportRequest, String reportTitle, String language,
                                                ReportExportType accept) throws JRException {
        log.info("generateDirect() - XThread: {}", Thread.currentThread().getName());
        Map<String, Object> parametersMap = new HashMap<>();

        //step1: get names to be translated
        //todo:merge with map

        List<ReportEntry> entries = prepareEntries(reportRequest, language);
        List<String> namesToTranslate = entries.stream().filter(ReportEntry::isTranslate)
                .map(ReportEntry::getKey)
                .toList();

        //batch translate:
        if (!namesToTranslate.isEmpty()) {
            translateEntries(entries, String.join("\n", namesToTranslate), language);
        }

        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        entries.forEach(reportEntry -> {
            ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
            node.putIfAbsent("name", TextNode.valueOf(reportEntry.getKey()));
            node.putIfAbsent("value", TextNode.valueOf(String.valueOf(reportEntry.getValue())));
            arrayNode.add(node);
        });


        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(arrayNode.toString().getBytes());
        parametersMap.put("invoiceDataSource", new JsonDataSource(jsonDataStream));

        //todo: pass report id
        Report report = resourcesLoader.getReport(2L);

        parametersMap.put("title", StringUtils.defaultIfBlank(reportTitle, report.getName()));
        report.getImages().forEach(img ->
                parametersMap.put(
                        img.getName(),
                        Optional.ofNullable(resourcesLoader.getImage(img.getId()))
                                .orElseThrow(() -> new BadReportEntryException(img.getName(), img.getName()))
                ));
        //check if locale is supported and return the ReportLocale
        ReportLocale reportLocale =
                report.getLocales().stream().filter(lo -> lo.getContent().equalsIgnoreCase(language))
                        .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested Locale is not supported."));
        JasperReport jasperReport = resourcesLoader.getJasperReport(reportLocale.getId());
        JasperPrint jasperPrint = getPrint(jasperReport
                , parametersMap
                , new JREmptyDataSource());

        return outputStream -> {
            try {
                outputProcessor.export(accept, jasperPrint, outputStream);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        };

    }

    public List<ReportEntry> prepareEntries(ObjectNode reportRequest, String language) {
        List<ReportEntry> entries = new ArrayList<>();

        reportRequest.fields().forEachRemaining(entry -> {

            ReportEntry reportEntry = new ReportEntry();
            //defaulted to itself
            String name = messageSource.getMessage(entry.getKey(), null, entry.getKey(), Locale.forLanguageTag(language));

            if (!language.toLowerCase().equals(defaultLanguage) && StringUtils.equals(name, entry.getKey())) {
                reportEntry.setTranslate(true);
            }
            reportEntry.setKey(name);
            reportEntry.setValue(entry.getValue().textValue());
            entries.add(reportEntry);
        });
        return entries;
    }

    public void translateEntries(List<ReportEntry> entries, String concatenatedString, String language) {

        log.info("translation input: {}", concatenatedString);
        concatenatedString = translator.translate(concatenatedString,
                Language.ENGLISH.value(),
                language);
        log.info("translation output: {}", concatenatedString);

        //update a translation list with results
        String[] s = concatenatedString.split("\n");//for LTR & RTL

        AtomicInteger r = new AtomicInteger(0);
        //fill empty gaps
        entries.stream()
                .filter(ReportEntry::isTranslate)
                .forEach(reportEntry -> reportEntry.setKey(s[r.getAndIncrement()].strip()));
    }


    public JasperPrint getPrint(JasperReport jasperReport, Map<String, Object> parametersMap, JRDataSource dataSource) throws JRException {
        return JasperFillManager.fillReport(jasperReport
                , parametersMap
                , dataSource);
    }
}
