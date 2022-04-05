package zatribune.spring.jasperreports.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zatribune.spring.jasperreports.model.GenericResponse;
import zatribune.spring.jasperreports.utils.FileUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/notes")
@RestController
public class NotesController {

    private final FileUtils fileUtils;

    @Autowired
    public NotesController(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @GetMapping("/implementation")
    public ResponseEntity<GenericResponse> getImplementationNotes() throws IOException {


        List<String> collect=fileUtils.fetchTxtLines(new ClassPathResource("/static/txt/implementation.txt").getFile());

        return ResponseEntity.ok(GenericResponse.builder()
                .code(2000)
                .message("Success")
                .data(collect)
                .build());

    }
}
