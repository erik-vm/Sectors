package vm.erik.sectors.service.impl;

import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.service.AdminService;

import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public AdminStatsDto getAdminStats() {
        return null;
    }

    @Override
    public List<UserDetailsDto> getAllUsers() {
        return List.of();
    }

    @Override
    public List<UserDetailsDto> searchUsers(String searchTerm) {
        return List.of();
    }

    @Override
    public void blockUser(UUID userId) {

    }

    @Override
    public void unblockUser(UUID userId) {

    }

    @Override
    public UserDetailsDto getUserDetails(UUID userId) {
        return null;
    }
}
