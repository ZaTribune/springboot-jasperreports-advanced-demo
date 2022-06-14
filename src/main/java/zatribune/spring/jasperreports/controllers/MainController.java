package zatribune.spring.jasperreports.controllers;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.GenericResponse;
import zatribune.spring.jasperreports.model.ReportExportType;
import zatribune.spring.jasperreports.model.ReportRequest;
import zatribune.spring.jasperreports.services.ReportingService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestMapping("/reporting")
@RestController
public class MainController {

    private final ReportingService reportingService;

    @Autowired
    public MainController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @Async("taskExecutor")
    @PostMapping(value = "/generate/{type}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_PDF_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.TEXT_HTML_VALUE,MediaType.TEXT_PLAIN_VALUE})
    public CompletableFuture<ResponseEntity<GenericResponse>> generate(@Valid @RequestBody ReportRequest reportRequest,
                                         @PathVariable(value = "type") ReportExportType type,
                                         HttpServletResponse response)
            throws JRException, IOException, UnsupportedItemException {
        log.info("XThread: " + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(reportingService.generateReport(reportRequest,type, response));
    }

}
