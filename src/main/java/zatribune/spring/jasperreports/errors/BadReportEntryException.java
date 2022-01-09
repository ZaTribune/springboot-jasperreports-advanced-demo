package zatribune.spring.jasperreports.errors;


import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.entities.ReportField;
import zatribune.spring.jasperreports.db.entities.ReportList;

import java.util.stream.Collectors;

public class BadReportEntryException extends NullPointerException {

    public BadReportEntryException(String entry, Report report) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                                "Data must include the following lists: %s and the following fields: %s. " +
                                "And Other unmatched entries are ignored.",
                        entry,
                        report.getReportLists().stream().map(ReportList::getName).collect(Collectors.toList()),
                        report.getReportFields().stream().map(ReportField::getName).collect(Collectors.toList()))
        );
    }

    public BadReportEntryException(String entry,String message) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                                "%s",
                        entry,message)
        );
    }
}
