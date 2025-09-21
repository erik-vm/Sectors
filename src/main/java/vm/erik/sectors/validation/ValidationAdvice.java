package vm.erik.sectors.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
class ValidationAdvice {

    private final ValidationService validationService;


    ValidationAdvice(ValidationService validationService) {
        this.validationService = validationService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/admin/sectors";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model, HttpServletRequest request) {

        if (request.getRequestURI().contains("/auth/register")) {
            ValidationErrors validationErrors = new ValidationErrors();
            validationService.addCustomError(validationErrors, ex.getMessage());
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("userRegistration", request.getAttribute("userRegistration"));
            model.addAttribute("showRegisterTab", true);
            return "auth/auth";
        }

        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error/500";
    }
}