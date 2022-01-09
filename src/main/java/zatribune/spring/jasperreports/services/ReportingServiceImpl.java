package zatribune.spring.jasperreports.services;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zatribune.spring.jasperreports.config.ResourcesLoader;
import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.entities.ReportLocale;
import zatribune.spring.jasperreports.errors.BadReportEntryException;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.ReportRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class ReportingServiceImpl implements ReportingService {


    @Value("${jasperreports.output-path}")
    private String outputPath;

    private final ResourcesLoader resourcesLoader;

    public ReportingServiceImpl(ResourcesLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }

    @Override
    public ResponseEntity<?> generateReport(ReportRequest reportRequest, HttpServletResponse servletResponse)
            throws JRException, IOException, UnsupportedItemException {
        log.info("XThread: " + Thread.currentThread().getName());
        //first check for report existence
        Report report = resourcesLoader.getReportRepository().findByName(reportRequest.getReportName())
                .orElseThrow(() -> new BadReportEntryException("reportName", "No Report found by the given name."));

        //then, check for locale match meaning: (a template that supports the language requested by the user).
        ReportLocale reportLocale=report.getLocales().stream()
                .filter(locale -> locale.getValue().toLowerCase().equals(reportRequest.getLocale().toLowerCase()))
                .findFirst()
                .orElseThrow(()->new UnsupportedItemException("Locale",reportRequest.getLocale(),report.getReportLocalesValues()));

        Map<String,Object>parametersMap= processReportRequest(report,reportRequest.getData());

        JasperPrint jasperPrint = JasperFillManager.fillReport(resourcesLoader.getJasperReports().get(reportLocale.getId())
                , parametersMap
                , new JREmptyDataSource());

        String fileName = String.format("%s%s", "test", new SimpleDateFormat("yyyyMMddhhmmss'.pdf'").format(new Date()));

        servletResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        return exportToPdf(jasperPrint, servletResponse.getOutputStream());
    }

    public Map<String,Object> processReportRequest(Report report, Map<String, Object> inputMap) {

        //filled then injected to the report
        Map<String, Object> parametersMap = new HashMap<>();

        // first extract lists
        report.getReportLists().parallelStream().forEach(reportList -> {
            // initialize a list for each report list -->to be injected to the JasperReport
            List<Map<?, ?>> injectedList = new ArrayList<>();
            //get the list using its name defined on DB
            List<?> list = Optional.ofNullable((List<?>) inputMap.get(reportList.getName()))
                    .orElseThrow(() -> new BadReportEntryException(reportList.getName(), report));

            list.forEach((listItem) -> {//for each map/entry on the list

                injectedList.add(((Map<?, ?> )listItem));
            });
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

    public ResponseEntity<?> exportToPdf(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(
//                new SimpleOutputStreamExporterOutput(outputPath+"/"+reportName+".pdf"));

        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(outputStream));


        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("ZaTribune");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exportConfig.setCompressed(true);

        exporter.setConfiguration(reportConfig);

        exporter.exportReport();
        return ResponseEntity.ok("Ok");
    }

    public void exportToXls(JasperPrint jasperPrint, String name) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(outputPath + "/" + name + ".xls"));

        SimpleXlsxReportConfiguration reportConfig
                = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[]{name});

        exporter.setConfiguration(reportConfig);
        exporter.exportReport();
    }

    public void exportToCsv(JasperPrint jasperPrint, String name) throws JRException {

        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(
                new SimpleWriterExporterOutput(outputPath + "/" + name + ".csv"));

        exporter.exportReport();
    }

    public void exportToHtml(JasperPrint jasperPrint, String name) throws JRException {

        HtmlExporter exporter = new HtmlExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleHtmlExporterOutput(outputPath + "/" + name + ".html"));

        exporter.exportReport();
    }

    /**
     * To avoid compiling it every time, we can save it to a file
     **/
    public boolean saveReport(InputStream jasperReport) throws JRException {
        JRSaver.saveObject(jasperReport, "employeeReport.jasper");


        //todo: return if file exists
        return true;
    }
}
