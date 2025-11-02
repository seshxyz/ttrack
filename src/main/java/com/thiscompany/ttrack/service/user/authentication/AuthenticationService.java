package com.thiscompany.ttrack.service.user.authentication;

import com.thiscompany.ttrack.controller.authentication.dto.AuthRequest;
import com.thiscompany.ttrack.controller.authentication.dto.AuthResponse;

public interface AuthenticationService {
	
	AuthResponse authenticate(AuthRequest request);
	
}
