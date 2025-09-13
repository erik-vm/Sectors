package vm.erik.sectors.service.impl;

import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.AdminService;
import vm.erik.sectors.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AdminStatsDto getAdminStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByIsActiveTrue();
        long blockedUsers = userRepository.countByIsLockedTrue();
        
        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .blockedUsers(blockedUsers)
                .build();
    }

    @Override
    public List<UserDetailsDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDetailsDto)
                .toList();
    }

    @Override
    public List<UserDetailsDto> searchUsers(String searchTerm) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(this::convertToUserDetailsDto)
                .toList();
    }

    @Override
    public void blockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsLocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsLocked(false);
        userRepository.save(user);
    }

    @Override
    public UserDetailsDto getUserDetails(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserDetailsDto(user);
    }

    private UserDetailsDto convertToUserDetailsDto(User user) {
        return UserDetailsDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getPerson() != null ? user.getPerson().getFirstName() : null)
                .lastName(user.getPerson() != null ? user.getPerson().getLastName() : null)
                .isActive(user.getIsActive())
                .isLocked(user.getIsLocked())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .latestSubmission(user.getLatestSubmission())
                .build();
    }
}
