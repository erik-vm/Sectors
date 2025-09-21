package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;

import java.util.List;
import java.util.Set;

public interface UserSubmissionService {

    List<UserSubmission> getUserSubmissions(User user);

    List<UserSubmission> getUserSubmissions(User user, int limit);

    UserSubmission getUserSubmission(User user, Long submissionId);

    UserSubmission getSubmissionById(Long submissionId);

    UserSubmission createSubmission(User user, UserSubmission submission);

    UserSubmission createSubmission(User user, UserSubmission submission, List<Long> sectorIds);

    UserSubmission updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission);

    UserSubmission updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission, List<Long> sectorIds);

    void deleteSubmission(User user, Long submissionId);

    long getUserSubmissionsCount(User user);

    void deactivateSubmission(User user, Long submissionId);

    void activateSubmission(User user, Long submissionId);

    /**
     * Get only the most specific (deepest) selected sectors, excluding parent sectors
     * that have selected children
     */
    Set<Sector> getMostSpecificSelectedSectors(UserSubmission submission);

    /**
     * Handles submission creation with validation - returns view name to render
     */
    String handleSubmissionCreation(UserSubmission submission, BindingResult result,
                                   List<Long> selectedSectors, Authentication authentication, Model model);

    /**
     * Handles submission update with validation - returns view name to render
     */
    String handleSubmissionUpdate(Long id, UserSubmission submission, BindingResult result,
                                 List<Long> selectedSectors, Authentication authentication, Model model);

    /**
     * Toggle active status of a submission for a user
     */
    String handleSubmissionStatusToggle(Long submissionId, Authentication authentication);

    /**
     * Handle viewing submissions - returns view name to render
     */
    String handleViewSubmissions(Model model, Authentication authentication);

    /**
     * Handle viewing single submission - returns view name to render
     */
    String handleViewSubmission(Long id, Model model, Authentication authentication);

    /**
     * Handle new submission form - returns view name to render
     */
    String handleNewSubmissionForm(Model model, Authentication authentication);

    /**
     * Handle edit submission form - returns view name to render
     */
    String handleEditSubmissionForm(Long id, Model model, Authentication authentication);
}
