package com.erii.exception;

public class DateTimeNotAfterCurrentTimeException extends RuntimeException {
    public DateTimeNotAfterCurrentTimeException(String message) {
        super(message);
    }
}

