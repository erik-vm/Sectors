package vm.erik.sectors.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "people")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = {"user"})
@Builder
public class Person extends BaseEntity {


    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @OneToOne(mappedBy = "person")
    private User user;
}
