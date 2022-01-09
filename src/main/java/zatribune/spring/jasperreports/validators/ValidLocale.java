package zatribune.spring.jasperreports.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocaleValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD,ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocale {

    String message() default "Unsupported local.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
