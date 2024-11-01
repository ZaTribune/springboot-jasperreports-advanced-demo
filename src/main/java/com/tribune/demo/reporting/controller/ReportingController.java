package com.tribune.demo.reporting.controller;


import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import com.tribune.demo.reporting.service.ReportingService;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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


    @Async("taskExecutor")
    @PostMapping(value = "/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(description = "Generate a modeled report.")
    public CompletableFuture<Void> generateV1(@Valid @RequestBody ReportRequest reportRequest,
                                              @PathVariable(value = "type") ReportExportType type,
                                              HttpServletResponse response)
            throws UnsupportedItemException {
        log.info("generateV1() - XThread: {}", Thread.currentThread().getName());
        return CompletableFuture.runAsync(() -> {
            try {
                reportingService.generateFromModel(reportRequest, type, response);
            } catch (JRException | IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        });
    }

    @Async("taskExecutor")
    @PostMapping(value = "/v2/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(description = "Generate a report using json object that needs to be converted to a standard json array while also checking for translation.")
    public CompletableFuture<Void> generateV2(@Valid @RequestBody ObjectNode reportRequest,
                                              @PathVariable(value = "type") ReportExportType exportType,
                                              @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "EN") String language,
                                              @RequestParam(defaultValue = "") String reportTitle,
                                              HttpServletResponse response) throws UnsupportedItemException {
        log.info("generateV2() - XThread: {}", Thread.currentThread().getName());
        return CompletableFuture.runAsync(() -> {
            try {
                reportingService.generateDirect(reportRequest, reportTitle, language, exportType, response);
            } catch (JRException | IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        });
    }
}
