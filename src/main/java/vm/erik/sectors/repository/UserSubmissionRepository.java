package vm.erik.sectors.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;

import java.util.List;

@Repository
public interface UserSubmissionRepository extends JpaRepository<UserSubmission, Long> {

    List<UserSubmission> findByUserOrderByCreatedAtDesc(User user);

    List<UserSubmission> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    UserSubmission findByIdAndUser(Long id, User user);

    long countByUser(User user);

}
