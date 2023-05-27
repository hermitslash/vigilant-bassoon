package shreshtha.inc.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final RsaKeyProperties rsaKeyProperties;
    private final JwtEncoder jwtEncoder;
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(25, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private JWTVerifier buildJWTVerifier() throws CertificateException {
        var algo = Algorithm.RSA256(rsaKeyProperties.publicKey(), rsaKeyProperties.privateKey());
        return JWT.require(algo).build();
    }

    @SneakyThrows
    public String getUsernameFromToken(String token) {
        var verifiedToken = buildJWTVerifier().verify(token.replace("Bearer ", ""));
        return verifiedToken.getClaims().get("sub").asString();
    }

    public Boolean isValidToken(String token) {
        try {
            buildJWTVerifier().verify(token.replace("Bearer ", ""));
            // if token is valid no exception will be thrown
            log.debug("Valid TOKEN");
            return Boolean.TRUE;
        } catch (CertificateException e) {
            //if CertificateException comes from buildJWTVerifier()
            log.error("Invalid TOKEN");
            return Boolean.FALSE;
        } catch (JWTVerificationException e) {
            // if JWT Token in invalid
            log.error("Invalid TOKEN");
            return Boolean.FALSE;
        } catch (Exception e) {
            // If any other exception comes
            log.error("Invalid TOKEN");
            return Boolean.FALSE;
        }
    }
}
