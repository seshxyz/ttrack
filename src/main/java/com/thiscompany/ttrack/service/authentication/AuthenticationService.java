package com.thiscompany.ttrack.service.authentication;

import com.thiscompany.ttrack.controller.authentication.dto.AuthRequest;
import com.thiscompany.ttrack.controller.authentication.dto.AuthResponse;
import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;

public interface AuthenticationService {

    void register(UserCreationRequest request);

    AuthResponse authenticate(AuthRequest request);

}
