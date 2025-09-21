package vm.erik.sectors.validation;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

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


    public boolean handleValidationErrors(BindingResult result, Model model, Object formObject, String formAttributeName) {
        if (result.hasErrors()) {
            var validationErrors = processBindingResult(result);
            model.addAttribute(formAttributeName, formObject);
            model.addAttribute("validationErrors", validationErrors);
            return true;
        }
        return false;
    }


    public boolean handleRegistrationValidationErrors(BindingResult result, Model model, Object formObject) {
        if (result.hasErrors()) {
            var validationErrors = processBindingResult(result);
            model.addAttribute("userRegistration", formObject);
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("showRegisterTab", true);
            return true;
        }
        return false;
    }


    public void addCustomErrorToModel(Model model, String errorMessage) {
        var validationErrors = new ValidationErrors();
        addCustomError(validationErrors, errorMessage);
        model.addAttribute("validationErrors", validationErrors);
    }
}