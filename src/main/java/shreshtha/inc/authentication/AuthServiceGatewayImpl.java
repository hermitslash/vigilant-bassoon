package shreshtha.inc.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import shreshtha.inc.common.AppUser;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
class AuthServiceGatewayImpl implements AuthServiceGateway {
    private final JpaUserRepository jpaUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JpaUser registerCustomerAccount(AppUser appUser) {
        var roles = new HashSet<>(appRoleRepository.findAllByRoleNameIn(List.of(AppRole.ERole.ROLE_USER)));
        appUser.setRoles(roles);
        if (!jpaUserRepository.existsByUsernameAndEmailAddress(appUser.getUsername(), appUser.getEmailAddress())) {
            JpaUser customerAccount = JpaUser.builder().firstName(appUser.getFirstName()).lastName(appUser.getLastName()).phoneNumber(appUser.getPhoneNumber()).companyName(appUser.getCompanyName()).emailAddress(appUser.getEmailAddress()).enabled(false).accountNonExpired(true).accountNonLocked(false).credentialsNonExpired(false).username(appUser.getUsername()).password(passwordEncoder.encode(appUser.getPassword())).authorities(appUser.getRoles()).dateCreated(Instant.now(Clock.system(ZoneId.systemDefault()))).lastUpdated(Instant.now(Clock.system(ZoneId.systemDefault()))).build();
            jpaUserRepository.save(customerAccount);
        }
        return null;
    }

    @Override
    public JpaUser approveCustomerAccount(Long accountId) {
        return jpaUserRepository.save(jpaUserRepository.findById(accountId).map(foundUser -> {
            var allRoles = appRoleRepository.findAllByRoleNameIn(Arrays.asList(AppRole.ERole.SCOPE_RS_READ, AppRole.ERole.SCOPE_RS_WRITE, AppRole.ERole.SCOPE_TOKEN_READ, AppRole.ERole.SCOPE_DOWNLOAD_REPORT));
            foundUser.getAuthorities().addAll(allRoles);
            foundUser.setEnabled(true);
            foundUser.setAccountNonExpired(true);
            foundUser.setAccountNonLocked(true);
            foundUser.setCredentialsNonExpired(true);
            foundUser.setLastUpdated(Instant.now(Clock.system(ZoneId.systemDefault())));
            return foundUser;
        }).orElseThrow());
    }

    @Override
    public Optional<JpaUser> loggedInUser() {
        JpaUser authenticatedUser = (JpaUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authenticatedUser.isEnabled() && authenticatedUser.isAccountNonExpired() && authenticatedUser.isAccountNonLocked() && authenticatedUser.isCredentialsNonExpired()) {
            return Optional.of(authenticatedUser);
        } else return Optional.empty();
    }
}
