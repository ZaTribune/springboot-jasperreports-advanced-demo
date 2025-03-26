package com.tribune.demo.reporting.util.processor;

import com.tribune.demo.reporting.TestUtil;
import com.tribune.demo.reporting.model.ReportExportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class XlsOutputProcessorTest extends BaseTest {


    @BeforeEach
    void setUp() {
        processor = new XlsOutputProcessor();
    }


    @Test
    void export() throws JRException, IOException {
        testExport(ReportExportType.XLS, 1000);
    }

    @Test
    void getType() {
        assertEquals(ReportExportType.XLS, processor.getExportType());
    }
}