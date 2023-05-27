package shreshtha.inc.authentication;


import shreshtha.inc.common.AppUser;
import shreshtha.inc.common.AppUserDto;

import java.util.Optional;

interface UserService {

    Optional<AppUserDto> registerAppUser(AppUser appUser);
    Optional<AppUserDto> approveAppUser(Long accountId);
    AuthenticationResponse generateTokenForUser(AuthenticationRequest authReq);
    AuthenticationResponse checkIsValidToken(String token);
    AppUserDto loggedInUser();
}