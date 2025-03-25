package com.tribune.demo.reporting.util.processor;


import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import com.tribune.demo.reporting.model.ReportExportType;
import net.sf.jasperreports.engine.JasperPrint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DynamicOutputProcessorTest {

    DynamicOutputProcessor processor;


    PdfOutputProcessor pdfProcessor;
    XlsOutputProcessor excelProcessor;
    HtmlOutputProcessor htmlProcessor;
    CsvOutputProcessor csvProcessor;

    @BeforeEach
    void setUp() {
        // Create mock OutputProcessor instances
        pdfProcessor = mock(PdfOutputProcessor.class);
        when(pdfProcessor.getExportType()).thenReturn(ReportExportType.PDF);

        excelProcessor = mock(XlsOutputProcessor.class);
        when(excelProcessor.getExportType()).thenReturn(ReportExportType.XLS);

        htmlProcessor = mock(HtmlOutputProcessor.class);
        when(htmlProcessor.getExportType()).thenReturn(ReportExportType.HTML);

        csvProcessor = mock(CsvOutputProcessor.class);
        when(csvProcessor.getExportType()).thenReturn(ReportExportType.CSV);

        // Injecting all processors into the service
        List<OutputProcessor> processors = List.of(pdfProcessor, excelProcessor, htmlProcessor, csvProcessor);
        processor = new DynamicOutputProcessor(processors);
    }

    @Test
    void testInitialization_WithValidProcessors() {
        // Verify that the processorServices map is correctly populated

        assertEquals(4, processor.getSize());
        assertEquals(pdfProcessor, processor.as(ReportExportType.PDF));
        assertEquals(excelProcessor, processor.as(ReportExportType.XLS));
        assertEquals(htmlProcessor, processor.as(ReportExportType.HTML));
        assertEquals(csvProcessor, processor.as(ReportExportType.CSV));
    }

    @ParameterizedTest
    @CsvSource({
            "PDF",
            "XLS",
            "CSV",
            "HTML"
    })
    void testExport_DelegatesToCorrectProcessor(String exportType) throws Exception {

        JasperPrint jasperPrint = mock(JasperPrint.class);
        OutputStream outputStream = mock(OutputStream.class);


        ReportExportType type = ReportExportType.valueOf(exportType);
        processor.export(type, jasperPrint, outputStream);


        OutputProcessor expectedProcessor = processor.as(type);

        assertEquals(type, expectedProcessor.getExportType());
        verify(expectedProcessor).export(jasperPrint, outputStream);

        // Verify no interactions with other processors
        List<OutputProcessor> otherProcessors = Stream.of(pdfProcessor, excelProcessor, csvProcessor, htmlProcessor)
                .filter(p -> p != expectedProcessor)
                .toList();

        for (OutputProcessor processor : otherProcessors) {
            verify(processor, times(0)).export(jasperPrint, outputStream);
        }
    }

    @Test
    void testExport_ThrowsExceptionForUnsupportedExportType() {

        JasperPrint jasperPrint = mock(JasperPrint.class);

        assertThrows(IllegalArgumentException.class, () -> processor.export(ReportExportType.valueOf("whatever"), jasperPrint, mock(OutputStream.class)));
    }

}