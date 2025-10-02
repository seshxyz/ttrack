package com.thiscompany.ttrack.exceptions.illegal;

import com.thiscompany.ttrack.exceptions.base.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class TaskIllegalStateException extends CustomRuntimeException {
	
	public TaskIllegalStateException(String message) {
		super(message, HttpStatus.CONFLICT, new Object[]{});
	}
	
}
