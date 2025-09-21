package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.mapper.UserMapper;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.AdminService;
import vm.erik.sectors.handler.AdminHandler;
import vm.erik.sectors.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSubmissionRepository userSubmissionRepository;
    private final AdminHandler adminHandler;

    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           UserSubmissionRepository userSubmissionRepository, AdminHandler adminHandler) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userSubmissionRepository = userSubmissionRepository;
        this.adminHandler = adminHandler;
    }

    @Override
    public AdminStatsDto getAdminStats() {
        List<User> users = userRepository.findAll();
        long totalUsers = users.size();
        long activeUsers = users.stream().filter(User::getIsActive).count();
        long blockedUsers = users.stream().filter(User::getIsLocked).count();

        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .blockedUsers(blockedUsers)
                .totalSubmissions(getTotalSubmissionsCount())
                .build();
    }

    @Override
    public List<UserDetailsDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDetailsDto> userDetailsDtos = new ArrayList<>();
        for (User user : users) {
            userDetailsDtos.add(userMapper.toUserDetailsDTO(user));
        }
        return userDetailsDtos;
    }

    @Override
    public void blockStatusToggler(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        User updatedUser = user.get();
        updatedUser.setIsActive(!updatedUser.getIsActive());
        updatedUser.setIsLocked(!updatedUser.getIsLocked());
        userRepository.save(updatedUser);
    }

    @Override
    public UserDetailsDto getUserDetails(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        return userMapper.toUserDetailsDTO(user.get());
    }

    @Override
    public long getTotalSubmissionsCount() {
        return userSubmissionRepository.count();
    }

    @Override
    public String handleAdminDashboard(Model model, Authentication authentication) {
        return adminHandler.handleAdminDashboard(model, authentication);
    }

    @Override
    public String handleBlockUser(Long userId, RedirectAttributes redirectAttributes) {
        return adminHandler.handleBlockUser(userId, redirectAttributes);
    }

    @Override
    public String handleUnblockUser(Long userId, RedirectAttributes redirectAttributes) {
        return adminHandler.handleUnblockUser(userId, redirectAttributes);
    }

    @Override
    public Object handleGetUserDetails(Long userId) {
        return adminHandler.handleGetUserDetails(userId);
    }

    @Override
    public String handleViewUserSubmission(Long submissionId, Model model) {
        return adminHandler.handleViewUserSubmission(submissionId, model);
    }

    @Override
    public String handleManageSectors(Model model) {
        return adminHandler.handleManageSectors(model);
    }

    @Override
    public String handleNewSectorForm(Long parentId, Model model) {
        return adminHandler.handleNewSectorForm(parentId, model);
    }

    @Override
    public String handleEditSectorForm(Long id, Model model) {
        return adminHandler.handleEditSectorForm(id, model);
    }

    @Override
    public String handleDeactivateSector(Long id, RedirectAttributes redirectAttributes) {
        return adminHandler.handleDeactivateSector(id, redirectAttributes);
    }

    @Override
    public String handleActivateSector(Long id, RedirectAttributes redirectAttributes) {
        return adminHandler.handleActivateSector(id, redirectAttributes);
    }

    @Override
    public String handleViewSector(Long id, Model model) {
        return adminHandler.handleViewSector(id, model);
    }

    @Override
    public String handleViewProfile(Model model, Authentication authentication) {
        return adminHandler.handleViewProfile(model, authentication);
    }

    @Override
    public String handleUpdateProfile(String firstName, String lastName, String email,
                                    Authentication authentication, RedirectAttributes redirectAttributes) {
        return adminHandler.handleUpdateProfile(firstName, lastName, email, authentication, redirectAttributes);
    }

    @Override
    public String handleChangePassword(String currentPassword, String newPassword, String confirmPassword,
                                     Authentication authentication, RedirectAttributes redirectAttributes) {
        return adminHandler.handleChangePassword(currentPassword, newPassword, confirmPassword, authentication, redirectAttributes);
    }
}
