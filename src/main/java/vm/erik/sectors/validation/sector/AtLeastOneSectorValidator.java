package vm.erik.sectors.validation.sector;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vm.erik.sectors.model.Sector;

import java.util.Set;

public class AtLeastOneSectorValidator implements ConstraintValidator<AtLeastOneSector, Set<Sector>> {

    @Override
    public void initialize(AtLeastOneSector constraintAnnotation) {
    }

    @Override
    public boolean isValid(Set<Sector> sectors, ConstraintValidatorContext context) {
        return sectors != null && !sectors.isEmpty();
    }
}