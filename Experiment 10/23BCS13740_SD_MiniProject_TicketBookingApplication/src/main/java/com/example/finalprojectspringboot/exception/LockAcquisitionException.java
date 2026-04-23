package com.example.finalprojectspringboot.exception;

public class LockAcquisitionException extends RuntimeException {

    public LockAcquisitionException(String message) {
        super(message);
    }
}
