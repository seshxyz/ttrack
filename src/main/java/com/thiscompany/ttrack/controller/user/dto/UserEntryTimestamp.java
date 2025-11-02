package com.thiscompany.ttrack.controller.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserEntryTimestamp(
	
	String username,
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	LocalDateTime lastLogin
) {

}
