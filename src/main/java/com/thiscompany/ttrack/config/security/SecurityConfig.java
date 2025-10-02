package com.thiscompany.ttrack.config.security;

import com.thiscompany.ttrack.config.security.JWT.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {
	
	private final JwtAuthFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final AuthEntryPoint authEntryPoint;
	private final RequestPostHandleInterceptor requestPostHandleInterceptor;
	
	@Value("${server.port}")
	private String port;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(CsrfConfigurer::disable)
			.cors(httpSecurityCorsConfigurer -> {
			
			})
			.authorizeHttpRequests(
				auth ->
					auth
						.requestMatchers(HttpMethod.POST, "/auth").permitAll()
						.requestMatchers(HttpMethod.POST, "/register").permitAll()
						.requestMatchers("/api/v1/**").authenticated()
						.requestMatchers( "/swagger-ui/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
						.requestMatchers("/error").permitAll()
						.anyRequest().authenticated()
			)
			.exceptionHandling(
				handler ->
					handler.authenticationEntryPoint(authEntryPoint)
						   .accessDeniedHandler(customAccessDeniedHandler)
			)
			.sessionManagement(
				configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS
				))
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.requestCache(RequestCacheConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			.anonymous(AnonymousConfigurer::disable)
			.logout(LogoutConfigurer::disable);
		return http.build();
	}
	
	@Bean
	public CorsFilter corsConfiguration() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedMethods(List.of("GET","POST","PATCH","DELETE","OPTIONS"));
		corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:" + port));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestPostHandleInterceptor);
	}
	
}
