package com.thiscompany.ttrack.controller.task.dto;

import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
	
	@Size(max = 200, message = "field.long_title")
	String title,
	
	@Size(max = 540, message = "field.long_details")
	String details,
	
	String priority

) {

}
