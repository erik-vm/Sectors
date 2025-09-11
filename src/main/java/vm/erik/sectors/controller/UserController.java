package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.dto.UserUpdateForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.service.UserService;
import vm.erik.sectors.service.UserSubmissionService;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SectorService sectorService;
    private final UserSubmissionService userSubmissionService;

    @GetMapping
    public String userDashboard(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        
        // Get current user submission (latest one)
        var currentSubmission = userSubmissionService.getLatestSubmissionForUser(currentUser);
        
        // Add data to model
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentSubmission", currentSubmission);
        model.addAttribute("sectors", sectorService.getAllActiveSectors());
        
        // Create form with current data
        if (!model.containsAttribute("userForm")) {
            UserUpdateForm form = new UserUpdateForm();
            if (currentUser.getPerson() != null) {
                form.setFirstName(currentUser.getPerson().getFirstName());
                form.setLastName(currentUser.getPerson().getLastName());
            }
            if (currentSubmission != null) {
                form.setAgreeToTerms(currentSubmission.getAgreeToTerms());
                form.setSelectedSectors(currentSubmission.getSelectedSectors().stream()
                        .map(sector -> sector.getId()).toList());
            }
            model.addAttribute("userForm", form);
        }

        return "user-dashboard";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute UserUpdateForm userForm,
                              BindingResult result,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("userForm", userForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userForm", result);
            redirectAttributes.addFlashAttribute("errorMessage", "Please correct the errors below.");
            return "redirect:/user";
        }

        try {
            User currentUser = userService.getCurrentUser(authentication);
            userService.updateUserProfile(currentUser, userForm);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("userForm", userForm);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/user";
    }
}
