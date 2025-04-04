package com.tribune.demo.reporting.util.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;
import net.sf.jasperreports.pdf.SimplePdfReportConfiguration;
import org.springframework.stereotype.Service;
import com.tribune.demo.reporting.config.reporting.PdfExportConfig;
import com.tribune.demo.reporting.config.reporting.PdfReportConfig;
import com.tribune.demo.reporting.model.ReportExportType;

import java.io.OutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class PdfOutputProcessor implements OutputProcessor{

    private final PdfReportConfig pdfReportConfig;

    private final PdfExportConfig pdfExportConfig;

    @Override
    public void export(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        log.info("{} exporting print: {}",getClass().getSimpleName(),jasperPrint.getName());
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();

        reportConfig.setSizePageToContent(pdfReportConfig.getSizePageToContent());
        reportConfig.setForceLineBreakPolicy(pdfReportConfig.getForceLineBreakPolicy());

        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();

        exportConfig.setMetadataAuthor(pdfExportConfig.getMetadataAuthor());
        exportConfig.setEncrypted(pdfExportConfig.getReportEncrypted());
        exportConfig.setAllowedPermissionsHint(pdfExportConfig.getAllowedPermissionsHint());
        exportConfig.setCompressed(pdfExportConfig.getReportCompressed());

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
    }

    @Override
    public ReportExportType getExportType() {
        return ReportExportType.PDF;
    }
}
