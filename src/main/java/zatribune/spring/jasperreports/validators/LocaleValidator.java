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

    }

//     if (!supportedLocals.contains(language))
//            throw new UnsupportedLanguageException(language, supportedLocals);

    @Override
    public boolean isValid(ReportRequest request, ConstraintValidatorContext context) {
        //that's it

        Report report=reportRepository.findByName(request.getReportName())
                .orElseThrow(() -> new BadReportEntryException("reportName", "No Report found by the given name."));

               return report.getLocales()
                .stream()
                .filter(locale -> locale.getValue().toLowerCase().equals(request.getLocale().toLowerCase()))
                .findFirst()
                .orElseThrow(()->new UnsupportedItemException("Locale",request.getLocale(),report.getReportLocalesValues()))!=null;

        //return localeProperties.getLocales().contains(value);
    }
}
