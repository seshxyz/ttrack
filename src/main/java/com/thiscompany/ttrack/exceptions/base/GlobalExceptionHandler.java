package com.thiscompany.ttrack.exceptions.base;


import com.thiscompany.ttrack.utils.common.ProblemDetailCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	
	private final ProblemDetailCreator problemDetailCreator;
	
	@ExceptionHandler(CustomRuntimeException.class)
	public ResponseEntity<ProblemDetail> handleCustomRuntimeException(
		CustomRuntimeException ex
	) {
		return problemDetailCreator.buildProblemDetailResponse(
			ex.getHttpStatus(),
			ex.getMessage(),
			ex.getArgs()
		);
	}
	
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ProblemDetail> handleBindException(BindException ex) {
		var problemdetail = problemDetailCreator.createProblemDetail(
			HttpStatus.BAD_REQUEST,
			"error.400",
			new Object[0]
		);
		problemdetail.setDetail(null);
		problemdetail.setProperty("errors", ex.getAllErrors()
											  .stream()
											  .map(ObjectError::getDefaultMessage)
											  .toList()
		);
		return ResponseEntity.badRequest()
							 .body(problemdetail);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
		HttpMessageNotReadableException exception
	) {
		return problemDetailCreator.buildProblemDetailResponse(
			HttpStatus.BAD_REQUEST,
			"error.400",
			new Object[]{exception.getMessage()}
		);
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ProblemDetail> handleNoResourceFoundException(NoResourceFoundException ex) {
		return problemDetailCreator.buildProblemDetailResponse(
			HttpStatus.NOT_FOUND,
			"error.404",
			new Object[]{ex.getBody().getInstance()}
		);
	}
	
	@ExceptionHandler(HttpServerErrorException.InternalServerError.class)
	public ResponseEntity<ProblemDetail> handleInternalServerErrorException(HttpServerErrorException.InternalServerError exception) {
		return problemDetailCreator.buildProblemDetailResponse(
			HttpStatus.BAD_REQUEST,
			"error.400",
			new Object[]{exception.getMessage()}
		);
	}
	
}
