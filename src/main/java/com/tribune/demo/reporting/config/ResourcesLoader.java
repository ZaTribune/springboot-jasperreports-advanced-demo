package com.tribune.demo.reporting.config;


import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.repository.ReportRepository;
import com.tribune.demo.reporting.error.BadReportEntryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class ResourcesLoader implements CommandLineRunner {


    private final Map<Long, JasperReport> jasperReports = new HashMap<>(1);
    private final Map<Long, BufferedImage> images = new HashMap<>(1);

    private final ReportRepository reportRepository;


    @Override
    public void run(String @NonNull ... args) {
        log.info("Loading jasper reports");
        loadReports();
    }


    public void loadReports() {

        reportRepository.findAll().forEach(report -> {
            //loading the templates
            report.getLocales().forEach(reportLocale -> {
                try {
                    InputStream inputStream
                            = getClass().getResourceAsStream(reportLocale.getTemplatePath());
                    JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                    jasperReports.put(reportLocale.getId(), jasperReport);
                } catch (JRException e) {
                    log.error(e.getMessage());
                }
            });
            //then loading images
            report.getImages().forEach(img -> {
                try {
                    BufferedImage image = ImageIO.read(new ClassPathResource(img.getRelativePath()).getInputStream());
                    images.put(img.getId(), image);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
        });
    }

    public Report getReport(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new BadReportEntryException("reportId", "No Report found by the given id."));
    }

    public JasperReport getJasperReport(Long id) {
        return jasperReports.get(id);
    }

    public BufferedImage getImage(Long id) {
        return images.get(id);
    }

}
