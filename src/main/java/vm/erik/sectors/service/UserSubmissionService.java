package vm.erik.sectors.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;

import java.util.List;
import java.util.Set;

public interface UserSubmissionService {


    UserSubmission getUserSubmission(User user, Long submissionId);

    UserSubmission getSubmissionById(Long submissionId);

    Set<Sector> getMostSpecificSelectedSectors(UserSubmission submission);

    String handleSubmissionCreation(UserSubmission submission, BindingResult result,
                                    List<Long> selectedSectors, Authentication authentication, Model model);

    String handleSubmissionUpdate(Long id, UserSubmission submission, BindingResult result,
                                  List<Long> selectedSectors, Authentication authentication, Model model);

    String handleSubmissionStatusToggle(Long submissionId, Authentication authentication);

    String handleViewSubmissions(Model model, Authentication authentication);

    String handleViewSubmission(Long id, Model model, Authentication authentication);

    String handleNewSubmissionForm(Model model, Authentication authentication);

    String handleEditSubmissionForm(Long id, Model model, Authentication authentication);
}
