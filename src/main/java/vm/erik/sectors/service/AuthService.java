package vm.erik.sectors.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.dto.RegisterForm;

public interface AuthService {

    void registerUser(RegisterForm registerForm);

    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);

    /**
     * Handles user registration with validation - returns view name to render
     */
    String handleUserRegistration(RegisterForm registerForm, BindingResult result, Model model);
}
