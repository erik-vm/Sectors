package vm.erik.sectors.repository;

import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.Role;

public interface RoleRepository {

    Role findByRoleName(RoleName roleName);
}