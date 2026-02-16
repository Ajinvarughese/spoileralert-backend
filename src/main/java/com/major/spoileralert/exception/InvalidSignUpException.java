package com.major.spoileralert.exception;

public class InvalidSignUpException extends Exception {
    public InvalidSignUpException(String errorMessage) {
        super(errorMessage);
    }
}
