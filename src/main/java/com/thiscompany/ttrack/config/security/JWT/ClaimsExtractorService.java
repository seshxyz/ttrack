package com.thiscompany.ttrack.config.security.JWT;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ClaimsExtractorService {
	
	private final JwtService jwtService;
	public static final ThreadLocal<Claims> threadClaimsHolder = new ThreadLocal<>();
	
	public String extractSubject() {
		return threadClaimsHolder.get().getSubject();
	}
	
	public boolean tokenValidNotBefore() {
		Date currentDate = Date.from(Instant.now());
		return currentDate.after(threadClaimsHolder.get().getNotBefore());
	}
	
	public void extractAllClaims(String token) {
		Claims claims = jwtService.verifier().decryptAndVerify(token);
		threadClaimsHolder.set(claims);
	}
	
}
