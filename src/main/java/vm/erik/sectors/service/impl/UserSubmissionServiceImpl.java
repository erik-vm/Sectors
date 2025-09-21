package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.handler.UserSubmissionHandler;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.UserSubmissionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserSubmissionServiceImpl implements UserSubmissionService {

    private final UserSubmissionRepository userSubmissionRepository;
    private final UserSubmissionHandler userSubmissionHandler;

    public UserSubmissionServiceImpl(UserSubmissionRepository userSubmissionRepository, UserSubmissionHandler userSubmissionHandler) {
        this.userSubmissionRepository = userSubmissionRepository;
        this.userSubmissionHandler = userSubmissionHandler;
    }


    @Override
    public UserSubmission getUserSubmission(User user, Long submissionId) {
        return userSubmissionRepository.findByIdAndUser(submissionId, user);
    }

    @Override
    public UserSubmission getSubmissionById(Long submissionId) {
        return userSubmissionRepository.findById(submissionId).orElse(null);
    }


    @Override
    public Set<Sector> getMostSpecificSelectedSectors(UserSubmission submission) {
        Set<Sector> selectedSectors = submission.getSelectedSectors();
        if (selectedSectors == null || selectedSectors.isEmpty()) {
            return new HashSet<>();
        }

        Set<Sector> mostSpecific = new HashSet<>();

        for (Sector sector : selectedSectors) {
            boolean hasSelectedChild = false;

            // Check if this sector has any selected children
            for (Sector potentialChild : selectedSectors) {
                if (isChildOf(potentialChild, sector)) {
                    hasSelectedChild = true;
                    break;
                }
            }

            // If this sector has no selected children, it's a most specific selection
            if (!hasSelectedChild) {
                mostSpecific.add(sector);
            }
        }

        return mostSpecific;
    }

    private boolean isChildOf(Sector potentialChild, Sector potentialParent) {
        Sector parent = potentialChild.getParent();
        while (parent != null) {
            if (parent.getId().equals(potentialParent.getId())) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }


    @Override
    public String handleSubmissionCreation(UserSubmission submission, BindingResult result,
                                           List<Long> selectedSectors, Authentication authentication, Model model) {
        return userSubmissionHandler.handleSubmissionCreation(submission, result, selectedSectors, authentication, model);
    }

    @Override
    public String handleSubmissionUpdate(Long id, UserSubmission submission, BindingResult result,
                                         List<Long> selectedSectors, Authentication authentication, Model model) {
        return userSubmissionHandler.handleSubmissionUpdate(id, submission, result, selectedSectors, authentication, model);
    }

    @Override
    public String handleSubmissionStatusToggle(Long submissionId, Authentication authentication) {
        return userSubmissionHandler.handleSubmissionStatusToggle(submissionId, authentication);
    }

    @Override
    public String handleViewSubmissions(Model model, Authentication authentication) {
        return userSubmissionHandler.handleViewSubmissions(model, authentication);
    }

    @Override
    public String handleViewSubmission(Long id, Model model, Authentication authentication) {
        return userSubmissionHandler.handleViewSubmission(id, model, authentication);
    }

    @Override
    public String handleNewSubmissionForm(Model model, Authentication authentication) {
        return userSubmissionHandler.handleNewSubmissionForm(model, authentication);
    }

    @Override
    public String handleEditSubmissionForm(Long id, Model model, Authentication authentication) {
        return userSubmissionHandler.handleEditSubmissionForm(id, model, authentication);
    }
}
