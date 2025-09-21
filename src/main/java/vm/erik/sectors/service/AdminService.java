package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;

import java.util.List;

public interface AdminService {



    long getTotalSubmissionsCount();

    String handleAdminDashboard(Model model, Authentication authentication);

    String handleBlockUser(Long userId, RedirectAttributes redirectAttributes);

    String handleUnblockUser(Long userId, RedirectAttributes redirectAttributes);

    Object handleGetUserDetails(Long userId);

    String handleViewUserSubmission(Long submissionId, Model model);

    String handleManageSectors(Model model);

    String handleNewSectorForm(Long parentId, Model model);

    String handleEditSectorForm(Long id, Model model);

    String handleDeactivateSector(Long id, RedirectAttributes redirectAttributes);

    String handleActivateSector(Long id, RedirectAttributes redirectAttributes);

    String handleViewSector(Long id, Model model);

    String handleViewProfile(Model model, Authentication authentication);

    String handleUpdateProfile(String firstName, String lastName, String email, Authentication authentication, RedirectAttributes redirectAttributes);

    String handleChangePassword(String currentPassword, String newPassword, String confirmPassword, Authentication authentication, RedirectAttributes redirectAttributes);
}
