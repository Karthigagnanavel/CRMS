package com.app.crms.exception;
// Custom exception extending NumberFormatException
public class InvalidNumberFormatException extends NumberFormatException {
    public InvalidNumberFormatException(String message) {
        super(message);
    }
}

