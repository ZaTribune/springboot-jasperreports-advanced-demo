package com.tribune.demo.reporting.controller;


import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.service.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RequestMapping("/reporting")
@Tag(name = "ReportingController")
@RestController
public class ReportingController {

    private final ReportingService reportingService;

    @Autowired
    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @PostMapping(value = "/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(description = "Generate a modeled report.")
    public ResponseEntity<StreamingResponseBody> generateV1(@Valid @RequestBody ReportRequest reportRequest,
                                                            @PathVariable(value = "type") ReportExportType type)
            throws UnsupportedItemException, JRException {
        log.info("generateV1() - XThread: {}", Thread.currentThread().getName());


        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "test_" + timestamp + "." + type.toString().toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(reportingService.generateFromModel(reportRequest, type));
    }


    @PostMapping(value = "/v2/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(description = "Generate a report using json object that needs to be converted to a standard json array while also checking for translation.")
    public ResponseEntity<StreamingResponseBody> generateV2(@Valid @RequestBody Map<String, Object> reportRequest,
                                                            @PathVariable(value = "type") ReportExportType exportType,
                                                            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "EN") String language,
                                                            @RequestParam(defaultValue = "") String reportTitle) throws UnsupportedItemException, JRException {
        log.info("generateV2() - XThread: {}", Thread.currentThread().getName());

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "test_" + timestamp + "." + exportType.toString().toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(reportingService.generateDirect(reportRequest, reportTitle, language, exportType));

    }
}
