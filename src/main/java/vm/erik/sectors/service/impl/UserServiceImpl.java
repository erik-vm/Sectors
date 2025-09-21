package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import vm.erik.sectors.handler.UserHandler;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.UserService;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserHandler userHandler;

    public UserServiceImpl(UserRepository userRepository,
                           UserHandler userHandler) {
        this.userRepository = userRepository;
        this.userHandler = userHandler;
    }


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername((username));
    }

    @Override
    public String handleUserDashboard(Model model, Authentication authentication) {
        return userHandler.handleUserDashboard(model, authentication);
    }

    @Override
    public String handleUpdateProfile(String firstName, String lastName, String email, Authentication authentication) {
        return userHandler.handleUpdateProfile(firstName, lastName, email, authentication);
    }

    @Override
    public String handleChangePassword(String currentPassword, String newPassword, String confirmPassword, Authentication authentication) {
        return userHandler.handleChangePassword(currentPassword, newPassword, confirmPassword, authentication);
    }

    @Override
    public String handleViewSettings(Model model, Authentication authentication) {
        return userHandler.handleViewSettings(model, authentication);
    }

    @Override
    public void addUserRoleToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("userRole", "ADMIN");
            } else {
                model.addAttribute("userRole", "USER");
            }
        } else {
            model.addAttribute("userRole", null);
        }
    }
}
