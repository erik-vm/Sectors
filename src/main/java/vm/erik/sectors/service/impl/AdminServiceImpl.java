package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.handler.AdminHandler;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.AdminService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {


    private final UserSubmissionRepository userSubmissionRepository;
    private final AdminHandler adminHandler;

    public AdminServiceImpl(
            UserSubmissionRepository userSubmissionRepository, AdminHandler adminHandler) {

        this.userSubmissionRepository = userSubmissionRepository;
        this.adminHandler = adminHandler;
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
