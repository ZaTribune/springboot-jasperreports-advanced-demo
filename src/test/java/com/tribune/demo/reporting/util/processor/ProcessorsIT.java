package com.tribune.demo.reporting.util.processor;

import com.tribune.demo.reporting.TestUtil;
import com.tribune.demo.reporting.config.reporting.PdfExportConfig;
import com.tribune.demo.reporting.config.reporting.PdfReportConfig;
import com.tribune.demo.reporting.model.ReportExportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
public class ProcessorsIT {


    PdfOutputProcessor pdfProcessor;
    XlsOutputProcessor excelProcessor;
    HtmlOutputProcessor htmlProcessor;
    CsvOutputProcessor csvProcessor;

    DynamicOutputProcessor processor;

    Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary directory for the test
        tempDir = Files.createTempDirectory("formats-test-");
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up the temporary directory and all its contents
        try (Stream<Path> paths = Files.walk(tempDir)) { // Use try-with-resources to close the Stream<Path>
            paths.sorted((a, b) -> -a.compareTo(b)) // Reverse order to delete files before directories
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to clean up temporary files", e);
                        }
                    });
        }
    }

    @BeforeEach
    void setup() {

        PdfReportConfig pdfReportConfig = new PdfReportConfig();
        PdfExportConfig pdfExportConfig = new PdfExportConfig();

        pdfProcessor = new PdfOutputProcessor(pdfReportConfig, pdfExportConfig);
        excelProcessor = new XlsOutputProcessor();
        htmlProcessor = new HtmlOutputProcessor();
        csvProcessor = new CsvOutputProcessor();

        List<OutputProcessor> processors = List.of(pdfProcessor, excelProcessor, htmlProcessor, csvProcessor);
        processor = new DynamicOutputProcessor(processors);
    }

    @ParameterizedTest
    @CsvSource({
            "PDF",
            "XLS",
            "CSV",
            "HTML"
    })
    void getType(ReportExportType exportType) {
        switch (exportType) {
            case PDF:
                assertEquals(ReportExportType.PDF, pdfProcessor.getExportType());
                break;
            case XLS:
                assertEquals(ReportExportType.XLS, excelProcessor.getExportType());
                break;
            case CSV:
                assertEquals(ReportExportType.CSV, csvProcessor.getExportType());
                break;
            case HTML:
                assertEquals(ReportExportType.HTML, htmlProcessor.getExportType());
                break;
        }
    }

    @ParameterizedTest
    @CsvSource({
            "PDF,1000",
            "XLS,1000",
            "CSV,20",
            "HTML,1000"
    })
    void export(ReportExportType type, Integer minSize) throws JRException, IOException {
        JasperPrint jasperPrint = TestUtil.createDummyJasperPrint();

        String fix = type.name().toLowerCase();

        String fileName = "dummy." + fix;
        Path path = tempDir.resolve(fileName);

        OutputProcessor outputProcessor = processor.as(type);

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            outputProcessor.export(jasperPrint, outputStream);
        }

        log.info("Path: {}", path);

        assertTrue(Files.exists(path));
        long size = Files.size(path);

        log.info("{} size: {} Bytes", fix, size);

        assertTrue(size > minSize);
    }

}
