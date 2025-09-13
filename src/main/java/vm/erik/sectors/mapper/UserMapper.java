package vm.erik.sectors.mapper;

import org.springframework.stereotype.Component;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.model.User;

@Component
public class UserMapper {

    public UserDetailsDto toUserDetailsDTO (User user){
        return UserDetailsDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isLocked(user.getIsLocked())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .latestSubmission(user.getLatestSubmission())
                .firstName(user.getPerson().getFirstName())
                .lastName(user.getPerson().getLastName())
                .build();
    }

    public User toUser (UserDetailsDto userDetailsDto){
        User user = User.builder()
                .username(userDetailsDto.getUsername())
                .email(userDetailsDto.getEmail())
                .isLocked(userDetailsDto.isLocked())
                .isActive(userDetailsDto.isActive())
                .lastLogin(userDetailsDto.getLastLogin())
                .build();

        user.getPerson().setFirstName(userDetailsDto.getFirstName());
        user.getPerson().setLastName(userDetailsDto.getLastName());
        user.setCreatedAt(userDetailsDto.getCreatedAt());
        user.setUpdatedAt(userDetailsDto.getUpdatedAt());

        return user;
    }
}
