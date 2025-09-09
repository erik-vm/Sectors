package vm.erik.sectors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "sectors",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "parent_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = {"parent", "children", "userSubmissions"})
@Builder
public class Sector  extends BaseEntity{


    @Column(name = "name", nullable = false, length = 255)
    @NotBlank(message = "Sector name is required")
    @Size(max = 255, message = "Sector name must not exceed 255 characters")
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "description", length = 500)
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Column(name = "level", nullable = false)
    @Min(value = 0, message = "Level must be non-negative")
    private Integer level;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Sector parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC, name ASC")
    @Builder.Default
    private List<Sector> children = new ArrayList<>();

    @ManyToMany(mappedBy = "selectedSectors", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserSubmission> userSubmissions = new HashSet<>();
}
