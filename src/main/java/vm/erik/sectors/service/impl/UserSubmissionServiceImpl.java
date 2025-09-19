package vm.erik.sectors.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.UserSubmissionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSubmissionServiceImpl implements UserSubmissionService {

    private final UserSubmissionRepository userSubmissionRepository;

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
    public UserSubmission createSubmission(User user, UserSubmission submission) {
        submission.setUser(user);
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        submission.setIsActive(true);

        return userSubmissionRepository.save(submission);
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
}
