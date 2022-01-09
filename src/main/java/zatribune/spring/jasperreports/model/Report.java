package zatribune.spring.jasperreports.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {

    private String reportName;
    private String header;
    private String footer;
    private Data data;

}
