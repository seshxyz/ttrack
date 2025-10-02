package com.thiscompany.ttrack.controller.task.dto;

public record TaskResponse(
	
	String id,
	String title,
	String details,
	String priority,
	String status,
	String state,
	boolean isCompleted

) {

}
