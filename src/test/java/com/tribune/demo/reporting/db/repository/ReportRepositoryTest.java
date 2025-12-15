package com.tribune.demo.reporting.db.repository;

import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.entity.ReportLocale;
import com.tribune.demo.reporting.db.entity.ReportTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
class ReportRepositoryTest {

    @Autowired
    ReportRepository reportRepository;


    @BeforeAll
    static void init(@Autowired ReportRepository reportRepository) {
        Report report = new Report();
        report.setName("Test Report");

        report.addReportField("F1");

        report.addReportField("F2");

        ReportTable reportTable = new ReportTable();
        reportTable.setName("Test Table");
        reportTable.addListField("Some Table list field");

        report.addReportTable(reportTable);


        ReportLocale locale1 = new ReportLocale();
        locale1.setContent("English");

        ReportLocale locale2 = new ReportLocale();
        locale2.setContent("Arabic");

        report.addReportLocale(locale1);
        report.addReportLocale(locale2);


        reportRepository.save(report);
    }


    @Test
    void findById_ShouldReturnReportWithFieldsAndTables() {
        // Act
        Optional<Report> optionalReport = reportRepository.findById(1L);

        // Assert
        assertTrue(optionalReport.isPresent());
        Report report = optionalReport.get();
        assertEquals(1L, report.getId());
        assertEquals("Test Report", report.getName());
        assertFalse(report.getReportFields().isEmpty());
        assertFalse(report.getReportTables().isEmpty());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenReportDoesNotExist() {

        Optional<Report> optionalReport = reportRepository.findById(999L);

        assertTrue(optionalReport.isEmpty());
    }

    @Test
    void findByName_ShouldReturnReportWithFieldsAndTables() {

        Optional<Report> optionalReport = reportRepository.findByName("Test Report");

        assertTrue(optionalReport.isPresent());
        Report report = optionalReport.get();
        assertEquals(1L, report.getId());
        assertEquals("Test Report", report.getName());
        assertFalse(report.getReportFields().isEmpty());
        assertFalse(report.getReportTables().isEmpty());
    }

    @Test
    void findByName_ShouldReturnEmptyOptional_WhenReportDoesNotExist() {

        Optional<Report> optionalReport = reportRepository.findByName("Nonexistent Report");

        assertTrue(optionalReport.isEmpty());
    }
}