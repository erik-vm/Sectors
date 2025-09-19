package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.service.UserService;
import vm.erik.sectors.service.UserSubmissionService;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSubmissionService userSubmissionService;
    private final SectorService sectorService;

    @GetMapping
    public String userDashboard(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        List<UserSubmission> recentSubmissions = userSubmissionService.getUserSubmissions(currentUser, 5);

        model.addAttribute("user", currentUser);
        model.addAttribute("recentSubmissions", recentSubmissions);
        model.addAttribute("totalSubmissions", userSubmissionService.getUserSubmissionsCount(currentUser));

        return "user/dashboard";
    }

    @GetMapping("/submissions")
    public String viewSubmissions(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        List<UserSubmission> submissions = userSubmissionService.getUserSubmissions(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("submissions", submissions);

        return "user/submissions";
    }

    @GetMapping("/submission/new")
    public String newSubmissionForm(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        UserSubmission submission = new UserSubmission();
        submission.setName(currentUser.getPerson().getFirstName() + " " + currentUser.getPerson().getLastName());

        model.addAttribute("user", currentUser);
        model.addAttribute("submission", submission);
        model.addAttribute("sectors", sectorService.getAllSectorsHierarchy());

        return "user/submission-form";
    }

    @PostMapping("/submission/new")
    public String createSubmission(@ModelAttribute UserSubmission submission,
                                   BindingResult result,
                                   @RequestParam(required = false) List<Long> selectedSectors,
                                   Authentication authentication,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser(authentication);


        // Manual validation
        if (submission.getName() == null || submission.getName().trim().isEmpty()) {
            model.addAttribute("user", currentUser);
            model.addAttribute("submission", submission);
            model.addAttribute("sectors", sectorService.getAllSectorsHierarchy());
            model.addAttribute("errorMessage", "Name is required");
            return "user/submission-form";
        }

        if (submission.getAgreeToTerms() == null || !submission.getAgreeToTerms()) {
            model.addAttribute("user", currentUser);
            model.addAttribute("submission", submission);
            model.addAttribute("sectors", sectorService.getAllSectorsHierarchy());
            model.addAttribute("errorMessage", "You must agree to the terms and conditions");
            return "user/submission-form";
        }

        try {
            userSubmissionService.createSubmission(currentUser, submission, selectedSectors);
            redirectAttributes.addFlashAttribute("successMessage", "Submission created successfully!");
            return "redirect:/user/submissions";
        } catch (Exception e) {
            model.addAttribute("user", currentUser);
            model.addAttribute("submission", submission);
            model.addAttribute("sectors", sectorService.getAllSectorsHierarchy());
            model.addAttribute("errorMessage", "Error creating submission: " + e.getMessage());
            return "user/submission-form";
        }
    }

    @GetMapping("/submission/{id}")
    public String viewSubmission(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        UserSubmission submission = userSubmissionService.getUserSubmission(currentUser, id);

        if (submission == null) {
            return "redirect:/user/submissions?error=notfound";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("submission", submission);
        model.addAttribute("sectorHierarchy", sectorService.getAllSectorsHierarchy());
        model.addAttribute("mostSpecificSectors", userSubmissionService.getMostSpecificSelectedSectors(submission));

        return "user/submission-details";
    }

    @GetMapping("/submission/{id}/edit")
    public String editSubmissionForm(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        UserSubmission submission = userSubmissionService.getUserSubmission(currentUser, id);

        if (submission == null) {
            return "redirect:/user/submissions?error=notfound";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("submission", submission);
        model.addAttribute("sectors", sectorService.getAllSectorsHierarchy());

        return "user/submission-form";
    }

    @PostMapping("/submission/{id}/edit")
    public String updateSubmission(@PathVariable Long id,
                                   @Valid @ModelAttribute UserSubmission submission,
                                   BindingResult result,
                                   @RequestParam(required = false) List<Long> selectedSectors,
                                   Authentication authentication,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser(authentication);

        if (result.hasErrors()) {
            model.addAttribute("user", currentUser);
            model.addAttribute("sectors", sectorService.getAllSectorsHierarchy());
            return "user/submission-form";
        }

        try {
            userSubmissionService.updateSubmission(currentUser, id, submission, selectedSectors);
            redirectAttributes.addFlashAttribute("successMessage", "Submission updated successfully!");
            return "redirect:/user/submission/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating submission: " + e.getMessage());
            return "redirect:/user/submission/" + id + "/edit";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);

        model.addAttribute("user", currentUser);

        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser(authentication);

        try {
            userService.updateUserProfile(currentUser, firstName, lastName, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match!");
            return "redirect:/user/profile";
        }

        User currentUser = userService.getCurrentUser(authentication);

        if (currentPassword.equals(newPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password must be different from your current password!");
            return "redirect:/user/profile";
        }

        try {
            userService.changePassword(currentUser, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error changing password: " + e.getMessage());
        }

        return "redirect:/user/profile";
    }
}
