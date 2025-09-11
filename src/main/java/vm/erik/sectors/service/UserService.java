package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import vm.erik.sectors.dto.UserUpdateForm;
import vm.erik.sectors.model.User;

import java.util.UUID;

public interface UserService {


    User getCurrentUser(Authentication authentication);

    User getUserById(UUID userId);

    void updateUserProfile(User user, UserUpdateForm updateForm);

    User findByUsername(String username);

    User findByEmail(String email);
}
