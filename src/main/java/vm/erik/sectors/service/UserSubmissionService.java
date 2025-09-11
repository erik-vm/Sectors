package vm.erik.sectors.service;

import vm.erik.sectors.dto.UserUpdateForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;

import java.util.List;
import java.util.UUID;

public interface UserSubmissionService {

    UserSubmission getLatestSubmissionForUser(User user);

    UserSubmission createOrUpdateSubmission(User user, UserUpdateForm updateForm);

    List<UserSubmission> getSubmissionsForUser(User user);

    long countUsersWithCompleteProfiles();
}
