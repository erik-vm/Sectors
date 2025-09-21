package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import vm.erik.sectors.model.User;

public interface UserService {

    User findByUsername(String username);

    String handleUserDashboard(Model model, Authentication authentication);

    String handleUpdateProfile(String firstName, String lastName, String email, Authentication authentication);

    String handleChangePassword(String currentPassword, String newPassword, String confirmPassword, Authentication authentication);

    String handleViewSettings(Model model, Authentication authentication);

    void addUserRoleToModel(Model model, Authentication authentication);
}
