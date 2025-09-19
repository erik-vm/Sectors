package vm.erik.sectors.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.User;
import vm.erik.sectors.service.AdminService;
import vm.erik.sectors.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        // Get admin statistics
        var stats = adminService.getAdminStats();
        model.addAttribute("stats", stats);

        // Get all users
        var users = adminService.getAllUsers();
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

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        User currentAdmin = userService.getCurrentUser(authentication);
        model.addAttribute("user", currentAdmin);
        model.addAttribute("totalAdmins", adminService.getTotalAdminCount());
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
