package com.thiscompany.ttrack.exceptions.base;


import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private ProblemDetail buildProblemDetail(
            HttpStatus status, String messageKey, Object[] args, Locale locale
    ) {
        return ProblemDetail.forStatusAndDetail(
                status,
                Objects.requireNonNull(
                        messageSource.getMessage(status.toString(), args, messageKey, Locale.ENGLISH)
                )
        );
    }

    private ResponseEntity<ProblemDetail> buildProblemDetailResponse(
            HttpStatus status, String messageKey,
            Object[] args, Locale locale
    ) {
        var problemDetail = buildProblemDetail(status, messageKey, args, Locale.ENGLISH);
        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ProblemDetail> handleCustomRuntimeException(
            CustomRuntimeException ex, Locale locale
    ) {
        return buildProblemDetailResponse(
                ex.getHttpStatus(),
                ex.getMessage(),
                ex.getArgs(),
                Locale.ENGLISH
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex, Locale locale) {
        var problemdetail =  buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "error.400",
                new Object[0],
                Locale.ENGLISH
        );

        problemdetail.setProperty("errors", ex.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList()
        );
        return ResponseEntity.badRequest().body(problemdetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            Locale locale
    ) {
        return buildProblemDetailResponse(
                HttpStatus.BAD_REQUEST,
                "error.400",
                new Object[] {exception.getMessage()},
                Locale.ENGLISH
        );
    }
}
