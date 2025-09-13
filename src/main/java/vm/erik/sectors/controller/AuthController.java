package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.dto.LoginForm;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth")
    public String authPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        }

        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterForm());
        }

        return "auth";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterForm registerForm,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerForm", registerForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerForm", result);
            return "redirect:/auth";
        }

        try {
            authService.registerUser(registerForm);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please login.");
            return "redirect:/auth";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("registerForm", registerForm);
            redirectAttributes.addFlashAttribute("registerError", e.getMessage());
            return "redirect:/auth";
        }
    }

    @PostMapping("/demo-login")
    public String demoLogin(@RequestParam String userType) {
        try {
            if ("admin".equals(userType)) {
                authService.loginAsDemo("admin");
                return "redirect:/admin";
            } else {
                authService.loginAsDemo("user");
                return "redirect:/user";
            }
        } catch (Exception e) {
            return "redirect:/auth?error=Demo login failed";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/auth";
    }
}
