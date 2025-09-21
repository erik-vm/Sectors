package vm.erik.sectors.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vm.erik.sectors.dto.RegisterForm;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterForm> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegisterForm registerForm, ConstraintValidatorContext context) {
        if (registerForm.getPassword() == null || registerForm.getConfirmPassword() == null) {
            return true;
        }

        return registerForm.getPassword().equals(registerForm.getConfirmPassword());
    }
}