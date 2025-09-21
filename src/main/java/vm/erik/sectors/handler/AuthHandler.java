package vm.erik.sectors.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.Person;
import vm.erik.sectors.model.Role;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.RoleRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.validation.ValidationService;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class AuthHandler {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    public AuthHandler(UserRepository userRepository, PersonRepository personRepository,
                      RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                      ValidationService validationService) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
    }

    private void registerUser(RegisterForm registerForm) {
        Person person = Person.builder()
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .build();
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person = personRepository.save(person);

        Role userRole = roleRepository.findByRoleName(RoleName.USER);

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

    public String handleUserRegistration(RegisterForm registerForm, BindingResult result, Model model) {
        if (validationService.handleRegistrationValidationErrors(result, model, registerForm)) {
            return "auth/auth";
        }

        registerUser(registerForm);
        return "redirect:/auth?registered";
    }

    public String handleAuthPage(Model model, Authentication authentication) {
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
}