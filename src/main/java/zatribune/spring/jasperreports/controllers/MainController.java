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
import zatribune.spring.jasperreports.model.ReportRequest;
import zatribune.spring.jasperreports.services.ReportingService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/report")
@RestController
public class MainController {


    private final ReportingService reportingService;

    @Autowired
    public MainController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @Async("taskExecutor")
    @PostMapping(value = "/pdf", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE,MediaType.APPLICATION_PDF_VALUE})
    public CompletableFuture<?> generate(@Valid @RequestBody ReportRequest reportRequest,
                                         @RequestHeader(name = "Accept") MediaType accept,
                                         HttpServletResponse response)
            throws JRException, IOException, UnsupportedItemException {

        log.info("XThread: " + Thread.currentThread().getName());
        log.info("{}",accept);
        return CompletableFuture.completedFuture(reportingService.generateReport(reportRequest,accept, response));
    }

}
