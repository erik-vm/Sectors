package vm.erik.sectors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        return "simple-admin";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(required = false) String search, Model model) {
        // Get admin statistics
        var stats = adminService.getAdminStats();
        model.addAttribute("stats", stats);

        // Get users (with search if provided)
        var users = search != null && !search.trim().isEmpty();

        model.addAttribute("users", users);
        model.addAttribute("searchTerm", search);

        return "simple-admin";
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
}
