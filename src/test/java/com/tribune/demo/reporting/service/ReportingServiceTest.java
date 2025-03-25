package com.tribune.demo.reporting.service;

import com.tribune.demo.reporting.TestUtil;
import com.tribune.demo.reporting.config.ResourcesLoader;
import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.util.processor.DynamicOutputProcessor;
import com.tribune.demo.reporting.util.translator.Translator;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    ResourcesLoader resourcesLoader;


    @Mock
    DynamicOutputProcessor outputProcessor;

    @Mock
    MessageSource messageSource;

    @Mock
    Translator translator;


    @Spy
    @InjectMocks
    ReportingServiceImpl reportingService;


    @Mock
    JasperReport jasperReport;

    @Mock
    BufferedImage image;


    Report report;


    @BeforeEach
    void setUp() {

        report = TestUtil.mockReport();

        when(resourcesLoader.getReport(anyLong())).thenReturn(report);
        when(resourcesLoader.getJasperReport(anyLong())).thenReturn(jasperReport);
        when(resourcesLoader.getImage(anyLong())).thenReturn(image);
    }

    @Test
    void generateFromModel_whenSuccessful() throws Exception {

        ReportRequest reportRequest = TestUtil.mockReportRequest();


        ReportExportType exportType = ReportExportType.CSV;

        doReturn(new JasperPrint()).when(reportingService).getPrint(any(), any(), any());

        StreamingResponseBody responseBody = reportingService.generateFromModel(reportRequest, exportType);
        // Then: When the StreamingResponseBody writes to an output stream, it should throw a RuntimeException
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> responseBody.writeTo(outputStream));
    }

    @Test
    void generateFromModel_whenExportException() throws Exception {
        ReportRequest reportRequest = TestUtil.mockReportRequest();

        ReportExportType exportType = ReportExportType.CSV;

        JasperPrint print = new JasperPrint();
        print.setName("Zoro");

        doReturn(print).when(reportingService).getPrint(any(), any(), any());

        doThrow(new JRException("Error")).when(outputProcessor).export(any(), any(), any());

        StreamingResponseBody responseBody = reportingService.generateFromModel(reportRequest, exportType);

        // Then: When the StreamingResponseBody writes to an output stream, it should throw a RuntimeException
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(RuntimeException.class, () -> responseBody.writeTo(outputStream));

    }
}
