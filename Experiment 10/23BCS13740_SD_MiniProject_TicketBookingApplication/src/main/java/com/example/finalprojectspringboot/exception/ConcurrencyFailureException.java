package com.example.finalprojectspringboot.exception;

public class ConcurrencyFailureException extends RuntimeException {

    public ConcurrencyFailureException(String message) {
        super(message);
    }

    public ConcurrencyFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
