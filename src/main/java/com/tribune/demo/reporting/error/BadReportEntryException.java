package com.tribune.demo.reporting.error;


import com.tribune.demo.reporting.db.entities.Report;
import com.tribune.demo.reporting.db.entities.ReportField;
import com.tribune.demo.reporting.db.entities.ReportTable;

import java.util.stream.Collectors;

public class BadReportEntryException extends NullPointerException {

    public BadReportEntryException(String entry, Report report) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                                "Data must include the following lists: %s and the following fields: %s. " +
                                "And Other unmatched entries are ignored.",
                        entry,
                        report.getReportTables().stream().map(ReportTable::getName).collect(Collectors.toList()),
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
