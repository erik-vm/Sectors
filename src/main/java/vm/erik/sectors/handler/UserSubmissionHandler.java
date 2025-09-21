package vm.erik.sectors.handler;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.exceptions.SubmissionNotFoundException;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.repository.SectorRepository;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.validation.ValidationService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserSubmissionHandler {

    private final UserSubmissionRepository userSubmissionRepository;
    private final UserRepository userRepository;
    private final SectorRepository sectorRepository;
    private final SectorService sectorService;
    private final ValidationService validationService;

    public UserSubmissionHandler(UserSubmissionRepository userSubmissionRepository, UserRepository userRepository,
                                 SectorRepository sectorRepository, SectorService sectorService, ValidationService validationService) {
        this.userSubmissionRepository = userSubmissionRepository;
        this.userRepository = userRepository;
        this.sectorRepository = sectorRepository;
        this.sectorService = sectorService;
        this.validationService = validationService;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private void createSubmission(User user, UserSubmission submission, List<Long> selectedSectors) {
        submission.setUser(user);
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        submission.setIsActive(true);

        Set<Sector> sectors = new HashSet<>();
        if (selectedSectors != null) {
            for (Long sectorId : selectedSectors) {
                Sector sector = sectorRepository.findById(sectorId).orElse(null);
                if (sector != null) {
                    sectors.add(sector);
                }
            }
        }
        submission.setSelectedSectors(sectors);
        userSubmissionRepository.save(submission);
    }

    private void updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission, List<Long> selectedSectors) {
        UserSubmission existingSubmission = getUserSubmission(user, submissionId);
        existingSubmission.setName(updatedSubmission.getName());
        existingSubmission.setAgreeToTerms(updatedSubmission.getAgreeToTerms());
        existingSubmission.setUpdatedAt(LocalDateTime.now());

        Set<Sector> sectors = new HashSet<>();
        if (selectedSectors != null) {
            for (Long sectorId : selectedSectors) {
                Sector sector = sectorRepository.findById(sectorId).orElse(null);
                if (sector != null) {
                    sectors.add(sector);
                }
            }
        }
        existingSubmission.setSelectedSectors(sectors);
        userSubmissionRepository.save(existingSubmission);
    }

    private UserSubmission getUserSubmission(User user, Long submissionId) {
        UserSubmission submission = userSubmissionRepository.findByIdAndUser(submissionId, user);
        if (submission == null) {
            throw new SubmissionNotFoundException("Submission not found or access denied");
        }
        return submission;
    }

    private List<UserSubmission> getUserSubmissions(User user) {
        return userSubmissionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    private void deactivateSubmission(User user, Long submissionId) {
        UserSubmission submission = getUserSubmission(user, submissionId);
        submission.setIsActive(false);
        submission.setUpdatedAt(LocalDateTime.now());
        userSubmissionRepository.save(submission);
    }

    private void activateSubmission(User user, Long submissionId) {
        UserSubmission submission = getUserSubmission(user, submissionId);
        submission.setIsActive(true);
        submission.setUpdatedAt(LocalDateTime.now());
        userSubmissionRepository.save(submission);
    }

    private List<Sector> getMostSpecificSelectedSectors(UserSubmission submission) {
        return submission.getSelectedSectors().stream()
                .filter(sector -> sector.getChildren().stream()
                        .noneMatch(child -> submission.getSelectedSectors().contains(child)))
                .toList();
    }

    public String handleSubmissionCreation(UserSubmission submission, BindingResult result,
                                           List<Long> selectedSectors, Authentication authentication, Model model) {

        if (validationService.handleValidationErrors(result, model, submission, "submission")) {
            model.addAttribute("sectors", sectorService.getActiveSectorsHierarchy());
            return "user/submission-form";
        }

        User currentUser = getCurrentUser(authentication);
        createSubmission(currentUser, submission, selectedSectors);
        return "redirect:/user/submissions?success=created";
    }

    public String handleSubmissionUpdate(Long id, UserSubmission submission, BindingResult result,
                                         List<Long> selectedSectors, Authentication authentication, Model model) {

        if (validationService.handleValidationErrors(result, model, submission, "submission")) {
            model.addAttribute("sectors", sectorService.getActiveSectorsHierarchy());
            return "user/submission-form";
        }

        User currentUser = getCurrentUser(authentication);
        updateSubmission(currentUser, id, submission, selectedSectors);
        return "redirect:/user/submissions?success=updated";
    }

    public String handleSubmissionStatusToggle(Long submissionId, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        UserSubmission submission = getUserSubmission(currentUser, submissionId);

        if (submission.getIsActive()) {
            deactivateSubmission(currentUser, submissionId);
            return "redirect:/user/submission/" + submissionId + "?success=deactivated";
        } else {
            activateSubmission(currentUser, submissionId);
            return "redirect:/user/submission/" + submissionId + "?success=activated";
        }
    }

    public String handleViewSubmissions(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        var submissions = getUserSubmissions(currentUser);
        model.addAttribute("submissions", submissions);
        model.addAttribute("user", currentUser);
        return "user/submissions";
    }

    public String handleViewSubmission(Long id, Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        UserSubmission submission = getUserSubmission(currentUser, id);

        model.addAttribute("submission", submission);
        model.addAttribute("mostSpecificSectors", getMostSpecificSelectedSectors(submission));

        return "user/submission-details";
    }

    public String handleNewSubmissionForm(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        UserSubmission submission = new UserSubmission();
        submission.setName(currentUser.getPerson().getFirstName() + " " + currentUser.getPerson().getLastName());

        model.addAttribute("submission", submission);
        model.addAttribute("sectors", sectorService.getActiveSectorsHierarchy());
        return "user/submission-form";
    }

    public String handleEditSubmissionForm(Long id, Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        UserSubmission submission = getUserSubmission(currentUser, id);

        model.addAttribute("submission", submission);
        model.addAttribute("sectors", sectorService.getActiveSectorsHierarchy());
        return "user/submission-form";
    }
}