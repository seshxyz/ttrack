package com.thiscompany.ttrack.utils.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProblemDetailBuilder {
	
	private final MessageSource messageSource;
	
	public ProblemDetail buildProblemDetail(
		HttpStatus status, String messageKey, Object[] args
	) {
		return ProblemDetail.forStatusAndDetail(
			status,
			Objects.requireNonNull(
				messageSource.getMessage(messageKey, args, "error.400", LocaleContextHolder.getLocale())
			)
		);
	}
	
	public ResponseEntity<ProblemDetail> buildProblemDetailResponse(
		HttpStatus status, String messageKey,
		Object[] args
	) {
		var problemDetail = buildProblemDetail(status, messageKey, args);
		return ResponseEntity.status(status).body(problemDetail);
	}
	
}
