package com.thiscompany.ttrack.controller.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreationRequest(
	
	@NotEmpty(message = "field.empty_username")
	@Size(min = 6, max = 36, message = "{field.short_or_long_username}")
	@Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{field.invalid_username}")
	String username,
	
	@NotEmpty(message = "{field.empty_password}")
	@Size(min = 8, max = 40, message = "Pfield.short_or_long_password}")
	String password

) {

}
