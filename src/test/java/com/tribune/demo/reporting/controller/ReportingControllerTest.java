package com.tribune.demo.reporting.controller;

import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.service.ReportingService;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class ReportingControllerTest {

    @Mock
    ReportingService reportingService;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    ReportingController reportingController;

    ReportRequest reportRequest;
    ObjectNode reportRequestJson;
    String LANGUAGE = "EN";
    String REPORT_TITLE = "Sample Report";
    ReportExportType EXPORT_TYPE = ReportExportType.PDF;

    @BeforeEach
    void setUp() {
        reportRequest = new ReportRequest();
        reportRequestJson = JsonNodeFactory.instance.objectNode();
    }

    @Test
    void testGenerateV1Success() throws Exception {

        doNothing().when(reportingService).generateFromModel(reportRequest, EXPORT_TYPE, response);

        CompletableFuture<Void> result = reportingController.generateV1(reportRequest, EXPORT_TYPE, response);

        result.join();
        verify(reportingService, times(1)).generateFromModel(reportRequest, EXPORT_TYPE, response);
    }

    @Test
    void testGenerateV1Exception() throws Exception {

        doThrow(new IOException("Something wrong happened!")).when(reportingService)
                .generateFromModel(reportRequest, EXPORT_TYPE, response);

        CompletionException completionException = assertThrows(CompletionException.class, () -> {
            CompletableFuture<Void> result = reportingController.generateV1(reportRequest, EXPORT_TYPE, response);
            result.join();
        });

        Throwable cause = completionException.getCause();
        assertEquals(ResponseStatusException.class, cause.getClass());
        ResponseStatusException responseStatusException = (ResponseStatusException) cause;

        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
        assertEquals("Something wrong happened!", responseStatusException.getReason());
    }


    @Test
    void testGenerateV2Success() throws Exception {

        doNothing().when(reportingService).generateDirect(reportRequestJson, REPORT_TITLE, LANGUAGE, EXPORT_TYPE, response);

        CompletableFuture<Void> result = reportingController.generateV2(reportRequestJson, EXPORT_TYPE, LANGUAGE, REPORT_TITLE, response);


        result.join();
        verify(reportingService, times(1)).generateDirect(reportRequestJson, REPORT_TITLE, LANGUAGE, EXPORT_TYPE, response);
    }

    @Test
    void testGenerateV2Exception() throws Exception {

        doThrow(new IOException("Something wrong happened!")).when(reportingService)
                .generateDirect(reportRequestJson, REPORT_TITLE, LANGUAGE, EXPORT_TYPE, response);

        CompletionException completionException = assertThrows(CompletionException.class, () -> {
            CompletableFuture<Void> result = reportingController.generateV2(reportRequestJson, EXPORT_TYPE, LANGUAGE, REPORT_TITLE, response);
            result.join();
        });

        Throwable cause = completionException.getCause();
        assertEquals(ResponseStatusException.class, cause.getClass());
        ResponseStatusException responseStatusException = (ResponseStatusException) cause;

        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
        assertEquals("Something wrong happened!", responseStatusException.getReason());
    }
}
