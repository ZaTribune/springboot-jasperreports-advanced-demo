package com.tribune.demo.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.service.ReportingService;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ReportingController.class)
class ReportingControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    ReportingService reportingService;


    ReportRequest reportRequest;
    ObjectNode reportRequestJson;


    @BeforeEach
    void setUp() {
        reportRequest = new ReportRequest();
        reportRequestJson = JsonNodeFactory.instance.objectNode();
    }

    @Test
    void testGenerateReport_Success() throws Exception {

        ReportRequest request = new ReportRequest();
        request.setReportId(123L);
        request.setLocale("ar");
        request.setData(Map.of("a","1","b","2"));

        ReportExportType type = ReportExportType.PDF;

        StreamingResponseBody mockStream = outputStream -> outputStream.write("Mocked PDF Content".getBytes());

        when(reportingService.generateFromModel(request, type)).thenReturn(mockStream);

        mockMvc.perform(post("/reporting/generate/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGenerateReport_whenValidationError() throws Exception {

        ReportRequest request = new ReportRequest();
        request.setReportId(123L);
        request.setData(Map.of("a","1","b","2"));

        ReportExportType type = ReportExportType.PDF;

        mockMvc.perform(post("/reporting/generate/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Error"));
    }


    @Test
    void testGenerateReportV2_Success() throws Exception {

        Map<String,Object> reportRequest = new HashMap<>();
        reportRequest.put("key", "value");
        ReportExportType exportType = ReportExportType.PDF;
        String language = "EN";
        String reportTitle = "Test Report";

        StreamingResponseBody mockStream = outputStream -> outputStream.write("Mocked PDF Content".getBytes());

        when(reportingService.generateDirect(any(), eq(reportTitle), eq(language), eq(exportType))).thenReturn(mockStream);

        mockMvc.perform(post("/reporting/v2/generate/{type}", exportType)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, language)
                        .param("reportTitle", reportTitle)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGenerateReportV2_UnsupportedItemException() throws Exception {

        Map<String,Object> reportRequest = new HashMap<>();
        reportRequest.put("key", "value");

        String language = "Fr";
        String reportTitle = "Test Report";

        when(reportingService.generateDirect(any(), eq(reportTitle), eq(language), eq(ReportExportType.PDF)))
                .thenThrow(new UnsupportedItemException("Unsupported locale type", "locale", List.of("Ar","En")));


        mockMvc.perform(post("/reporting/v2/generate/{type}", ReportExportType.PDF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, language)
                        .param("reportTitle", reportTitle)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unsupported Unsupported locale type item at 'locale'. Supported Unsupported locale type Items are [Ar, En]."));
    }

    @Test
    void testGenerateReportV2_JRException() throws Exception {

        Map<String,Object> reportRequest = new HashMap<>();
        reportRequest.put("key", "value");
        ReportExportType exportType = ReportExportType.PDF;
        String language = "EN";
        String reportTitle = "Test Report";

        when(reportingService.generateDirect(any(), eq(reportTitle), eq(language), eq(exportType)))
                .thenThrow(new JRException("Error generating report"));


        mockMvc.perform(post("/reporting/v2/generate/{type}", exportType)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, language)
                        .param("reportTitle", reportTitle)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error generating report"));
    }
}
