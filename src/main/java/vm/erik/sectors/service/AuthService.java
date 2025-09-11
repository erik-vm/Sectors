package vm.erik.sectors.service;

import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.model.User;

public interface AuthService {


    User registerUser(RegisterForm registerForm);

    void loginAsDemo(String userType);

    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);
}
