package zatribune.spring.jasperreports.controllers;


import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.ReportExportType;
import zatribune.spring.jasperreports.model.ReportRequest;
import zatribune.spring.jasperreports.services.ReportingService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestMapping("/reporting")
@Api(tags = "ReportingController")
@RestController
public class ReportingController {

    private final ReportingService reportingService;

    @Autowired
    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @Async("taskExecutor")
    @PostMapping(value = "/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.TEXT_HTML_VALUE,MediaType.TEXT_PLAIN_VALUE})
    @ApiOperation(value = "Generate a modeled report.")
    public CompletableFuture<Void> generate(@Valid @RequestBody ReportRequest reportRequest,
                                         @PathVariable(value = "type") ReportExportType type,
                                         HttpServletResponse response)
            throws UnsupportedItemException {
        log.info("XThread: " + Thread.currentThread().getName());
        return CompletableFuture.runAsync(()-> {
            try {
                reportingService.generateReport(reportRequest,type, response);
            } catch (JRException | IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
        });
    }

    @Async("taskExecutor")
    @PostMapping(value = "/v2/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.TEXT_HTML_VALUE,MediaType.TEXT_PLAIN_VALUE})
    @ApiOperation(value = "Generate a report using json object that needs to be converted to a standard json array while also checking for translation.")
    public CompletableFuture<Void> generate(@Valid @RequestBody ObjectNode reportRequest,
                                            @PathVariable(value = "type") ReportExportType type,
                                            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE,defaultValue = "EN") String language,
                                            HttpServletResponse response) throws UnsupportedItemException {
        log.info("XThread: " + Thread.currentThread().getName());
        return CompletableFuture.runAsync(()-> {
            try {
                reportingService.generateReport(reportRequest,language,type, response);
            } catch (JRException | IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
        });
    }


}
