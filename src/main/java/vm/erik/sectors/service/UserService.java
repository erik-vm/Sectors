package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import vm.erik.sectors.model.User;

public interface UserService {

    User getCurrentUser(Authentication authentication);

    void updateUserProfile(User user, String firstName, String lastName, String email);

    void changePassword(User user, String currentPassword, String newPassword);

    User findByUsername(String username);

    String handleUserDashboard(Model model, Authentication authentication);

    String handleViewProfile(Model model, Authentication authentication);

    String handleUpdateProfile(String firstName, String lastName, String email, Authentication authentication);

    String handleChangePassword(String currentPassword, String newPassword, String confirmPassword, Authentication authentication);

    String handleViewSettings(Model model, Authentication authentication);

    /**
     * Add user role to model based on authentication
     */
    void addUserRoleToModel(Model model, Authentication authentication);
}
