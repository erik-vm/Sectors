package vm.erik.sectors.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(ServiceException e, RedirectAttributes redirectAttributes) {
        log.error("Service exception occurred: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/user";
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException e, RedirectAttributes redirectAttributes) {
        log.warn("Validation exception occurred: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/user";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e, RedirectAttributes redirectAttributes) {
        log.warn("User not found: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "User not found");
        return "redirect:/user";
    }

    @ExceptionHandler(SubmissionNotFoundException.class)
    public String handleSubmissionNotFoundException(SubmissionNotFoundException e, RedirectAttributes redirectAttributes) {
        log.warn("Submission not found: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Submission not found");
        return "redirect:/user/submissions";
    }

    @ExceptionHandler(PasswordValidationException.class)
    public String handlePasswordValidationException(PasswordValidationException e, RedirectAttributes redirectAttributes) {
        log.warn("Password validation failed: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/user/profile";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, RedirectAttributes redirectAttributes) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        return "redirect:/user";
    }
}