package com.thiscompany.ttrack.config.security.JWT;

import com.thiscompany.ttrack.exceptions.empty.JwtEmptySubjectException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {
	
	private final JwtProperties jwtProps;
	
	public Generator generator() {
		return new Generator();
	}
	
	public JwtVerifier verifier() {
		return new JwtVerifier();
	}
	
	public class Generator {
		
		private final Map<String, Object> claims = new HashMap<>();
		
		private String subject;
		
		public Generator claims(String key, Object value) {
			if (key != null && value != null) this.claims.put(key, value);
			return this;
		}
		
		public Generator subject(@NotNull String subject) {
			Optional.of(subject)
					.ifPresentOrElse(inputSubject -> this.subject = inputSubject, JwtEmptySubjectException::new);
			this.subject = subject;
			return this;
		}
		
		public String generateAndEncrypt(Date issuedAt) {
			Date expiresAt = Date.from(issuedAt.toInstant().plus(jwtProps.tokenLifetime()));
			
			String buildedToken = Jwts.builder()
									  .claims(claims)
									  .subject(subject)
									  .issuer(jwtProps.issuer())
									  .issuedAt(issuedAt)
									  .notBefore(issuedAt)
									  .expiration(expiresAt)
									  .signWith(jwtProps.getSigningKey(), Jwts.SIG.HS512)
									  .compressWith(Jwts.ZIP.GZIP)
									  .compact();
			
			return Jwts.builder()
					   .content(buildedToken)
					   .encryptWith(jwtProps.getEncryptionKey(), Jwts.ENC.A256GCM)
					   .compact();
		}
		
	}
	
	public class JwtVerifier {
		
		public Claims decryptAndVerify(String token) {
			String decrypted = new String(
				Jwts.parser()
					.decryptWith(jwtProps.getEncryptionKey())
					.build()
					.parseEncryptedContent(token)
					.getPayload(),
				StandardCharsets.UTF_8
			);
			
			return Jwts.parser()
					   .verifyWith(jwtProps.getSigningKey())
					   .build()
					   .parseSignedClaims(decrypted)
					   .getPayload();
		}
		
	}
	
}
