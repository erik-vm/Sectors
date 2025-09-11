package vm.erik.sectors.service.impl;

import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public User registerUser(RegisterForm registerForm) {
        return null;
    }

    @Override
    public void loginAsDemo(String userType) {

    }

    @Override
    public boolean isUsernameTaken(String username) {
        return false;
    }

    @Override
    public boolean isEmailTaken(String email) {
        return false;
    }
}
