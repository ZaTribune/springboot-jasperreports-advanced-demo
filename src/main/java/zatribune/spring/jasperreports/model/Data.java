package zatribune.spring.jasperreports.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Data {

    private String invoice_data;
    private String invoice_number;
    private String customer_name;
    private String address;
    private String amount;
    private String mobile_number;
    private List<DataItem> dataList;
}
