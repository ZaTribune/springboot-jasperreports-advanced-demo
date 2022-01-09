package zatribune.spring.jasperreports.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = LocaleValidator.class)
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidLocale {

    String message() default "Unsupported local.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
