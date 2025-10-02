package com.thiscompany.ttrack.config;

import com.thiscompany.ttrack.utils.common.ProblemDetailBuilder;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Own mock-class to handle 500s
 */
@RestController
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	
	private final ProblemDetailBuilder problemBuilder;
	
	@RequestMapping("/error")
	public ResponseEntity<ProblemDetail> handleError(HttpServletRequest request) {
		var problemDetail = problemBuilder.buildProblemDetail(
			HttpStatus.BAD_REQUEST,
			"error.500",
			new Object[]{}
		);
		problemDetail.setProperty("error",request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
		return ResponseEntity.internalServerError().body(problemDetail);
	}

}
