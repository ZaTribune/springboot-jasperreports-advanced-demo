package com.tribune.demo.reporting.util.processor;

import com.tribune.demo.reporting.model.ReportExportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvMetadataReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Slf4j
@Service
public class CsvOutputProcessor implements OutputProcessor {

    /**
     * <pre><code>
     *   exporter.setExporterOutput(
     *          new SimpleWriterExporterOutput ( outputPath + " / test.csv "));
     * </code></pre>
     */
    @Override
    public void export(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        log.info("{} exporting print: {}", getClass().getSimpleName(), jasperPrint.getName());

        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));

        SimpleCsvMetadataReportConfiguration configuration = new SimpleCsvMetadataReportConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }

    @Override
    public ReportExportType getExportType() {
        return ReportExportType.CSV;
    }
}
