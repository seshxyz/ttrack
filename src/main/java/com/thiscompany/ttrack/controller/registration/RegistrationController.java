package com.thiscompany.ttrack.controller.registration;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.service.user.register.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Registration API", description = "Endpoint for user registration. Allowed for Admin.")
public class RegistrationController {
	
	private final RegisterService registerService;
	
	@Operation(summary = "Register user", description = "Method to register a new user")
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserCreationRequest request) {
		registerService.register(request);
		return new ResponseEntity<>(
			"User [" + request.username() + "] registered successfully",
			HttpStatus.CREATED
		);
	}
	
}
