package com.tribune.demo.reporting.service;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.tribune.demo.reporting.config.ResourcesLoader;
import com.tribune.demo.reporting.db.entities.Report;
import com.tribune.demo.reporting.db.entities.ReportLocale;
import com.tribune.demo.reporting.error.BadReportEntryException;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportEntry;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.util.translator.Translator;
import com.tribune.demo.reporting.util.processor.DynamicOutputProcessorService;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReportingServiceImpl implements ReportingService {

    private final ResourcesLoader resourcesLoader;

    private final DynamicOutputProcessorService outputProcessor;

    private final MessageSource messageSource;
    private final Translator translator;

    @Value("${system.default-language}")
    private String defaultLanguage;

    /**
     * to export to paths
     *
     * @<code> exporter.setExporterOutput(new SimpleOutputStreamExporterOutput ( outputPath + " / " + fileName));
     * </code>
     * we can also utilize {@link net.sf.jasperreports.engine.util.JRSaver}
     * @<code> JRSaver.saveObject(jasperReport, " employeeReport.jasper ");
     * </code>
     **/

    @Override
    public void generateFromModel(ReportRequest reportRequest, ReportExportType accept, HttpServletResponse servletResponse)
            throws JRException, IOException, UnsupportedItemException {
        log.info("generateFromModel() - XThread: {}", Thread.currentThread().getName());

        //first check for report existence
        Report report = resourcesLoader.getReportRepository().findById(reportRequest.getReportId())
                .orElseThrow(() -> new BadReportEntryException("reportId", "No Report found by the given id."));

        //then, check for locale match meaning: (a template that supports the language requested by the user).
        ReportLocale reportLocale = report.getLocales().stream()
                .filter(l -> l.getContent().equalsIgnoreCase(reportRequest.getLocale()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedItemException("Locale", reportRequest.getLocale(), report.getReportLocalesValues()));

        Map<String, Object> parametersMap = processReportRequest(report, reportRequest.getData());

        JasperPrint jasperPrint = JasperFillManager.fillReport(resourcesLoader.getJasperReports().get(reportLocale.getId())
                , parametersMap
                , new JREmptyDataSource());


        String fileName = String.format("%s%s", "test", new SimpleDateFormat("yyyyMMddhhmmss'." + accept.toString().toLowerCase() + "'")
                .format(new Date()));

        log.info("fileName: {}", fileName);

        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        outputProcessor.export(accept, jasperPrint, servletResponse.getOutputStream());
    }

    public Map<String, Object> processReportRequest(Report report, Map<String, Object> inputMap) {

        //filled then injected to the report
        Map<String, Object> parametersMap = new HashMap<>();

        final String reportName = "reportName";//to be within constants
        if (inputMap.get(reportName) != null)//optional feature to change the default title
            parametersMap.put(reportName, inputMap.get(reportName));
        else
            parametersMap.put(reportName, report.getName());
        // first extract lists
        report.getReportTables().parallelStream().forEach(reportList -> {
            // initialize a list for each report list -->to be injected to the JasperReport
            List<Map<?, ?>> injectedList = new ArrayList<>();
            //get the list using its name defined on DB
            List<?> list = Optional.ofNullable((List<?>) inputMap.get(reportList.getName()))
                    .orElseThrow(() -> new BadReportEntryException(reportList.getName(), report));

            list.forEach(listItem ->
                    //for each map/entry on the list
                    injectedList.add(((Map<?, ?>) listItem))
            );
            //finally, add the list
            parametersMap.put(reportList.getName(), new JRBeanCollectionDataSource(injectedList));
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
                        Optional.ofNullable(resourcesLoader.getImages().get(img.getId()))
                                .orElseThrow(() -> new BadReportEntryException(img.getName(), report))
                ));

        return parametersMap;
    }

    @Override
    public void generateDirect(ObjectNode reportRequest, String reportTitle, String language,
                               ReportExportType accept, HttpServletResponse servletResponse) throws JRException, IOException {
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
            translateEntries(entries, String.join(",", namesToTranslate), language);
        }

        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        entries.forEach(reportEntry -> {
            ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
            node.putIfAbsent("name", TextNode.valueOf(reportEntry.getKey()));
            node.putIfAbsent("value", TextNode.valueOf(String.valueOf(reportEntry.getValue())));
            arrayNode.add(node);
        });


        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(arrayNode.toString().getBytes());
        try {
            parametersMap.put("invoiceDataSource", new JsonDataSource(jsonDataStream));
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        //todo: pass report id
        Report report = resourcesLoader.getReportRepository()
                .findById(2L)
                .orElseThrow();

        parametersMap.put("title", StringUtils.defaultIfBlank(reportTitle, report.getName()));
        report.getImages().forEach(img ->
                parametersMap.put(
                        img.getName(),
                        Optional.ofNullable(resourcesLoader.getImages().get(img.getId()))
                                .orElseThrow(() -> new BadReportEntryException(img.getName(), img.getName()))
                ));
        //check if locale is supported and return the ReportLocale
        ReportLocale reportLocale =
                report.getLocales().stream().filter(lo -> lo.getContent().equalsIgnoreCase(language))
                        .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested Locale is not supported."));
        JasperReport jasperReport = resourcesLoader.getJasperReports().get(reportLocale.getId());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport
                , parametersMap
                , new JREmptyDataSource());
        String fileName = String.format("%s%s", "test", new SimpleDateFormat("yyyyMMddhhmmss'." + accept.toString().toLowerCase() + "'")
                .format(new Date()));
        log.info("fileName: {}", fileName);
        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        outputProcessor.export(accept, jasperPrint, servletResponse.getOutputStream());
    }

    public List<ReportEntry> prepareEntries(ObjectNode reportRequest, String language) {
        List<ReportEntry> entries = new ArrayList<>();
        AtomicInteger mainIndex = new AtomicInteger(0);
        reportRequest.fields().forEachRemaining(entry -> {
            mainIndex.getAndIncrement();
            String name;
            ReportEntry en = new ReportEntry();
            name = messageSource.getMessage(entry.getKey(), null, entry.getKey(), Locale.forLanguageTag(language));

            if (!language.toLowerCase().equals(defaultLanguage) && name != null && name.equals(entry.getKey())) {
                en.setTranslate(true);
            }
            en.setKey(name);
            en.setValue(entry.getValue().textValue());
            entries.add(en);
        });
        return entries;
    }

    public void translateEntries(List<ReportEntry> entries, String concatenatedString, String language) {

        log.info("translation input: {}", concatenatedString);
        concatenatedString = translator.translate(translator.breakCamel(concatenatedString),
                Language.ENGLISH.value(),
                language);
        log.info("translation output: {}", concatenatedString);

        //update a translation list with results
        String[] s = concatenatedString.split("[,،]");//for LTR & RTL

        AtomicInteger r = new AtomicInteger(0);
        //fill empty gaps
        entries.stream()
                .filter(ReportEntry::isTranslate)
                .forEach(reportEntry -> reportEntry.setKey(s[r.getAndIncrement()].strip()));
    }


}
