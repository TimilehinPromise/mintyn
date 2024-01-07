package com.code.mintyn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CardNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;
    
    public CardNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CardNotFoundException(String message) {
        super(message);
        httpStatus = BAD_REQUEST;
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = BAD_REQUEST;
    }
}