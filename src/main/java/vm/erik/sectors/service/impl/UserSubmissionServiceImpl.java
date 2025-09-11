package vm.erik.sectors.service.impl;

import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.UserUpdateForm;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;
import vm.erik.sectors.service.UserSubmissionService;

import java.util.List;

@Service
public class UserSubmissionServiceImpl implements UserSubmissionService {
    @Override
    public UserSubmission getLatestSubmissionForUser(User user) {
        return null;
    }

    @Override
    public UserSubmission createOrUpdateSubmission(User user, UserUpdateForm updateForm) {
        return null;
    }

    @Override
    public List<UserSubmission> getSubmissionsForUser(User user) {
        return List.of();
    }

    @Override
    public long countUsersWithCompleteProfiles() {
        return 0;
    }
}
