package com.thiscompany.ttrack.exceptions.exist;

import com.thiscompany.ttrack.exceptions.base.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends CustomRuntimeException {
	
	public UserAlreadyExistException(String message, Object[] args) {
		super(message, HttpStatus.CONFLICT, args);
	}
	
}
