package com.thiscompany.ttrack.exceptions.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Object[] args;

    public CustomRuntimeException(String message, HttpStatus status, Object[] args) {
        super(message);
        this.httpStatus = status;
        this.args = args;
    }

}
