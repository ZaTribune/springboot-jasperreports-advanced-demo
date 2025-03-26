package com.tribune.demo.reporting.config;

import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.entity.ReportImage;
import com.tribune.demo.reporting.db.entity.ReportLocale;
import com.tribune.demo.reporting.db.entity.ReportTable;
import com.tribune.demo.reporting.db.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("local")
@RequiredArgsConstructor
@Component
public class DevBootstrap implements SmartInitializingSingleton {


    private final ReportRepository reportRepository;


    @Override
    public void afterSingletonsInstantiated() {
        log.info("Loading local data ...");
        initModel1();
        initModel2();
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
}
