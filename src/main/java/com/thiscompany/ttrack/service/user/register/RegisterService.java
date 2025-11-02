package com.thiscompany.ttrack.service.user.register;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;

public interface RegisterService {
	
	void register(UserCreationRequest request);
	
}
