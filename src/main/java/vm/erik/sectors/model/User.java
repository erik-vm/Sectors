package vm.erik.sectors.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder
@ToString(exclude = {"submissions", "password"})
public class User extends BaseEntity implements UserDetails {


    @Column(name = "username", nullable = false, unique = true, length = 20)
    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username length must be between 5-20 characters")
    private String username;


    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters")
    @JsonIgnore
    private String password;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    private Boolean isLocked = false;

    @Column(name = "is_expired", nullable = false)
    @Builder.Default
    private Boolean isExpired = false;

    @Column(name = "credentials_expired", nullable = false)
    @Builder.Default
    private Boolean credentialsExpired = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserSubmission> submissions = new HashSet<>();

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().getDisplayName().toUpperCase()))
                .collect(Collectors.toList());
    }


    public UserSubmission getLatestSubmission() {
        UserSubmission[] userSubmissions = submissions.toArray(new UserSubmission[0]);
        return userSubmissions.length > 0 ? userSubmissions[userSubmissions.length - 1] : null;
    }
}