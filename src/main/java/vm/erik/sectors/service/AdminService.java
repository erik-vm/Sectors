package vm.erik.sectors.service;

import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    AdminStatsDto getAdminStats();
    
    List<UserDetailsDto> getAllUsers();
    
    List<UserDetailsDto> searchUsers(String searchTerm);
    
    void blockUser(UUID userId);
    
    void unblockUser(UUID userId);
    
    UserDetailsDto getUserDetails(UUID userId);
}
