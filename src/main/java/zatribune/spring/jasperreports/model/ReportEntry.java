package zatribune.spring.jasperreports.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportEntry {

    private int index;
    private String key;
    private String value;
    private boolean translate;
}
