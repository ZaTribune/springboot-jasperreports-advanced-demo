package zatribune.spring.jasperreports.validators;

import org.springframework.beans.factory.annotation.Autowired;
import zatribune.spring.jasperreports.config.model.LocaleProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocaleValidator implements ConstraintValidator<ValidLocale,String> {

    @Autowired
    private LocaleProperties localeProperties;

    @Override
    public void initialize(ValidLocale constraintAnnotation) {



    }

//     if (!supportedLocals.contains(language))
//            throw new UnsupportedLanguageException(language, supportedLocals);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //that's it
        return localeProperties.getLocales().contains(value);
    }
}
