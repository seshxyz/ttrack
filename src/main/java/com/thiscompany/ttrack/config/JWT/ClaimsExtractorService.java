package com.thiscompany.ttrack.config.JWT;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ClaimsExtractorService {

    private final JwtService jwtService;

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Date extractNotBefore(String token) {
        return extractAllClaims(token).getNotBefore();
    }

    public String extractIssuer(String token) {
        return extractAllClaims(token).getIssuer();
    }

    public Date extractIssuedAt(String token) {
        return extractAllClaims(token).getIssuedAt();
    }

    public boolean areTokenDatesValid(String token) {
        Date currentDate = Date.from(Instant.now());
        Claims claims = extractAllClaims(token);
        if (!claims.getNotBefore().after(currentDate)) {
            return !claims.getExpiration().before(currentDate);
        } return false;
    }

    public Claims extractAllClaims(String token) {
        return jwtService.verifier().decryptAndVerify(token);
    }

    public Claims extractHeader(String token) {
        return jwtService.verifier().decryptAndVerify(token);
    }
}
