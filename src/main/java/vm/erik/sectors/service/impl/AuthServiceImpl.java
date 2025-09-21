package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.handler.AuthHandler;
import vm.erik.sectors.model.Person;
import vm.erik.sectors.model.Role;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.AuthService;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthHandler authHandler;

    public AuthServiceImpl(UserRepository userRepository, AuthHandler authHandler) {
        this.userRepository = userRepository;
        this.authHandler = authHandler;
    }


    public static void buildUser(RegisterForm registerForm, Person person, Role userRole, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        User user = User.builder()
                .username(registerForm.getUsername())
                .email(registerForm.getEmail())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .person(person)
                .roles(Collections.singleton(userRole))
                .isActive(true)
                .isLocked(false)
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }


    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsUserByUsername((username));
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsUserByEmail((email));
    }

    @Override
    public String handleUserRegistration(RegisterForm registerForm, BindingResult result, Model model) {
        return authHandler.handleUserRegistration(registerForm, result, model);
    }

    @Override
    public String handleAuthPage(Model model, Authentication authentication) {
        return authHandler.handleAuthPage(model, authentication);
    }
}
