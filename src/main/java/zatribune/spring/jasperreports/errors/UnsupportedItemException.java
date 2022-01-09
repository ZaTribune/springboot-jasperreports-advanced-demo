package zatribune.spring.jasperreports.errors;


import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.entities.ReportField;
import zatribune.spring.jasperreports.db.entities.ReportList;

import java.util.List;
import java.util.stream.Collectors;

public class UnsupportedItemException extends NullPointerException {

    public UnsupportedItemException(String support, String unsupported, List<String> supported) {
        super(
                String.format("Unsupported %s item at '%s'. Supported %s Items are %s.",
                        support,unsupported,support,supported)
        );
    }
}
