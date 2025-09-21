package vm.erik.sectors.mapper;

import org.springframework.stereotype.Component;
import vm.erik.sectors.dto.SubmissionSummaryDto;
import vm.erik.sectors.dto.UserDetailsDto;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.User;
import vm.erik.sectors.model.UserSubmission;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDetailsDto toUserDetailsDTO (User user){
        // Map all submissions
        List<SubmissionSummaryDto> submissions = user.getSubmissions() != null ?
                user.getSubmissions().stream()
                        .map(this::toSubmissionSummaryDto)
                        .collect(Collectors.toList()) : List.of();



        return UserDetailsDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isLocked(user.getIsLocked())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .submissions(submissions)
                .firstName(user.getPerson() != null ? user.getPerson().getFirstName() : null)
                .lastName(user.getPerson() != null ? user.getPerson().getLastName() : null)
                .build();
    }

    private SubmissionSummaryDto toSubmissionSummaryDto(UserSubmission submission) {
        if (submission == null) {
            return null;
        }

        Set<String> sectorNames = submission.getSelectedSectors() != null ?
                submission.getSelectedSectors().stream()
                        .map(Sector::getName)
                        .collect(Collectors.toSet()) : Set.of();

        return SubmissionSummaryDto.builder()
                .id(submission.getId())
                .name(submission.getName())
                .agreeToTerms(submission.getAgreeToTerms())
                .isActive(submission.getIsActive())
                .createdAt(submission.getCreatedAt())
                .updatedAt(submission.getUpdatedAt())
                .selectedSectorsCount(submission.getSelectedSectors() != null ? submission.getSelectedSectors().size() : 0)
                .selectedSectorNames(sectorNames)
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
