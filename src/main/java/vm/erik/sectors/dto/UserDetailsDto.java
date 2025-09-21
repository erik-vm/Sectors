package vm.erik.sectors.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserDetailsDto {

    private Long id;
    private String username;
    private String email;
    private boolean isLocked;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    // Person details
    private String firstName;
    private String lastName;


    private List<SubmissionSummaryDto> submissions;

    // Convenience methods for templates
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }


}