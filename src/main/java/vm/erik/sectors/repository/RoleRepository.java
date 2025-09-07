package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
