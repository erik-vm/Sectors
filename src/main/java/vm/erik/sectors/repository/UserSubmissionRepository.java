package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.UserSubmission;

@Repository
public interface UserSubmissionRepository extends JpaRepository<UserSubmission, Long> {
}
