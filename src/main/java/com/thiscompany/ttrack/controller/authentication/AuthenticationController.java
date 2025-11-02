package com.thiscompany.ttrack.controller.authentication;

import com.thiscompany.ttrack.controller.authentication.dto.AuthRequest;
import com.thiscompany.ttrack.controller.authentication.dto.AuthResponse;
import com.thiscompany.ttrack.service.user.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Endpoints for authentication and registration management")
public class AuthenticationController {
	
	private final AuthenticationService authService;
	
	@Operation(summary = "Authenticate user", description = "Method to authenticate a user")
	@PostMapping("/auth")
	public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
	
}
