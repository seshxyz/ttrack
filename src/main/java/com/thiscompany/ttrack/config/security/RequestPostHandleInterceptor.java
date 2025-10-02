package com.thiscompany.ttrack.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.thiscompany.ttrack.config.security.JWT.ClaimsExtractorService.threadClaimsHolder;

@Component
public class RequestPostHandleInterceptor implements HandlerInterceptor {
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if(threadClaimsHolder != null) {
			threadClaimsHolder.remove();
		}
	}
	
}
