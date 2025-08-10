package com.thiscompany.ttrack.config;

import com.thiscompany.ttrack.config.JWT.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests(
                   auth ->
                       auth
                           .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                           .requestMatchers(HttpMethod.POST, "/register").permitAll()
                           .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                           .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                           .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                           .requestMatchers("/api/v1/**").authenticated()
                           .anyRequest().authenticated()
                )
                .exceptionHandling(
                        handler->
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

}
