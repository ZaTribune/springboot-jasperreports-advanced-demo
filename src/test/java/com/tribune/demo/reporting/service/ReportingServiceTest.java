package com.tribune.demo.reporting.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tribune.demo.reporting.TestUtil;
import com.tribune.demo.reporting.config.ResourcesLoader;
import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.util.processor.DynamicOutputProcessor;
import com.tribune.demo.reporting.util.translator.Translator;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
    }

    void mockResourceLoaderTasks() {
        when(resourcesLoader.getReport(anyLong())).thenReturn(report);
        when(resourcesLoader.getJasperReport(anyLong())).thenReturn(jasperReport);
        when(resourcesLoader.getImage(anyLong())).thenReturn(image);
    }

    @Test
    void generateFromModel_whenSuccessful() throws Exception {

        ReportRequest reportRequest = TestUtil.mockReportRequest();
        mockResourceLoaderTasks();


        ReportExportType exportType = ReportExportType.CSV;

        doReturn(new JasperPrint()).when(reportingService).getPrint(any(), any(), any());

        StreamingResponseBody responseBody = reportingService.generateFromModel(reportRequest, exportType);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> responseBody.writeTo(outputStream));
    }

    @Test
    void generateFromModel_whenExportException() throws Exception {
        ReportRequest reportRequest = TestUtil.mockReportRequest();
        mockResourceLoaderTasks();

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


    @Test
    void generateDirect_whenSuccessful_noTranslationNeeded() throws Exception {

        ObjectNode reportRequest = TestUtil.mockDirectReportRequest();
        mockResourceLoaderTasks();

        doReturn(new JasperPrint()).when(reportingService).getPrint(any(), any(), any());

        when(resourcesLoader.getReport(anyLong())).thenReturn(report);
        when(resourcesLoader.getJasperReport(anyLong())).thenReturn(jasperReport);
        ReflectionTestUtils.setField(reportingService, "defaultLanguage", "en");

        StreamingResponseBody responseBody = reportingService.generateDirect(reportRequest, "testReport", "en",
                ReportExportType.CSV);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> responseBody.writeTo(outputStream));
    }


    @Test
    void generateDirect_whenSuccessful_translationNeeded() throws Exception {

        ObjectNode reportRequest = TestUtil.mockDirectReportRequest();
        mockResourceLoaderTasks();

        doReturn(new JasperPrint()).when(reportingService).getPrint(any(), any(), any());

        when(resourcesLoader.getReport(anyLong())).thenReturn(report);
        when(resourcesLoader.getJasperReport(anyLong())).thenReturn(jasperReport);


        when(messageSource.getMessage(any(), any(), any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0)
        );

        when(translator.translate(any(),any(),any()))
                .thenAnswer(invocation -> invocation.getArgument(0)
        );

        ReflectionTestUtils.setField(reportingService, "defaultLanguage", "en");

        StreamingResponseBody responseBody = reportingService.generateDirect(reportRequest, "testReport", "ar",
                ReportExportType.CSV);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> responseBody.writeTo(outputStream));
    }

    @Test
    void generateDirect_whenSuccessful_translationIgnored() throws Exception {

        ObjectNode reportRequest = TestUtil.mockDirectReportRequest();
        mockResourceLoaderTasks();

        doReturn(new JasperPrint()).when(reportingService).getPrint(any(), any(), any());

        when(resourcesLoader.getReport(anyLong())).thenReturn(report);
        when(resourcesLoader.getJasperReport(anyLong())).thenReturn(jasperReport);

        ReflectionTestUtils.setField(reportingService, "defaultLanguage", "en");

        StreamingResponseBody responseBody = reportingService.generateDirect(reportRequest, "testReport", "ar",
                ReportExportType.CSV);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> responseBody.writeTo(outputStream));
    }


    @Test
    void generateDirect_whenSuccessful_whenExportException() throws Exception {

        ObjectNode reportRequest = TestUtil.mockDirectReportRequest();
        mockResourceLoaderTasks();

        doReturn(new JasperPrint()).when(reportingService).getPrint(any(), any(), any());

        when(resourcesLoader.getReport(anyLong())).thenReturn(report);
        when(resourcesLoader.getJasperReport(anyLong())).thenReturn(jasperReport);
        ReflectionTestUtils.setField(reportingService, "defaultLanguage", "en");

        StreamingResponseBody responseBody = reportingService.generateDirect(reportRequest, "testReport", "en",
                ReportExportType.CSV);
        doThrow(new JRException("Error")).when(outputProcessor).export(any(), any(), any());
        // Then: When the StreamingResponseBody writes to an output stream, it should throw a RuntimeException
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(RuntimeException.class, () -> responseBody.writeTo(outputStream));
    }

    @Test
    void getPrint_ShouldReturnMockedJasperPrint() throws JRException {
        // Given
        Map<String, Object> parametersMap = new HashMap<>();
        JasperPrint mockJasperPrint = mock(JasperPrint.class);

        // Mock static method JasperFillManager.fillReport
        try (MockedStatic<JasperFillManager> ms = mockStatic(JasperFillManager.class)) {

            ms.when(() -> JasperFillManager.fillReport(any(JasperReport.class), anyMap(), any(JRDataSource.class)))
                    .thenReturn(mockJasperPrint);

            JasperPrint result = reportingService.getPrint(jasperReport, parametersMap, new JREmptyDataSource());

            assertNotNull(result);
            assertEquals(mockJasperPrint, result);

            ms.verify(() -> JasperFillManager.fillReport(any(JasperReport.class), anyMap(), any(JRDataSource.class)), times(1));
        }
    }
}
