package vm.erik.sectors.handler;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.exceptions.UserNotFoundException;
import vm.erik.sectors.exceptions.PasswordValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import vm.erik.sectors.repository.PersonRepository;

import java.time.LocalDateTime;

@Component
public class UserHandler {

    private final UserRepository userRepository;
    private final UserSubmissionRepository userSubmissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public UserHandler(UserRepository userRepository, UserSubmissionRepository userSubmissionRepository,
                      PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.userSubmissionRepository = userSubmissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    public String handleUserDashboard(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        var submissions = userSubmissionRepository.findByUserOrderByCreatedAtDesc(currentUser);
        model.addAttribute("submissions", submissions);
        model.addAttribute("user", currentUser);

        return "user/dashboard";
    }

    public String handleViewProfile(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        model.addAttribute("user", currentUser);
        return "user/profile";
    }

    public String handleUpdateProfile(String firstName, String lastName, String email,
                                    Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        updateUserProfile(currentUser, firstName, lastName, email);
        return "redirect:/user/profile?success=profile-updated";
    }

    public String handleChangePassword(String currentPassword, String newPassword, String confirmPassword,
                                     Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        validatePasswordChange(currentPassword, newPassword, confirmPassword, currentUser);
        changePassword(currentUser, currentPassword, newPassword);
        return "redirect:/user/profile?success=password-changed";
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private void updateUserProfile(User user, String firstName, String lastName, String email) {
        user.getPerson().setFirstName(firstName);
        user.getPerson().setLastName(lastName);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        user.getPerson().setUpdatedAt(LocalDateTime.now());

        personRepository.save(user.getPerson());
        userRepository.save(user);
    }

    private void changePassword(User user, String currentPassword, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
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

    public String handleViewSettings(Model model, Authentication authentication) {
        return handleViewProfile(model, authentication);
    }
}