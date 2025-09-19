package vm.erik.sectors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder
@ToString(exclude = {"user", "selectedSectors"})
public class UserSubmission extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "agreed_to_terms", nullable = false)
    @NotNull(message = "Agreement to terms is required")
    @Builder.Default
    private Boolean agreeToTerms = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "submission_sectors",
            joinColumns = @JoinColumn(name = "submission_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    @Builder.Default
    private Set<Sector> selectedSectors = new HashSet<>();


}
