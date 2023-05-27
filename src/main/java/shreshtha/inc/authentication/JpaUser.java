package shreshtha.inc.authentication;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "app_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "emailAddress")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long userId;
    @NotBlank
    private String username;
    private String password;
    @NotBlank
    @Column(length = 65)
    private String firstName;
    @Column(length = 75)
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    @Email
    private String emailAddress;

    @NotBlank
    @Column(length = 10, nullable = false)
    private String companyName;

    @Column(length = 30, nullable = false)
    private String phoneNumber;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    @CreationTimestamp
    @Column(nullable = false)
    private Instant dateCreated;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdated;

}