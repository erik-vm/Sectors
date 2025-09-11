package vm.erik.sectors.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.UserUpdateForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.service.UserService;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getCurrentUser(Authentication authentication) {
        return null;
    }

    @Override
    public User getUserById(UUID userId) {
        return null;
    }

    @Override
    public void updateUserProfile(User user, UserUpdateForm updateForm) {

    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }
}
