package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.model.User;

public interface UserService {

    User getCurrentUser(Authentication authentication);

    User getUserById(Long userId);

    void updateUserProfile(User user, RegisterForm updateForm);

    User findByUsername(String username);

    User findByEmail(String email);
}
