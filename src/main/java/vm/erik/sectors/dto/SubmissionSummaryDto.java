package vm.erik.sectors.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SubmissionSummaryDto {

    private Long id;
    private String name;
    private Boolean agreeToTerms;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer selectedSectorsCount;
    private Set<String> selectedSectorNames;
}