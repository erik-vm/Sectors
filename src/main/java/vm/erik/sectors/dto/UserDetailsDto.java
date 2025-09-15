package vm.erik.sectors.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.model.UserSubmission;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

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
    
    // Latest submission details
    private UserSubmission latestSubmission;
    
    // Convenience methods for templates
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }
    
    public boolean isProfileComplete() {
        return latestSubmission != null && 
               latestSubmission.getAgreeToTerms() && 
               latestSubmission.getSelectedSectors() != null && 
               !latestSubmission.getSelectedSectors().isEmpty();
    }
    
    public Set<Sector> getSelectedSectors() {
        return latestSubmission != null ? latestSubmission.getSelectedSectors() : Set.of();
    }
    
    public boolean getAgreeToTerms() {
        return latestSubmission != null && latestSubmission.getAgreeToTerms();
    }
}