package shreshtha.inc.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shreshtha.inc.authentication.AppRole;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String emailAddress;
    private String phoneNumber;
    private String companyName;
    private Set<AppRole> roles;
}