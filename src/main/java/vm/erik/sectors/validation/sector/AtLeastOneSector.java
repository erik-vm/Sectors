package vm.erik.sectors.validation.sector;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneSectorValidator.class)
@Documented
public @interface AtLeastOneSector {
    String message() default "You must select at least one sector";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}