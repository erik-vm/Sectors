package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.exceptions.PasswordValidationException;
import vm.erik.sectors.exceptions.UserNotFoundException;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.UserService;

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

    public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository,
                          PasswordEncoder passwordEncoder, UserSubmissionRepository userSubmissionRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSubmissionRepository = userSubmissionRepository;
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
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            throw new UserNotFoundException("User not found");
        }

        List<UserSubmission> recentSubmissions = userSubmissionRepository.findByUserOrderByCreatedAtDesc(currentUser, PageRequest.of(0, 5));

        model.addAttribute("user", currentUser);
        model.addAttribute("recentSubmissions", recentSubmissions);
        model.addAttribute("totalSubmissions", userSubmissionRepository.countByUser(currentUser));

        return "user/dashboard";
    }

    @Override
    public String handleViewProfile(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            throw new UserNotFoundException("User not found");
        }

        model.addAttribute("user", currentUser);
        return "user/profile";
    }

    @Override
    public String handleUpdateProfile(String firstName, String lastName, String email,
                                    Authentication authentication, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            throw new UserNotFoundException("User not found");
        }

        try {
            updateUserProfile(currentUser, firstName, lastName, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            log.error("Error updating profile for user {}: {}", currentUser.getUsername(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/user/profile";
    }

    @Override
    public String handleChangePassword(String currentPassword, String newPassword, String confirmPassword,
                                     Authentication authentication, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            throw new UserNotFoundException("User not found");
        }

        try {
            validatePasswordChange(currentPassword, newPassword, confirmPassword, currentUser);
            changePassword(currentUser, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (PasswordValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            log.error("Error changing password for user {}: {}", currentUser.getUsername(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error changing password: " + e.getMessage());
        }

        return "redirect:/user/profile";
    }

    private void validatePasswordChange(String currentPassword, String newPassword, String confirmPassword, User user) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordValidationException("New passwords do not match!");
        }

        if (currentPassword.equals(newPassword)) {
            throw new PasswordValidationException("New password must be different from your current password!");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordValidationException("Current password is incorrect!");
        }
    }
}
