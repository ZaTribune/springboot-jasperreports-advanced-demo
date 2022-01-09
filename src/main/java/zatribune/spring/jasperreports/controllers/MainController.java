package zatribune.spring.jasperreports.controllers;


import zatribune.spring.jasperreports.errors.UnsupportedLanguageException;
import zatribune.spring.jasperreports.services.PdfService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import zatribune.spring.jasperreports.validators.ValidLocale;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class MainController {


    private final PdfService pdfService;

    private static final List<String> supportedLocals = Arrays.asList("en", "ar");



    @Autowired
    public MainController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Async("taskExecutor")
    @PostMapping(value = "/pdf",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public CompletableFuture<?> generate(@RequestBody String json,
                                         @ValidLocale @RequestHeader("Accept-Language") String language
            , HttpServletResponse response)
            throws JRException, IOException {

        return CompletableFuture.completedFuture(pdfService.generatePdf(json,response));
    }
}
