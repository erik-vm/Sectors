package vm.erik.sectors.service;

import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;

import java.util.List;

public interface AdminService {


    AdminStatsDto getAdminStats();

    List<UserDetailsDto> getAllUsers();

    void blockStatusToggler(Long userId);


    UserDetailsDto getUserDetails(Long userId);

    long getTotalSubmissionsCount();
}
