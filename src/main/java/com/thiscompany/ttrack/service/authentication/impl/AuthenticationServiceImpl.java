package com.thiscompany.ttrack.service.authentication.impl;

import com.thiscompany.ttrack.config.security.JWT.JwtService;
import com.thiscompany.ttrack.controller.authentication.dto.AuthRequest;
import com.thiscompany.ttrack.controller.authentication.dto.AuthResponse;
import com.thiscompany.ttrack.controller.user.dto.UserAuthDate;
import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.kafka.producer.KafkaCommonProducer;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.model.UserPermission;
import com.thiscompany.ttrack.repository.UserPermissionRepository;
import com.thiscompany.ttrack.repository.UserRepository;
import com.thiscompany.ttrack.service.authentication.AuthenticationService;
import com.thiscompany.ttrack.service.user.mapper.UserMapper;
import com.thiscompany.ttrack.utils.constant.PermissionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Value("${app.kafka.topic}")
	private String topic;
	
	private final KafkaCommonProducer<UserAuthDate> kafka;
	private final UserRepository userRepo;
	private final JwtService jwtService;
	private final AuthenticationManager authManager;
	private final UserPermissionRepository userPermisRepo;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
	public AuthResponse authenticate(AuthRequest request) {
		var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
		String token = jwtService.generator()
								 .subject(request.username())
								 .generateAndEncrypt(new Date());
		authManager.authenticate(authToken);
		kafka.sendEvent(topic, new UserAuthDate(request.username(), LocalDateTime.now()));
		return new AuthResponse(token);
	}
	
	public void register(UserCreationRequest request) {
		User newUser = userMapper.createRequestToEntity(request, passwordEncoder);
		UserPermission userPermission = new UserPermission();
		userPermission.setUser_name(newUser)
					  .setPermission(PermissionConstant.getDefaultPermission());
		newUser.setPermissions(Set.of(userPermission));
		userRepo.save(newUser);
	}
	
}
