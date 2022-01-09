package zatribune.spring.jasperreports.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItem {

    private String month;
    private String invoice_date;
    private String invoice_number;
    private String amount;
    private String num_category;
    private String call_plan;
    private String payment_date;
    private String upload_date;
    private String status;
}
