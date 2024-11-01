package com.tribune.demo.reporting.util.processor;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;
import com.tribune.demo.reporting.model.ReportExportType;

import java.io.OutputStream;

@Slf4j
@Service
public class XlsOutputProcessor implements OutputProcessor{

/**
 * @<code>
 *    exporter.setExporterOutput(
 *        new SimpleOutputStreamExporterOutput(outputPath + "/" + name + ".xls"));
 * </code>
 **/
    @Override
    public void export(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(outputStream));

        SimpleXlsxReportConfiguration reportConfig
                = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[]{"name"});

        exporter.setConfiguration(reportConfig);
        exporter.exportReport();
    }

    @Override
    public ReportExportType getExportType() {
        return ReportExportType.XLS;
    }
}
