package vm.erik.sectors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import vm.erik.sectors.validation.sector.AtLeastOneSector;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) @Builder
@ToString(exclude = {"user", "selectedSectors"})
public class UserSubmission extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Column(name = "agreed_to_terms", nullable = false)
    @NotNull(message = "You must agree to the terms and conditions")
    @Builder.Default
    private Boolean agreeToTerms = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "submission_sectors",
            joinColumns = @JoinColumn(name = "submission_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    @AtLeastOneSector
    @Builder.Default
    private Set<Sector> selectedSectors = new HashSet<>();


}
