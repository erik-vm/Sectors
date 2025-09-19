package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping()
    public String authPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        }

        if (!model.containsAttribute("userRegistration")) {
            model.addAttribute("userRegistration", new RegisterForm());
        }

        return "auth/auth";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegistration") RegisterForm registerForm,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistration", registerForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistration", result);
            return "redirect:/auth";
        }

        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegistration", registerForm);
            redirectAttributes.addFlashAttribute("registrationError", "Passwords do not match");
            return "redirect:/auth";
        }

        try {
            authService.registerUser(registerForm);
            return "redirect:/auth?registered";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("userRegistration", registerForm);
            redirectAttributes.addFlashAttribute("registrationError", e.getMessage());
            return "redirect:/auth";
        }
    }


    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/auth";
    }
}
