package vm.erik.sectors.validation;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class ValidationService {

    public ValidationErrors processBindingResult(BindingResult bindingResult) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (bindingResult != null && bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(validationErrors::addFieldError);
            bindingResult.getGlobalErrors().forEach(error ->
                validationErrors.addErrorMessage(error.getDefaultMessage()));
        }

        return validationErrors;
    }

    public void addCustomError(ValidationErrors errors, String message) {
        errors.addErrorMessage(message);
    }

    /**
     * Handles validation errors for forms that stay on the same page (no redirect)
     * Returns true if there are validation errors, false otherwise
     */
    public boolean handleValidationErrors(BindingResult result, Model model, Object formObject, String formAttributeName) {
        if (result.hasErrors()) {
            var validationErrors = processBindingResult(result);
            model.addAttribute(formAttributeName, formObject);
            model.addAttribute("validationErrors", validationErrors);
            return true; // Has errors
        }
        return false; // No errors
    }

    /**
     * Handles validation errors for registration specifically (sets register tab flag)
     * Returns true if there are validation errors, false otherwise
     */
    public boolean handleRegistrationValidationErrors(BindingResult result, Model model, Object formObject) {
        if (result.hasErrors()) {
            var validationErrors = processBindingResult(result);
            model.addAttribute("userRegistration", formObject);
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("showRegisterTab", true);
            return true; // Has errors
        }
        return false; // No errors
    }

    /**
     * Add custom validation error to existing model
     */
    public void addCustomErrorToModel(Model model, String errorMessage) {
        var validationErrors = new ValidationErrors();
        addCustomError(validationErrors, errorMessage);
        model.addAttribute("validationErrors", validationErrors);
    }
}