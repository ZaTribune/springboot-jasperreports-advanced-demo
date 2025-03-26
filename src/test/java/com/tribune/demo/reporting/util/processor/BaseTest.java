package com.tribune.demo.reporting.util.processor;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.stream.Stream;

import com.tribune.demo.reporting.TestUtil;
import com.tribune.demo.reporting.model.ReportExportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
public class BaseTest {

    protected OutputProcessor processor;
    protected static Path tempDir;

    @BeforeAll
    static void init() throws Exception {
        // Create a temporary directory for the test
        tempDir = Files.createTempDirectory("formats-test-");
    }

    @AfterAll
    static void tearDown() throws Exception {
        // Clean up the temporary directory and all its contents
        try (Stream<Path> paths = Files.walk(tempDir)) {
            paths.sorted((a, b) -> -a.compareTo(b)) // Reverse order to delete files before directories
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to clean up temporary files", e);
                        }
                    });
        }
    }

    void testExport(ReportExportType type, int minSize) throws JRException, IOException {
        JasperPrint jasperPrint = TestUtil.createDummyJasperPrint();

        String fix = type.toString().toLowerCase();

        String fileName = "dummy." + fix;
        Path path = tempDir.resolve(fileName);

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            processor.export(jasperPrint, outputStream);
        }

        log.info("Path: {}", path);

        assertTrue(Files.exists(path));
        long size = Files.size(path);

        log.info("{} size: {} Bytes", fix, size);

        assertTrue(size > minSize);
    }
}

