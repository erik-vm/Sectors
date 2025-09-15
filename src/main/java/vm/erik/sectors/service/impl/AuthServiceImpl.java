package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.Person;
import vm.erik.sectors.model.Role;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.RoleRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.AuthService;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PersonRepository personRepository,
                          RoleRepository roleRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public void registerUser(RegisterForm registerForm) {
        if (isUsernameTaken(registerForm.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (isEmailTaken(registerForm.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Person person = Person.builder()
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .build();
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person = personRepository.savePerson(person);

        Role userRole = roleRepository.findByRoleName(RoleName.USER);

        User user = User.builder()
                .username(registerForm.getUsername())
                .email(registerForm.getEmail())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .person(person)
                .roles(Collections.singleton(userRole))
                .isActive(true)
                .isLocked(false)
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.saveUser(user);
    }


    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.isUsernameTaken(username);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.isEmailTaken(email);
    }
}
