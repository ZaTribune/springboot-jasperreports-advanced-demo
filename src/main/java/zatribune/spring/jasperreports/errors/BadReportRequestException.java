package zatribune.spring.jasperreports.errors;


public class BadReportRequestException extends RuntimeException {


    public BadReportRequestException(String entry, String message) {
        super(
                String.format("Bad Request Structure. Possible error at [%s]. " +
                                "%s",
                        entry,message)
        );
    }
}
