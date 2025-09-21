package vm.erik.sectors.handler;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.service.UserSubmissionService;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.mapper.UserMapper;
import vm.erik.sectors.exceptions.UserNotFoundException;
import vm.erik.sectors.exceptions.PasswordValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.List;

@Component
public class AdminHandler {

    private final UserSubmissionService userSubmissionService;
    private final SectorService sectorService;
    private final UserRepository userRepository;
    private final UserSubmissionRepository userSubmissionRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public AdminHandler(UserSubmissionService userSubmissionService,
                       SectorService sectorService, UserRepository userRepository,
                       UserSubmissionRepository userSubmissionRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.userSubmissionService = userSubmissionService;
        this.sectorService = sectorService;
        this.userRepository = userRepository;
        this.userSubmissionRepository = userSubmissionRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private void updateUserProfile(User user, String firstName, String lastName, String email) {
        user.getPerson().setFirstName(firstName);
        user.getPerson().setLastName(lastName);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        user.getPerson().setUpdatedAt(LocalDateTime.now());

        personRepository.save(user.getPerson());
        userRepository.save(user);
    }

    private void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordValidationException("Current password is incorrect!");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public String handleAdminDashboard(Model model, Authentication authentication) {
        // Create admin stats directly
        List<User> allUsers = userRepository.findAll();
        long totalUsers = allUsers.size();
        long activeUsers = allUsers.stream().filter(User::getIsActive).count();
        long blockedUsers = allUsers.stream().filter(User::getIsLocked).count();
        long totalSubmissions = userSubmissionRepository.count();

        AdminStatsDto stats = AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .blockedUsers(blockedUsers)
                .totalSubmissions(totalSubmissions)
                .build();
        model.addAttribute("stats", stats);

        User currentAdmin = getCurrentUser(authentication);
        var users = allUsers.stream()
                .filter(user -> !user.getUsername().equals(currentAdmin.getUsername()))
                .map(userMapper::toUserDetailsDTO)
                .toList();
        model.addAttribute("users", users);

        return "admin/dashboard";
    }

    public String handleBlockUser(Long userId, RedirectAttributes redirectAttributes) {
        try {
            blockStatusToggler(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User has been blocked successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to block user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    public String handleUnblockUser(Long userId, RedirectAttributes redirectAttributes) {
        try {
            blockStatusToggler(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User has been unblocked successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to unblock user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    public Object handleGetUserDetails(Long userId) {
        try {
            return getUserDetails(userId);
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private void blockStatusToggler(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        User updatedUser = user.get();
        updatedUser.setIsActive(!updatedUser.getIsActive());
        updatedUser.setIsLocked(!updatedUser.getIsLocked());
        userRepository.save(updatedUser);
    }

    private UserDetailsDto getUserDetails(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        return userMapper.toUserDetailsDTO(user.get());
    }

    public String handleViewUserSubmission(Long submissionId, Model model) {
        try {
            UserSubmission submission = userSubmissionService.getSubmissionById(submissionId);
            if (submission == null) {
                return "redirect:/admin?error=submission-not-found";
            }

            model.addAttribute("submission", submission);
            model.addAttribute("mostSpecificSectors", userSubmissionService.getMostSpecificSelectedSectors(submission));
            model.addAttribute("isAdminView", true);

            return "admin/view-user-submission";
        } catch (Exception e) {
            return "redirect:/admin?error=access-denied";
        }
    }

    public String handleManageSectors(Model model) {
        List<Sector> allSectors = sectorService.getAllSectorsHierarchy();
        model.addAttribute("sectors", allSectors);
        return "admin/sectors";
    }

    public String handleNewSectorForm(Long parentId, Model model) {
        Sector sector = new Sector();

        if (parentId != null) {
            Sector parent = sectorService.getSectorById(parentId);
            if (parent != null && parent.getLevel() < 2) {
                sector.setParent(parent);
                sector.setLevel(parent.getLevel() + 1);
            }
        } else {
            sector.setLevel(0);
        }

        model.addAttribute("sector", sector);
        model.addAttribute("parentSectors", sectorService.getActiveSectorsByMaxLevel(1));
        return "admin/sector-form";
    }

    public String handleEditSectorForm(Long id, Model model) {
        Sector sector = sectorService.getSectorById(id);
        if (sector == null) {
            return "redirect:/admin/sectors?error=notfound";
        }

        model.addAttribute("sector", sector);
        model.addAttribute("parentSectors", sectorService.getActiveSectorsByMaxLevel(1));
        return "admin/sector-form";
    }

    public String handleDeactivateSector(Long id, RedirectAttributes redirectAttributes) {
        try {
            sectorService.deactivateSector(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sector deactivated successfully. Existing user selections are preserved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating sector: " + e.getMessage());
        }
        return "redirect:/admin/sectors";
    }

    public String handleActivateSector(Long id, RedirectAttributes redirectAttributes) {
        try {
            sectorService.activateSector(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sector activated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error activating sector: " + e.getMessage());
        }
        return "redirect:/admin/sectors";
    }

    public String handleViewSector(Long id, Model model) {
        Sector sector = sectorService.getSectorById(id);
        if (sector == null) {
            return "redirect:/admin/sectors?error=notfound";
        }

        model.addAttribute("sector", sector);
        model.addAttribute("usageCount", sectorService.getSectorUsageCount(id));
        model.addAttribute("childrenCount", sector.getChildren().size());
        return "admin/sector-details";
    }

    public String handleViewProfile(Model model, Authentication authentication) {
        User currentAdmin = getCurrentUser(authentication);
        model.addAttribute("user", currentAdmin);
        return "admin/profile";
    }

    public String handleUpdateProfile(String firstName, String lastName, String email,
                                    Authentication authentication, RedirectAttributes redirectAttributes) {
        User currentAdmin = getCurrentUser(authentication);

        try {
            updateUserProfile(currentAdmin, firstName, lastName, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/admin/profile";
    }

    public String handleChangePassword(String currentPassword, String newPassword, String confirmPassword,
                                     Authentication authentication, RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match!");
            return "redirect:/admin/profile";
        }

        User currentAdmin = getCurrentUser(authentication);

        if (currentPassword.equals(newPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password must be different from your current password!");
            return "redirect:/admin/profile";
        }

        try {
            changePassword(currentAdmin, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error changing password: " + e.getMessage());
        }

        return "redirect:/admin/profile";
    }
}