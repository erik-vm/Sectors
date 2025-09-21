package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.dto.RegisterForm;

public interface AuthService {


    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);

    String handleUserRegistration(RegisterForm registerForm, BindingResult result, Model model);


    String handleAuthPage(Model model, Authentication authentication);
}
