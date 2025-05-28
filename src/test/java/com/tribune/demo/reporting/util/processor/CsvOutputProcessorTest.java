package com.tribune.demo.reporting.util.processor;

import com.tribune.demo.reporting.model.ReportExportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;



@Slf4j
class CsvOutputProcessorTest extends BaseTest{

    @BeforeEach
    void setUp() {
        processor = new CsvOutputProcessor();
    }

    @Test
    void export() throws JRException, IOException {
        testExport(ReportExportType.CSV, 20);
    }

    @Test
    void getType() {
        assertEquals(ReportExportType.CSV, processor.getExportType());
    }
}