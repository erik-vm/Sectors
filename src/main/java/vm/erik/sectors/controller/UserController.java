package vm.erik.sectors.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.service.UserService;
import vm.erik.sectors.service.UserSubmissionService;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSubmissionService userSubmissionService;

    @GetMapping
    public String userDashboard(Model model, Authentication authentication) {
        return userService.handleUserDashboard(model, authentication);
    }

    @GetMapping("/submissions")
    public String viewSubmissions(Model model, Authentication authentication) {
        return userSubmissionService.handleViewSubmissions(model, authentication);
    }

    @GetMapping("/submission/new")
    public String newSubmissionForm(Model model, Authentication authentication) {
        return userSubmissionService.handleNewSubmissionForm(model, authentication);
    }

    @PostMapping("/submission/new")
    public String createSubmission(@Valid @ModelAttribute UserSubmission submission,
                                   BindingResult result,
                                   @RequestParam(required = false) List<Long> selectedSectors,
                                   Authentication authentication,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        return userSubmissionService.handleSubmissionCreation(submission, result, selectedSectors, authentication, model);
    }

    @GetMapping("/submission/{id}")
    public String viewSubmission(@PathVariable Long id, Model model, Authentication authentication) {
        return userSubmissionService.handleViewSubmission(id, model, authentication);
    }

    @GetMapping("/submission/{id}/edit")
    public String editSubmissionForm(@PathVariable Long id, Model model, Authentication authentication) {
        return userSubmissionService.handleEditSubmissionForm(id, model, authentication);
    }

    @PostMapping("/submission/{id}/edit")
    public String updateSubmission(@PathVariable Long id,
                                   @Valid @ModelAttribute UserSubmission submission,
                                   BindingResult result,
                                   @RequestParam(required = false) List<Long> selectedSectors,
                                   Authentication authentication,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        return userSubmissionService.handleSubmissionUpdate(id, submission, result, selectedSectors, authentication, model);
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        return userService.handleViewProfile(model, authentication);
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        return userService.handleUpdateProfile(firstName, lastName, email, authentication, redirectAttributes);
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        return userService.handleChangePassword(currentPassword, newPassword, confirmPassword, authentication, redirectAttributes);
    }

    @PostMapping("/submission/{id}/toggle")
    public String toggleSubmissionStatus(@PathVariable Long id, Authentication authentication) {
        return userSubmissionService.handleSubmissionStatusToggle(id, authentication);
    }
}
