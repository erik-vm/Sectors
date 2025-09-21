package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.service.AdminService;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.service.UserService;
import vm.erik.sectors.service.UserSubmissionService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final UserSubmissionService userSubmissionService;
    private final SectorService sectorService;

    public AdminController(AdminService adminService, UserService userService,
                          UserSubmissionService userSubmissionService, SectorService sectorService) {
        this.adminService = adminService;
        this.userService = userService;
        this.userSubmissionService = userSubmissionService;
        this.sectorService = sectorService;
    }

    @GetMapping
    public String adminDashboard(Model model, Authentication authentication) {
        // Get admin statistics
        var stats = adminService.getAdminStats();
        model.addAttribute("stats", stats);

        // Get current admin user
        User currentAdmin = userService.getCurrentUser(authentication);

        // Get all users except current admin
        var allUsers = adminService.getAllUsers();
        var users = allUsers.stream()
            .filter(user -> !user.getUsername().equals(currentAdmin.getUsername()))
            .toList();
        model.addAttribute("users", users);

        return "admin/dashboard";
    }

    @PostMapping("/block")
    public String blockUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            adminService.blockStatusToggler(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User has been blocked successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to block user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/unblock")
    public String unblockUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            adminService.blockStatusToggler(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User has been unblocked successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to unblock user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public Object getUserDetails(@PathVariable Long userId) {
        try {
            return adminService.getUserDetails(userId);
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    // Admin view for other users' submissions (read-only)
    @GetMapping("/view-submission/{submissionId}")
    public String viewUserSubmission(@PathVariable Long submissionId, Model model, Authentication authentication) {
        try {
            // Get the submission directly by ID (admin can view any submission)
            UserSubmission submission = userSubmissionService.getSubmissionById(submissionId);

            if (submission == null) {
                return "redirect:/admin?error=submission-not-found";
            }

            model.addAttribute("submission", submission);
            model.addAttribute("mostSpecificSectors", userSubmissionService.getMostSpecificSelectedSectors(submission));
            model.addAttribute("isAdminView", true); // Flag to indicate this is admin viewing user submission

            return "admin/view-user-submission";
        } catch (Exception e) {
            return "redirect:/admin?error=access-denied";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        User currentAdmin = userService.getCurrentUser(authentication);
        model.addAttribute("user", currentAdmin);
        return "admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        User currentAdmin = userService.getCurrentUser(authentication);

        try {
            userService.updateUserProfile(currentAdmin, firstName, lastName, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/admin/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match!");
            return "redirect:/admin/profile";
        }

        User currentAdmin = userService.getCurrentUser(authentication);

        if (currentPassword.equals(newPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password must be different from your current password!");
            return "redirect:/admin/profile";
        }

        try {
            userService.changePassword(currentAdmin, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error changing password: " + e.getMessage());
        }

        return "redirect:/admin/profile";
    }
}
