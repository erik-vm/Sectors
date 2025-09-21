package vm.erik.sectors.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.handler.UserSubmissionHandler;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.repository.SectorRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.UserSubmissionService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserSubmissionServiceImpl implements UserSubmissionService {

    private final UserSubmissionRepository userSubmissionRepository;
    private final SectorRepository sectorRepository;
    private final UserSubmissionHandler userSubmissionHandler;

    public UserSubmissionServiceImpl(UserSubmissionRepository userSubmissionRepository,
                                     SectorRepository sectorRepository, UserSubmissionHandler userSubmissionHandler) {
        this.userSubmissionRepository = userSubmissionRepository;
        this.sectorRepository = sectorRepository;
        this.userSubmissionHandler = userSubmissionHandler;
    }

    @Override
    public List<UserSubmission> getUserSubmissions(User user) {
        return userSubmissionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<UserSubmission> getUserSubmissions(User user, int limit) {
        return userSubmissionRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, limit));
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
    public UserSubmission createSubmission(User user, UserSubmission submission) {
        submission.setUser(user);
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        submission.setIsActive(true);

        return userSubmissionRepository.save(submission);
    }

    @Override
    public UserSubmission createSubmission(User user, UserSubmission submission, List<Long> sectorIds) {
        log.debug("Creating submission for user: {}, submission name: {}, agreeToTerms: {}, sectorIds: {}",
                user.getUsername(), submission.getName(), submission.getAgreeToTerms(), sectorIds);

        submission.setUser(user);
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        submission.setIsActive(true);

        if (sectorIds != null && !sectorIds.isEmpty()) {
            Set<Sector> sectors = new HashSet<>();
            for (Long sectorId : sectorIds) {
                sectorRepository.findById(sectorId).ifPresent(sectors::add);
            }
            submission.setSelectedSectors(sectors);
            log.debug("Found {} sectors for submission", sectors.size());
        }

        try {
            UserSubmission saved = userSubmissionRepository.save(submission);
            log.debug("Successfully saved submission with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error saving submission: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public UserSubmission updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission) {
        UserSubmission existingSubmission = getUserSubmission(user, submissionId);

        if (existingSubmission == null) {
            throw new EntityNotFoundException("Submission not found or doesn't belong to user");
        }

        existingSubmission.setName(updatedSubmission.getName());
        existingSubmission.setAgreeToTerms(updatedSubmission.getAgreeToTerms());
        existingSubmission.setSelectedSectors(updatedSubmission.getSelectedSectors());
        existingSubmission.setUpdatedAt(LocalDateTime.now());

        return userSubmissionRepository.save(existingSubmission);
    }

    @Override
    public UserSubmission updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission, List<Long> sectorIds) {
        UserSubmission existingSubmission = getUserSubmission(user, submissionId);

        if (existingSubmission == null) {
            throw new EntityNotFoundException("Submission not found or doesn't belong to user");
        }

        existingSubmission.setName(updatedSubmission.getName());
        existingSubmission.setAgreeToTerms(updatedSubmission.getAgreeToTerms());
        existingSubmission.setUpdatedAt(LocalDateTime.now());

        if (sectorIds != null && !sectorIds.isEmpty()) {
            Set<Sector> sectors = new HashSet<>();
            for (Long sectorId : sectorIds) {
                sectorRepository.findById(sectorId).ifPresent(sectors::add);
            }
            existingSubmission.setSelectedSectors(sectors);
        } else {
            existingSubmission.setSelectedSectors(new HashSet<>());
        }

        return userSubmissionRepository.save(existingSubmission);
    }

    @Override
    public void deleteSubmission(User user, Long submissionId) {
        UserSubmission submission = getUserSubmission(user, submissionId);

        if (submission == null) {
            throw new EntityNotFoundException("Submission not found or doesn't belong to user");
        }

        userSubmissionRepository.delete(submission);
    }

    @Override
    public long getUserSubmissionsCount(User user) {
        return userSubmissionRepository.countByUser(user);
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
    public void deactivateSubmission(User user, Long submissionId) {
        UserSubmission submission = getUserSubmission(user, submissionId);
        if (submission == null) {
            throw new RuntimeException("Submission not found or access denied");
        }

        submission.setIsActive(false);
        userSubmissionRepository.save(submission);


    }

    @Override
    public void activateSubmission(User user, Long submissionId) {
        UserSubmission submission = getUserSubmission(user, submissionId);
        if (submission == null) {
            throw new RuntimeException("Submission not found or access denied");
        }

        submission.setIsActive(true);
        userSubmissionRepository.save(submission);
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
