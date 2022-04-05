package zatribune.spring.jasperreports.services;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zatribune.spring.jasperreports.config.ResourcesLoader;
import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.entities.ReportLocale;
import zatribune.spring.jasperreports.errors.BadReportEntryException;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.GenericResponse;
import zatribune.spring.jasperreports.model.ReportRequest;
import zatribune.spring.jasperreports.utils.processor.DynamicReportingAutowiredService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class ReportingServiceImpl implements ReportingService {

    private final ResourcesLoader resourcesLoader;

    private final DynamicReportingAutowiredService outputProcessor;

    /**
     * to export to paths
     * @<code>
     *     exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath + "/" + fileName));
     * </code>
     * we can also utilize {@link net.sf.jasperreports.engine.util.JRSaver}
     * @<code>
     *     JRSaver.saveObject(jasperReport, "employeeReport.jasper");
     * </code>
     **/
    @Autowired
    public ReportingServiceImpl(ResourcesLoader resourcesLoader, DynamicReportingAutowiredService outputProcessor) {
        this.resourcesLoader = resourcesLoader;
        this.outputProcessor=outputProcessor;
    }

    @Override
    public ResponseEntity<GenericResponse> generateReport(ReportRequest reportRequest, MediaType accept, HttpServletResponse servletResponse)
            throws JRException, IOException, UnsupportedItemException {
        log.info("XThread: " + Thread.currentThread().getName());
        //first check for report existence
        Report report = resourcesLoader.getReportRepository().findByName(reportRequest.getReportName())
                .orElseThrow(() -> new BadReportEntryException("reportName", "No Report found by the given name."));

        //then, check for locale match meaning: (a template that supports the language requested by the user).
        ReportLocale reportLocale = report.getLocales().stream()
                .filter(locale -> locale.getValue().equalsIgnoreCase(reportRequest.getLocale()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedItemException("Locale", reportRequest.getLocale(), report.getReportLocalesValues()));

        Map<String, Object> parametersMap = processReportRequest(report, reportRequest.getData());

        JasperPrint jasperPrint = JasperFillManager.fillReport(resourcesLoader.getJasperReports().get(reportLocale.getId())
                , parametersMap
                , new JREmptyDataSource());

        String fileName = String.format("%s%s", "test", new SimpleDateFormat("yyyyMMddhhmmss'.pdf'").format(new Date()));
        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        if (accept.equals(MediaType.APPLICATION_PDF) || accept.equals(MediaType.ALL)) {
            servletResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
        } else {
            servletResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        }
        outputProcessor.export(reportRequest.getExportType(),jasperPrint, servletResponse.getOutputStream());
        return ResponseEntity.ok(GenericResponse.builder()
                .message("Success")
                .reason(String.format("file with name %s returned", fileName))
                .build());
    }

    public Map<String, Object> processReportRequest(Report report, Map<String, Object> inputMap) {

        //filled then injected to the report
        Map<String, Object> parametersMap = new HashMap<>();

        // first extract lists
        report.getReportLists().parallelStream().forEach(reportList -> {
            // initialize a list for each report list -->to be injected to the JasperReport
            List<Map<?, ?>> injectedList = new ArrayList<>();
            //get the list using its name defined on DB
            List<?> list = Optional.ofNullable((List<?>) inputMap.get(reportList.getName()))
                    .orElseThrow(() -> new BadReportEntryException(reportList.getName(), report));

            list.forEach(listItem ->
                    //for each map/entry on the list
                    injectedList.add(((Map<?, ?>) listItem))
            );
            //finally,add the list
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

}
