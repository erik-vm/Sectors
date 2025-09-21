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
        return authService.handleAuthPage(model, authentication);
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegistration") RegisterForm registerForm,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        return authService.handleUserRegistration(registerForm, result, model);
    }


    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/auth";
    }

}
