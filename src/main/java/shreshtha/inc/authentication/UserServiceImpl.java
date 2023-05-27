package shreshtha.inc.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shreshtha.inc.common.AppUser;
import shreshtha.inc.common.AppUserDto;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class UserServiceImpl implements UserService {
    private final AuthServiceGateway authServiceGateway;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;


    @Override
    public AppUserDto loggedInUser() {
        Optional<JpaUser> foundUser = authServiceGateway.loggedInUser();
        if (foundUser.isPresent()) {
            return foundUser.map(au -> new AppUserDto(au.getUserId(), au.getUsername(),
                    au.getFirstName(), au.getLastName(), au.getEmailAddress(),
                    au.getPhoneNumber(), au.getCompanyName(), au.getAuthorities().stream().map(AppRole::getAuthority).collect(Collectors.joining(","))
            )).get();
        }
        return null;
    }

    @Override
    public AuthenticationResponse generateTokenForUser(AuthenticationRequest authReq) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.username(), authReq.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtils.generateToken(authentication);
        log.debug("Token granted: {}", token);
        return new AuthenticationResponse(token, true);
    }

    @Override
    public Optional<AppUserDto> registerAppUser(AppUser appUser) {
        Optional<JpaUser> jpaUser = Optional.ofNullable(authServiceGateway.registerCustomerAccount(appUser));
        return jpaUser.map(this::fromJpaUserToAppUserDto);
    }

    @Override
    public Optional<AppUserDto> approveAppUser(Long accountId) {
        Optional<JpaUser> jpaUser = Optional.ofNullable(authServiceGateway.approveCustomerAccount(accountId));
        return jpaUser.map(this::fromJpaUserToAppUserDto);
    }

    @Override
    public AuthenticationResponse checkIsValidToken(String token) {
        return new AuthenticationResponse(token, tokenUtils.isValidToken(token));
    }

    private AppUserDto fromJpaUserToAppUserDto(JpaUser au) {
        return new AppUserDto(au.getUserId(), au.getUsername(),
                au.getFirstName(), au.getLastName(), au.getEmailAddress(),
                au.getPhoneNumber(), au.getCompanyName(), au.getAuthorities().stream().map(AppRole::getAuthority).collect(Collectors.joining(",")));
    }
}