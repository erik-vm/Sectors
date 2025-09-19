package vm.erik.sectors.service;

import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;

import java.util.List;
import java.util.Set;

public interface UserSubmissionService {

    List<UserSubmission> getUserSubmissions(User user);

    List<UserSubmission> getUserSubmissions(User user, int limit);

    UserSubmission getUserSubmission(User user, Long submissionId);

    UserSubmission createSubmission(User user, UserSubmission submission);

    UserSubmission createSubmission(User user, UserSubmission submission, List<Long> sectorIds);

    UserSubmission updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission);

    UserSubmission updateSubmission(User user, Long submissionId, UserSubmission updatedSubmission, List<Long> sectorIds);

    void deleteSubmission(User user, Long submissionId);

    long getUserSubmissionsCount(User user);

    /**
     * Get only the most specific (deepest) selected sectors, excluding parent sectors
     * that have selected children
     */
    Set<Sector> getMostSpecificSelectedSectors(UserSubmission submission);
}
