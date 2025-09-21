package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import vm.erik.sectors.exceptions.PasswordValidationException;
import vm.erik.sectors.exceptions.UserNotFoundException;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.UserService;
import vm.erik.sectors.handler.UserHandler;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSubmissionRepository userSubmissionRepository;
    private final UserHandler userHandler;

    public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository,
                          PasswordEncoder passwordEncoder, UserSubmissionRepository userSubmissionRepository,
                          UserHandler userHandler) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSubmissionRepository = userSubmissionRepository;
        this.userHandler = userHandler;
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();
        return findByUsername(username);
    }

    @Override
    public void updateUserProfile(User user, String firstName, String lastName, String email) {
        if (firstName != null && user.getPerson() != null) {
            user.getPerson().setFirstName(firstName);
        }
        if (lastName != null && user.getPerson() != null) {
            user.getPerson().setLastName(lastName);
        }
        if (email != null) {
            user.setEmail(email);
        }

        user.setUpdatedAt(LocalDateTime.now());
        if (user.getPerson() != null) {
            personRepository.save(user.getPerson());
        }
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername((username));
    }

    @Override
    public String handleUserDashboard(Model model, Authentication authentication) {
        return userHandler.handleUserDashboard(model, authentication);
    }

    @Override
    public String handleViewProfile(Model model, Authentication authentication) {
        return userHandler.handleViewProfile(model, authentication);
    }

    @Override
    public String handleUpdateProfile(String firstName, String lastName, String email, Authentication authentication) {
        return userHandler.handleUpdateProfile(firstName, lastName, email, authentication);
    }

    @Override
    public String handleChangePassword(String currentPassword, String newPassword, String confirmPassword, Authentication authentication) {
        return userHandler.handleChangePassword(currentPassword, newPassword, confirmPassword, authentication);
    }

    @Override
    public String handleViewSettings(Model model, Authentication authentication) {
        return userHandler.handleViewSettings(model, authentication);
    }

    @Override
    public void addUserRoleToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("userRole", "ADMIN");
            } else {
                model.addAttribute("userRole", "USER");
            }
        } else {
            model.addAttribute("userRole", null);
        }
    }
}
