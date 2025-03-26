package com.tribune.demo.reporting.error;


import com.tribune.demo.reporting.db.entity.Report;
import com.tribune.demo.reporting.db.entity.ReportField;
import com.tribune.demo.reporting.db.entity.ReportTable;


public class BadReportEntryException extends RuntimeException {

    public BadReportEntryException(String message) {
        super(message);
    }

    public BadReportEntryException(String entry, Report report) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                              "Data must include the following lists: %s and the following fields: %s. " +
                              "And Other unmatched entries are ignored.",
                        entry,
                        report.getReportTables().stream().map(ReportTable::getName).sorted().toList(),
                        report.getReportFields().stream().map(ReportField::getName).sorted().toList())
        );
    }


    public BadReportEntryException(String entry, String message) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                              "%s",
                        entry, message)
        );
    }
}
