package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.mapper.UserMapper;
import vm.erik.sectors.model.Person;
import vm.erik.sectors.model.Role;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.RoleRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.AuthService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PersonRepository personRepository,
                          RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
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
        person = personRepository.save(person);

        Role userRole = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

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

        userRepository.save(user);
    }

    @Override
    public void loginAsDemo(String userType) {
        String username = "admin".equals(userType) ? "admin" : "user";
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Demo user not found"));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();

        UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
