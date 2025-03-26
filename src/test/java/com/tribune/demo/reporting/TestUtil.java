package com.tribune.demo.reporting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.entity.ReportImage;
import com.tribune.demo.reporting.db.entity.ReportLocale;
import com.tribune.demo.reporting.db.entity.ReportTable;
import com.tribune.demo.reporting.model.ReportRequest;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.HashMap;
import java.util.List;

public class TestUtil {


    private static final ObjectMapper mapper = new ObjectMapper();

    public static JasperPrint createDummyJasperPrint() throws JRException {
        List<Person> data = List.of(
                new Person("Muhammad", 30),
                new Person("Ali", 25)
        );

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        JasperReport jasperReport = JasperCompileManager.compileReport("src/test/resources/sample_report.jrxml");

        return JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
    }

    public static JasperPrint createMalformedJasperPrint() throws JRException {

        JasperReport jasperReport = JasperCompileManager.compileReport("src/test/resources/sample_malformed_report.jrxml");

        return JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JREmptyDataSource());
    }

    public static Report mockReport() {

        Report report = new Report();
        report.setId(1L);
        report.setName("Test Report");


        ReportLocale locale1 = new ReportLocale();
        locale1.setId(1L);
        locale1.setContent("en");
        locale1.setTemplatePath("src/test/resources/sample_report.jrxml");

        ReportLocale locale2 = new ReportLocale();
        locale2.setId(2L);
        locale2.setContent("ar");
        locale2.setTemplatePath("src/test/resources/sample_report.jrxml");

        report.addReportLocale(locale1);
        report.addReportLocale(locale2);

        report.addReportField("header");
        report.addReportField("footer");


        ReportTable table = new ReportTable();
        table.setName("invoiceDataSource");
        table.setId(1L);
        table.addListField("Name");
        table.addListField("Email");
        table.addListField("Age");

        report.addReportTable(table);

        ReportImage img1 = new ReportImage("p1", "cat.jpg");
        img1.setId(1L);
        ReportImage img2 = new ReportImage("p2", "cat.jpg");
        img2.setId(2L);

        report.addReportImage(img1);
        report.addReportImage(img2);

        return report;
    }


    public static ReportRequest mockReportRequest() throws JsonProcessingException {

        String json = """
                {
                    "reportId":1,
                    "locale":"ar",
                    "data": {
                        "reportName": "TestReport",
                        "header":"Something",
                        "footer":"© Copyright 2022",
                        "invoiceDataSource": [
                            {
                                "Name": "Somebody that I used to know",
                                "Email": "hello@gmail.com",
                                "Age":18
                            },
                            {
                                "Name": "So wake me up when it's all over",
                                "Email": "whatever@gmail.com",
                                "Age":22
                            }
                        ]
                    }
                }
                """;
        return mapper.readValue(json, ReportRequest.class);
    }

    public static ObjectNode mockDirectReportRequest() throws JsonProcessingException {

        String json = """
                {
                    "first name": "Muhammad",
                    "last name": "Ali",
                    "phone": "+0000000000",
                    "address": "Lala Land",
                    "age": "29",
                    "gender": "male"
                }
                """;
        return mapper.readValue(json, ObjectNode.class);
    }
}
