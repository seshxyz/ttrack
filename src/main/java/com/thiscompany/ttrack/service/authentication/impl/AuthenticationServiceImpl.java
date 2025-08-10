package com.thiscompany.ttrack.service.authentication.impl;

import com.thiscompany.ttrack.config.JWT.JwtService;
import com.thiscompany.ttrack.controller.authentication.dto.AuthRequest;
import com.thiscompany.ttrack.controller.authentication.dto.AuthResponse;
import com.thiscompany.ttrack.controller.user.dto.UserAuthDate;
import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.kafka.template.MyKafkaProducer;
import com.thiscompany.ttrack.model.Permission;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.model.UserPermission;
import com.thiscompany.ttrack.repository.UserPermissionRepository;
import com.thiscompany.ttrack.repository.UserRepository;
import com.thiscompany.ttrack.service.authentication.AuthenticationService;
import com.thiscompany.ttrack.service.const_utils.Constants;
import com.thiscompany.ttrack.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${app.kafka.topic}")
    private String topic;

    private final MyKafkaProducer<UserAuthDate> kafka;
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserPermissionRepository userPermisRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthResponse authenticate(AuthRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        String token = jwtService.generator().subject(request.username()).generateAndEncrypt();
        authManager.authenticate(authToken);
        kafka.sendEvent(topic, new UserAuthDate(request.username(), LocalDateTime.now()));
        return new AuthResponse(token);
    }

    public void register(UserCreationRequest request) {
        User newUser = userMapper.createRequestToEntity(request, passwordEncoder);
        UserPermission userPermission = new UserPermission();
        Permission permission = userPermisRepo.getReferenceById(Constants.DEFAULT_PERMISSION_ID).getPermission();
        userPermission.setUser_name(newUser);
        userPermission.setPermission(permission);
        newUser.setPermissions(Set.of(userPermission));
        userRepo.save(newUser);
    }

}
