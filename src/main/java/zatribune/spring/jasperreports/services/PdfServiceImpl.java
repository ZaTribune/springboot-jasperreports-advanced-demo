package zatribune.spring.jasperreports.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import zatribune.spring.jasperreports.config.ResourcesLoader;
import zatribune.spring.jasperreports.model.Report;
import net.sf.jasperreports.engine.*;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class PdfServiceImpl implements PdfService{


    @Value("${jasperreports.output-path}")
    private String outputPath;


    private final ResourcesLoader resourcesLoader;

    public PdfServiceImpl(ResourcesLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }

    @Override
    public ResponseEntity<?> generatePdf(String json, HttpServletResponse servletResponse) throws JRException, IOException {
        log.info("XThread: "+Thread.currentThread().getName());


        Map<String,Object> parametersMap=new HashMap<>();

        ObjectMapper mapper=new ObjectMapper();
        Report report=mapper.readValue(json, Report.class);


        parametersMap.put("header",report.getHeader());
        parametersMap.put("footer",report.getFooter());
        parametersMap.put("customer_name",report.getData().getCustomer_name());
        parametersMap.put("address",report.getData().getAddress());
        parametersMap.put("mobile_number",report.getData().getMobile_number());
        parametersMap.put("invoice_data",report.getData().getInvoice_data());
        parametersMap.put("invoice_number",report.getData().getInvoice_number());
        parametersMap.put("amount",report.getData().getAmount());
        parametersMap.put("reportName",report.getReportName());
        parametersMap.put("logo", resourcesLoader.getImages().get(0));

        parametersMap.put("invoiceDataSource",new JRBeanCollectionDataSource(report.getData().getDataList()));


        JasperPrint jasperPrint= JasperFillManager.fillReport(resourcesLoader.getReports().get(0),parametersMap
                ,new JRBeanCollectionDataSource(report.getData().getDataList()));

        String fileName = String.format("%s%s", "dito", new SimpleDateFormat("yyyyMMddhhmmss'.pdf'").format(new Date()));

        servletResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        return exportToPdf(jasperPrint,servletResponse.getOutputStream());
    }

    public ResponseEntity<?>exportToPdf(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
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
        exportConfig.setMetadataAuthor("Atos");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exportConfig.setCompressed(true);

        exporter.setConfiguration(reportConfig);

        exporter.exportReport();
        return ResponseEntity.ok("Ok");
    }

    public void exportToXls(JasperPrint jasperPrint,String name) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(outputPath+"/"+name+".xls"));

        SimpleXlsxReportConfiguration reportConfig
                = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[] { name });

        exporter.setConfiguration(reportConfig);
        exporter.exportReport();
    }

    public void exportToCsv(JasperPrint jasperPrint,String name) throws JRException {

        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(
                new SimpleWriterExporterOutput(outputPath+"/"+name+".csv"));

        exporter.exportReport();
    }

    public void exportToHtml(JasperPrint jasperPrint,String name) throws JRException {

        HtmlExporter exporter = new HtmlExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleHtmlExporterOutput(outputPath+"/"+name+".html"));

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
