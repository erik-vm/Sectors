package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.model.User;

public interface UserService {

    User getCurrentUser(Authentication authentication);

    User getUserById(Long userId);

    void updateUserProfile(User user, RegisterForm updateForm);

    void updateUserProfile(User user, String firstName, String lastName, String email);

    void changePassword(User user, String currentPassword, String newPassword);

    User findByUsername(String username);

    User findByEmail(String email);
}
