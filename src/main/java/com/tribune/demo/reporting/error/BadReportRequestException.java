package com.tribune.demo.reporting.error;


public class BadReportRequestException extends RuntimeException {


    public BadReportRequestException(String entry, String message) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                                "%s",
                        entry,message)
        );
    }
}
