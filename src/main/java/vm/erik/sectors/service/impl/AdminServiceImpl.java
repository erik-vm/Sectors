package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.mapper.UserMapper;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.repository.UserSubmissionRepository;
import vm.erik.sectors.service.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSubmissionRepository userSubmissionRepository;

    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper, UserSubmissionRepository userSubmissionRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userSubmissionRepository = userSubmissionRepository;
    }

    @Override
    public AdminStatsDto getAdminStats() {
        List<User> users = userRepository.findAll();
        long totalUsers = users.size();
        long activeUsers = users.stream().filter(User::getIsActive).count();
        long blockedUsers = users.stream().filter(User::getIsLocked).count();

        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .blockedUsers(blockedUsers)
                .totalSubmissions(getTotalSubmissionsCount())
                .build();
    }

    @Override
    public List<UserDetailsDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDetailsDto> userDetailsDtos = new ArrayList<>();
        for (User user : users) {
            userDetailsDtos.add(userMapper.toUserDetailsDTO(user));
        }
        return userDetailsDtos;
    }

    @Override
    public void blockStatusToggler(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User updatedUser = user.get();
        updatedUser.setIsActive(!updatedUser.getIsActive());
        updatedUser.setIsLocked(!updatedUser.getIsLocked());
        userRepository.save(updatedUser);
    }


    @Override
    public UserDetailsDto getUserDetails(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userMapper.toUserDetailsDTO(user.get());
    }

    @Override
    public long getTotalSubmissionsCount() {
        return userSubmissionRepository.count();
    }

}
