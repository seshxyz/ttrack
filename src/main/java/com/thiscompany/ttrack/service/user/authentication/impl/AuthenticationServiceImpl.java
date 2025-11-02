package com.thiscompany.ttrack.service.user.authentication.impl;

import com.thiscompany.ttrack.config.security.JWT.JwtService;
import com.thiscompany.ttrack.controller.authentication.dto.AuthRequest;
import com.thiscompany.ttrack.controller.authentication.dto.AuthResponse;
import com.thiscompany.ttrack.controller.user.dto.UserEntryTimestamp;
import com.thiscompany.ttrack.kafka.producer.KafkaGenericProducer;
import com.thiscompany.ttrack.service.user.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Value("${app.kafka.topic}")
	private String topic;
	
	private final KafkaGenericProducer<UserEntryTimestamp> kafka;
	private final JwtService jwtService;
	private final AuthenticationManager authManager;
	
	@Override
	public AuthResponse authenticate(AuthRequest request) {
		var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
		String token = jwtService.generator()
								 .subject(request.username())
								 .generateAndEncrypt(new Date());
		authManager.authenticate(authToken);
		kafka.sendEvent(topic, new UserEntryTimestamp(request.username(), LocalDateTime.now()));
		return new AuthResponse(token);
	}
	
}
