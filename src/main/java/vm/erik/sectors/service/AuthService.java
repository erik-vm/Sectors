package vm.erik.sectors.service;

import vm.erik.sectors.dto.RegisterForm;

public interface AuthService {


    void registerUser(RegisterForm registerForm);


    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);
}
