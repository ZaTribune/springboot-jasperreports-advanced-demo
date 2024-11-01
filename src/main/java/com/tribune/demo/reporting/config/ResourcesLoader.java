package com.tribune.demo.reporting.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.tribune.demo.reporting.db.entities.Report;
import com.tribune.demo.reporting.db.entities.ReportImage;
import com.tribune.demo.reporting.db.entities.ReportLocale;
import com.tribune.demo.reporting.db.entities.ReportTable;
import com.tribune.demo.reporting.db.repositories.ReportRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Getter
@Setter
@Component
public class ResourcesLoader implements CommandLineRunner {


    private Map<Long, JasperReport> jasperReports = new HashMap<>(1);
    private Map<Long, BufferedImage> images = new HashMap<>(1);

    private final ReportRepository reportRepository;

    public ResourcesLoader(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void run(String... args) {

        initModel1();
        initModel2();
        loadReports();
    }

    public void initModel1() {
        Report report = new Report("Auto Debit Recharge Information");

        ReportTable reportTable = new ReportTable("invoiceDataSource");
        reportTable.addListField("month");
        reportTable.addListField("invoice_date");
        reportTable.addListField("invoice_number");
        reportTable.addListField("amount");
        reportTable.addListField("num_category");
        reportTable.addListField("call_plan");
        reportTable.addListField("payment_date");
        reportTable.addListField("upload_date");
        reportTable.addListField("status");

        report.addReportTable(reportTable);

        report.addReportField("invoice_data");
        report.addReportField("customer_name");
        report.addReportField("invoice_number");
        report.addReportField("address");
        report.addReportField("amount");
        report.addReportField("mobile_number");
        report.addReportField("header");
        report.addReportField("footer");


        report.addReportImage(new ReportImage("logo", "/static/images/bank.png"));

        report.addReportLocale(new ReportLocale("en", "/static/templates/invoice_en.jrxml"));
        report.addReportLocale(new ReportLocale("ar", "/static/templates/invoice_ar.jrxml"));

        reportRepository.save(report);
    }

    public void initModel2() {
        Report report = new Report("Receipt");
        report.addReportImage(new ReportImage("logo", "/static/images/logo-com.png"));
        report.addReportLocale(new ReportLocale("ar", "/static/templates/receipt_ar.jrxml"));
        report.addReportLocale(new ReportLocale("en", "/static/templates/receipt_en.jrxml"));
        reportRepository.save(report);
    }


    public void loadReports() {

        reportRepository.findAll().forEach(report -> {
            //loading the templates
            report.getLocales().forEach(reportLocale -> {
                try {
                    JasperReport jasperReport;
                    InputStream inputStream
                            = getClass().getResourceAsStream(reportLocale.getTemplatePath());
                    jasperReport = JasperCompileManager.compileReport(inputStream);
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

}
