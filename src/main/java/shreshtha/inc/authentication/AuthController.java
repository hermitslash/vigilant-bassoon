package shreshtha.inc.authentication;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shreshtha.inc.common.AppUser;
import shreshtha.inc.common.AppUserDto;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
//@RequestMapping(produces = "application/vnd.shreshtha.inc.auth-v1+json")
class AuthController {
    private final UserServiceImpl userServiceImpl;
    @PostMapping("/api/auth/token")
    public ResponseEntity<AuthenticationResponse> generateTokenForUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userServiceImpl.generateTokenForUser(authenticationRequest));
    }
    
    @GetMapping("/api/auth/token/validate")
    public ResponseEntity<AuthenticationResponse> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
    	return ResponseEntity.ok(userServiceImpl.checkIsValidToken(authorizationHeader));
    }

    @GetMapping("/api/auth/userinfo")
    @PreAuthorize("hasAnyAuthority('SCOPE_TOKEN_READ')")
    public ResponseEntity<AppUserDto> loggedInUserInfo() {
        return ResponseEntity.of(Optional.ofNullable(userServiceImpl.loggedInUser()));
    }

    @PostMapping("/api/customer/register")
    public ResponseEntity<AppUserDto> registerCustomer(@Valid @RequestBody AppUser appUser) {
        return ResponseEntity.of(userServiceImpl.registerAppUser(appUser));
    }

    @PatchMapping("/api/customer/{customerId}/approval")
    public ResponseEntity<AppUserDto> makeCustomerApproval(@PathVariable Long customerId) {
        return ResponseEntity.of(userServiceImpl.approveAppUser(customerId));
    }

}
