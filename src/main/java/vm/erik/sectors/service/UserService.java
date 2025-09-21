package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import vm.erik.sectors.model.User;

public interface UserService {

    User getCurrentUser(Authentication authentication);

    void updateUserProfile(User user, String firstName, String lastName, String email);

    void changePassword(User user, String currentPassword, String newPassword);

    User findByUsername(String username);
}
