package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vm.erik.sectors.dto.AdminStatsDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.mapper.UserMapper;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.UserRepository;
import vm.erik.sectors.service.AdminService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
        User user = userRepository.findById(userId);
        user.setIsActive(!user.getIsActive());
        user.setIsLocked(!user.getIsLocked());
        userRepository.saveUser(user);
    }


    @Override
    public UserDetailsDto getUserDetails(Long userId) {
        User user = userRepository.findById(userId);
        return userMapper.toUserDetailsDTO(user);
    }


}
