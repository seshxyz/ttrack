package com.thiscompany.ttrack.controller.authentication.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(
	@NotEmpty(message = "field.empty_username")
	String username,
	
	@NotEmpty(message = "field.empty_password")
	String password
) {

}
