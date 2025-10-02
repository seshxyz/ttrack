package com.thiscompany.ttrack.controller.task.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record NewTaskRequest(
	
	@Nullable
	@Size(max = 300, message = "field.long_title")
	String title,
	
	@Nullable
	@Size(max = 540, message = "field.long_details")
	String details,
	
	@Nullable
	String priority

) {

}
