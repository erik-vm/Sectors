package vm.erik.sectors.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDto {
    
    private Long totalUsers;
    private Long activeUsers;
    private Long blockedUsers;
    private Long totalAdmins;
}