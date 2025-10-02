package com.thiscompany.ttrack.controller.user.dto;

import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
	
	String id,
	
	@NotNull
	UserUpdateBody data

) {

}
