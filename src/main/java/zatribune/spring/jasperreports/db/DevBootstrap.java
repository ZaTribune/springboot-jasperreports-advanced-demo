package zatribune.spring.jasperreports.db;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.entities.ReportList;
import zatribune.spring.jasperreports.db.repositories.ReportRepository;

import java.io.FileNotFoundException;

@Component
public class DevBootstrap implements CommandLineRunner {


    private final ReportRepository repository;


    @Autowired
    public DevBootstrap(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws FileNotFoundException {

        ReportList rl = new ReportList("petList");
        rl.addListField("name");
        rl.addListField("price");

        Report r = new Report("Pets report");
        r.addReportList(rl);

        r.setTemplateEn("pet_template.vm");

        repository.save(r);


        Report report1 = new Report("Employees Report");
        report1.addReportField("note", "Note", "ملحوظة");
        report1.addReportField("reason", "Reason", "السبب");
        report1.addReportField("issued_by", "Issued By", "بتصريح من");

        ReportList reportList1 = new ReportList();
        reportList1.setName("employeesList");
        reportList1.addListField("name", "Name", "الاسم");
        reportList1.addListField("salary", "Salary", "المرتَب");
        reportList1.addListField("joining_date", "Joining Date", "تاريخ الإنضمام");
        report1.addReportList(reportList1);

        ReportList reportList2 = new ReportList();
        reportList2.setName("descriptionList");
        reportList2.addListField("description");//no need for en or ar cause it's not visible
        report1.addReportList(reportList2);

        report1.setTemplateEn("employees_template.vm");

        repository.save(report1);

        Report report3 = new Report("Auto Debit Recharge Information");

        report3.setNameAr("معلومات إعادة شحن الخصم التلقائي");
        ReportList reportList3 = new ReportList("dataList");
        reportList3.addListField("month", "Month", "الشهر");
        reportList3.addListField("invoice_date", "Invoice Date", "تاريخ الفاتورة");
        reportList3.addListField("invoice_number", "Invoice Number", "رقم الفاتورة");
        reportList3.addListField("amount", "Amount", "المبلغ");
        reportList3.addListField("num_category", "Num Category", "الفئة");
        reportList3.addListField("call_plan", "Call Plan", "خطة الإتصال");
        reportList3.addListField("payment_date", "Payment Date", "تاريخ الدفع");
        reportList3.addListField("upload_date", "Upload Date", "تاريخ الرفع");
        reportList3.addListField("status", "Status", "الحالة");

        report3.addReportList(reportList3);

        report3.addReportField("invoice_data", "Invoice Data", "محتوى الفاتورة");
        report3.addReportField("customer_name", "Customer Name", "اسم العميل");
        report3.addReportField("invoice_number", "Invoice Number", "رقم الفاتورة");
        report3.addReportField("address", "Address", "العنوان");
        report3.addReportField("amount", "Amount", "المبلغ");
        report3.addReportField("mobile_number", "Mobile Number", "رقم الهاتف");

        report3.setTemplateEn("invoice_template.vm");
        repository.save(report3);
    }
}
