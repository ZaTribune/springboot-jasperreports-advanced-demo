package zatribune.spring.jasperreports.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileUtils {


    public List<String> fetchTxtLines(String path){
        List<String> collect = null;
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)){
            collect = bufferedReader.lines()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return collect;
    }
}
