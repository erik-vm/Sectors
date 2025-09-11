package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByRoleName(RoleName roleName);
    
    boolean existsByRoleName(RoleName roleName);
}