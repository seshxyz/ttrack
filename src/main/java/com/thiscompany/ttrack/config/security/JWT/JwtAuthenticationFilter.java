package com.thiscompany.ttrack.config.security.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiscompany.ttrack.exceptions.empty.JwtEmptySubjectException;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.utils.common.ProblemDetailCreator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ClaimsExtractorService claimsExtractor;
    private final ObjectMapper objectMapper;
    private final ProblemDetailCreator problemDetailCreator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
            try {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")){
                    filterChain.doFilter(request, response);
                    return;
                }
                String token = authHeader.substring(7);
                claimsExtractor.extractAllClaims(token);
                String username = Optional.of(claimsExtractor.extractSubject())
                                          .orElseThrow(JwtEmptySubjectException::new);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if(claimsExtractor.tokenValidNotBefore()){
                        setAuthentication(token, request);
                        filterChain.doFilter(request, response);
                    }
                }

            } catch (SignatureException e) {
                returnProblemDetailResponse(request, response, "token.signature_invalid", null);
            } catch (JwtEmptySubjectException e) {
                returnProblemDetailResponse(request, response, "token.empty_subject", null);
            } catch (ExpiredJwtException e) {
                returnProblemDetailResponse(request, response, "token.expired", null);
            } catch (UsernameNotFoundException e) {
                returnProblemDetailResponse(request, response, "user.not_found", new Object[]{e.getMessage()});
            }
        }

    private void setAuthentication(String token, HttpServletRequest request) {
        String username = Objects.requireNonNull(claimsExtractor.extractSubject());
        User userToAuthenticate = (User) userDetailsService.loadUserByUsername(username);
        var authToken = new UsernamePasswordAuthenticationToken(
                userToAuthenticate,
                null,
                userToAuthenticate.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void returnProblemDetailResponse(HttpServletRequest request,
                                             HttpServletResponse response,
                                             String detail, Object[] args) throws IOException {
        
        var problemDetail = problemDetailCreator.createProblemDetail(HttpStatus.UNAUTHORIZED, detail, args);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
        return;
    }
}
