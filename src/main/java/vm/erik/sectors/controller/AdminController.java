package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.service.AdminService;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final SectorService sectorService;

    public AdminController(AdminService adminService, UserService userService, SectorService sectorService) {
        this.adminService = adminService;
        this.userService = userService;
        this.sectorService = sectorService;
    }

    @GetMapping
    public String adminDashboard(Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleAdminDashboard(model, authentication);
    }

    @PostMapping("/block")
    public String blockUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        return adminService.handleBlockUser(userId, redirectAttributes);
    }

    @PostMapping("/unblock")
    public String unblockUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        return adminService.handleUnblockUser(userId, redirectAttributes);
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public Object getUserDetails(@PathVariable Long userId) {
        return adminService.handleGetUserDetails(userId);
    }

    @GetMapping("/view-submission/{submissionId}")
    public String viewUserSubmission(@PathVariable Long submissionId, Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleViewUserSubmission(submissionId, model);
    }

    @GetMapping("/sectors")
    public String manageSectors(Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleManageSectors(model);
    }

    @GetMapping("/sector/new")
    public String newSectorForm(@RequestParam(required = false) Long parentId, Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleNewSectorForm(parentId, model);
    }

    @PostMapping("/sector/new")
    public String createSector(@Valid @ModelAttribute Sector sector,
                               BindingResult result,
                               @RequestParam(required = false) Long parentId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        return sectorService.handleSectorCreation(sector, result, parentId, model);
    }

    @GetMapping("/sector/{id}/edit")
    public String editSectorForm(@PathVariable Long id, Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleEditSectorForm(id, model);
    }

    @PostMapping("/sector/{id}/edit")
    public String updateSector(@PathVariable Long id,
                               @Valid @ModelAttribute Sector sector,
                               BindingResult result,
                               @RequestParam(required = false) Long parentId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        return sectorService.handleSectorUpdate(id, sector, result, parentId, model);
    }

    @PostMapping("/sector/{id}/deactivate")
    public String deactivateSector(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return adminService.handleDeactivateSector(id, redirectAttributes);
    }

    @PostMapping("/sector/{id}/activate")
    public String activateSector(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return adminService.handleActivateSector(id, redirectAttributes);
    }

    @GetMapping("/sector/{id}")
    public String viewSector(@PathVariable Long id, Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleViewSector(id, model);
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        userService.addUserRoleToModel(model, authentication);
        return adminService.handleViewProfile(model, authentication);
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        return adminService.handleUpdateProfile(firstName, lastName, email, authentication, redirectAttributes);
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        return adminService.handleChangePassword(currentPassword, newPassword, confirmPassword, authentication, redirectAttributes);
    }
}
