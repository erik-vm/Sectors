package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.RegisterForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
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
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
