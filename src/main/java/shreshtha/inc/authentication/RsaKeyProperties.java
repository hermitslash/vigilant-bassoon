package shreshtha.inc.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey){}
