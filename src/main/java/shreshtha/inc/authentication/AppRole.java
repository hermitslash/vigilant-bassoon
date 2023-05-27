package shreshtha.inc.authentication;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "app_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppRole implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 65)
    @Getter(AccessLevel.PRIVATE)
    private ERole roleName;

    @Override
    public String getAuthority() {
        return getRoleName().name();
    }

    @Getter
    public enum ERole {
        ROLE_ADMIN, ROLE_USER,
        SCOPE_RS_WRITE, SCOPE_RS_READ,
        SCOPE_RS_UPDATE, SCOPE_RS_DELETE,
        SCOPE_TOKEN_READ,
        SCOPE_DOWNLOAD_REPORT, SCOPE_RS_EOD_READ
    }
}
