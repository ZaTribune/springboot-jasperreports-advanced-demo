package zatribune.spring.jasperreports.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import zatribune.spring.jasperreports.db.entities.Report;
import zatribune.spring.jasperreports.db.repositories.ReportRepository;
import zatribune.spring.jasperreports.errors.BadReportEntryException;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.ReportRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class LocaleValidator implements ConstraintValidator<ValidLocale,ReportRequest> {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public void initialize(ValidLocale constraintAnnotation) {
        // nothing to be initialized here
    }

    @Override
    public boolean isValid(ReportRequest request, ConstraintValidatorContext context) {
        //that's it

        Report report=reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new BadReportEntryException("reportName", "No Report found by the given id."));

               return report.getLocales()
                .stream()
                .filter(locale -> locale.getValue().equalsIgnoreCase(request.getLocale()))
                .findFirst()
                .orElseThrow(()->new UnsupportedItemException("Locale",request.getLocale(),report.getReportLocalesValues()))!=null;
    }
}
