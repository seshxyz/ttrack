package com.thiscompany.ttrack.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
	
	private final ObjectMapper objectMapper;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		objectMapper.writeValue(
			response.getWriter(),
			Map.of("path", request.getRequestURI(),
				"error", "Not authorized")
		);
	}
	
}
