package com.thiscompany.ttrack.controller;

import com.thiscompany.ttrack.utils.common.ProblemDetailCreator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Own yet mock-class to handle 500s
 */
@RestController
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	
	private final ProblemDetailCreator problemBuilder;
	
	@RequestMapping("/error")
	public ResponseEntity<ProblemDetail> handleError(HttpServletRequest request) {
		var problemDetail = problemBuilder.createProblemDetail(
			HttpStatus.BAD_REQUEST,
			"error.500",
			new Object[]{}
		);
		problemDetail.setProperty("error",request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
		return ResponseEntity.internalServerError().body(problemDetail);
	}

}
