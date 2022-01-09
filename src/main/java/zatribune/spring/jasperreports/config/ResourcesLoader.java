package zatribune.spring.jasperreports.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.entities.ReportImage;
import zatribune.spring.jasperreports.db.entities.ReportList;
import zatribune.spring.jasperreports.db.entities.ReportLocale;
import zatribune.spring.jasperreports.db.repositories.ReportRepository;

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

        initDB();
        loadReports();

    }

    public void initDB() {
        Report report = new Report("Auto Debit Recharge Information");

        ReportList reportList = new ReportList("invoiceDataSource");
        reportList.addListField("month");
        reportList.addListField("invoice_date");
        reportList.addListField("invoice_number");
        reportList.addListField("amount");
        reportList.addListField("num_category");
        reportList.addListField("call_plan");
        reportList.addListField("payment_date");
        reportList.addListField("upload_date");
        reportList.addListField("status");

        report.addReportList(reportList);

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
                    BufferedImage image = ImageIO.read(getClass().getResourceAsStream(img.getRelativePath()));
                    images.put(img.getId(), image);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });

        });
    }


}
