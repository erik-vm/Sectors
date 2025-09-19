package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    User findByUsername(String username);

    User findByEmail(String email);

    long countByRolesRoleName(RoleName roleName);
}
