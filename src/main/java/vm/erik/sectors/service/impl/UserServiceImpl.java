package vm.erik.sectors.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();
        return findByUsername(username);
    }

    @Override
    public User getUserById(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        return user.get();
    }

    @Override
    public void updateUserProfile(User user, RegisterForm updateForm) {
        if (updateForm.getFirstName() != null && user.getPerson() != null) {
            user.getPerson().setFirstName(updateForm.getFirstName());
        }
        if (updateForm.getLastName() != null && user.getPerson() != null) {
            user.getPerson().setLastName(updateForm.getLastName());
        }
        if (updateForm.getEmail() != null) {
            user.setEmail(updateForm.getEmail());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void updateUserProfile(User user, String firstName, String lastName, String email) {
        if (firstName != null && user.getPerson() != null) {
            user.getPerson().setFirstName(firstName);
        }
        if (lastName != null && user.getPerson() != null) {
            user.getPerson().setLastName(lastName);
        }
        if (email != null) {
            user.setEmail(email);
        }

        user.setUpdatedAt(LocalDateTime.now());
        if (user.getPerson() != null) {
            personRepository.save(user.getPerson());
        }
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername((username));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail((email));
    }


}
