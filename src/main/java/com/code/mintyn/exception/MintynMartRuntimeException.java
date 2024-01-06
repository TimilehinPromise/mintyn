package com.code.mintyn.exception;

public class MintynMartRuntimeException extends RuntimeException {

    public MintynMartRuntimeException(String message) {
        super(message);
    }

    public MintynMartRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
