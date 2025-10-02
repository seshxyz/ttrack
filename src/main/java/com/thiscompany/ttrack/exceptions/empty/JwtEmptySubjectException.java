package com.thiscompany.ttrack.exceptions.empty;

import com.thiscompany.ttrack.exceptions.base.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class JwtEmptySubjectException extends CustomRuntimeException {
	
	public JwtEmptySubjectException() {
		super("token.empty_subject", HttpStatus.UNAUTHORIZED, new Object[]{});
	}
	
}
