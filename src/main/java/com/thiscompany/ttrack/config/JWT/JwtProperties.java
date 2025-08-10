package com.thiscompany.ttrack.config.JWT;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(value = "app.jwt")
public record JwtProperties(

        @Value("${app.jwt.secret-key}")
        String secretKey,

        @Value("${app.jwt.encrypt-key}")
        String encryptKey,

        @DurationUnit(ChronoUnit.MINUTES)
        @Value("${app.jwt.token-lifetime}")
        Duration tokenLifetime,

        @Value("${app.jwt.issuer}")
        String issuer

) {

    public SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public SecretKey getEncryptionKey() {
        byte[] keyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
