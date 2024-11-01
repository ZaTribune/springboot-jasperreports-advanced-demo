package com.tribune.demo.reporting.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.tribune.demo.reporting.db.entities.Report;
import com.tribune.demo.reporting.db.repositories.ReportRepository;
import com.tribune.demo.reporting.error.BadReportEntryException;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportRequest;



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
                .filter(locale -> locale.getContent().equalsIgnoreCase(request.getLocale()))
                .findFirst()
                .orElseThrow(()->new UnsupportedItemException("Locale",request.getLocale(),report.getReportLocalesValues()))!=null;
    }
}
