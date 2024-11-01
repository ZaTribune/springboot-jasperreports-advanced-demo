package com.tribune.demo.reporting.util.processor;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.stereotype.Service;
import com.tribune.demo.reporting.model.ReportExportType;

import java.io.OutputStream;

@Slf4j
@Service
public class HtmlOutputProcessor implements OutputProcessor{

/**
 * @<code>
 *     exporter.setExporterOutput(
 *        new SimpleHtmlExporterOutput(outputPath + "/" + name + ".html"));
 * </code>
 **/

    @Override
    public void export(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        log.info("{} exporting print: {}",getClass().getSimpleName(),jasperPrint.getName());
        HtmlExporter exporter = new HtmlExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(
                new SimpleHtmlExporterOutput(outputStream));

        exporter.exportReport();
    }

    @Override
    public ReportExportType getExportType() {
        return ReportExportType.HTML;
    }
}
