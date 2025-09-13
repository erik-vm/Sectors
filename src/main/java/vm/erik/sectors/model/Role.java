package vm.erik.sectors.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vm.erik.sectors.enums.RoleName;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true,  callSuper = true)
@ToString(exclude = "users")
@Builder
public class Role extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true, length = 20)
    @NotNull(message = "Role name cannot be null")
    @EqualsAndHashCode.Include
    private RoleName roleName;

    @Column(name = "description", length = 255)
    private String description;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    public RoleName getName() {
        return roleName;
    }
}
