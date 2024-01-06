package com.code.mintyn.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class MintynException extends Exception {
    private final HttpStatus httpStatus;

    public MintynException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public MintynException(String message) {
        super(message);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public MintynException(Throwable cause) {
        super(cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public MintynException(Throwable cause, HttpStatus status) {
        super(cause);
        this.httpStatus = status;
    }

    public MintynException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public MintynException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
