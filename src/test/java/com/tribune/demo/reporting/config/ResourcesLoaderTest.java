package com.tribune.demo.reporting.config;

import com.tribune.demo.reporting.TestUtil;
import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.repository.ReportRepository;
import com.tribune.demo.reporting.error.BadReportEntryException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourcesLoaderTest {

    @Mock
    ReportRepository reportRepository;

    @InjectMocks
    ResourcesLoader resourcesLoader;

    Report report;


    @Mock
    JasperReport jasperReport;

    private final Long localeId = 1L;
    private final Long imageId = 2L;
    private final Long reportId = 3L;
    private final String templatePath = "/test_template.jrxml";
    private final String imagePath = "test_image.png";

    @BeforeEach
    void setUp() {
        report = TestUtil.mockReport();


    }

    @Test
    void loadReports_whenSuccessful() {
        when(reportRepository.findAll()).thenReturn(List.of(report));

        BufferedImage mockImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        try (MockedStatic<ImageIO> io = mockStatic(ImageIO.class);
             MockedStatic<JasperCompileManager> jc = mockStatic(JasperCompileManager.class)) {

            jc.when(() -> JasperCompileManager.compileReport(any(InputStream.class)))
                    .thenReturn(jasperReport);

            io.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(mockImage);

            resourcesLoader.run();

            io.verify(() -> ImageIO.read(any(InputStream.class)), times(2));
            assertNotNull(resourcesLoader.getImage(imageId));
        }

    }
    @Test
    void loadReports_whenTemplateIOException() {

        when(reportRepository.findAll()).thenReturn(List.of(report));

        try (MockedStatic<JasperCompileManager> jc = mockStatic(JasperCompileManager.class)) {

            jc.when(() -> JasperCompileManager.compileReport(nullable(InputStream.class)))
                    .thenThrow(new JRException("Something went wrong!"));

            jc.verifyNoInteractions();

            resourcesLoader.loadReports();
            assertNull(resourcesLoader.getJasperReport(5L));
        }

    }


    @Test
    void loadReports_whenImageIOException() {
        when(reportRepository.findAll()).thenReturn(List.of(report));

        try (MockedStatic<ImageIO> io = mockStatic(ImageIO.class);
             MockedStatic<JasperCompileManager> jc = mockStatic(JasperCompileManager.class)) {

            jc.when(() -> JasperCompileManager.compileReport(any(InputStream.class)))
                    .thenReturn(jasperReport);

            jc.when(() -> ImageIO.read(any(InputStream.class)))
                    .thenThrow(new IOException("Something went wrong!"));

            resourcesLoader.loadReports();

            io.verify(() -> ImageIO.read(any(InputStream.class)), times(2));
            assertNull(resourcesLoader.getImage(imageId));
        }

    }

    @Test
    void getReport_whenSuccessful() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        assertDoesNotThrow(() -> resourcesLoader.getReport(reportId));
    }

    @Test
    void getReport_whenNotFound() {
        when(reportRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BadReportEntryException.class, () -> resourcesLoader.getReport(5L));
    }


    @Test
    void getJasperReport_whenSuccessful() {
        Map<Long, JasperReport> jasperReports = new HashMap<>(1);
        jasperReports.put(reportId, jasperReport);
        ReflectionTestUtils.setField(resourcesLoader, "jasperReports", jasperReports);

        assertDoesNotThrow(() -> resourcesLoader.getJasperReport(reportId));
    }

    @Test
    void getJasperReport_whenNotFound() {
        Map<Long, JasperReport> jasperReports = new HashMap<>(1);
        jasperReports.put(reportId, jasperReport);
        ReflectionTestUtils.setField(resourcesLoader, "jasperReports", jasperReports);

        assertNull(resourcesLoader.getJasperReport(5L));
    }

}
