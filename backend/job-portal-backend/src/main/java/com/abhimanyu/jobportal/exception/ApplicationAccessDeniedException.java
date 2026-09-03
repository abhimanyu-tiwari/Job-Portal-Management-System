package com.abhimanyu.jobportal.exception;

public class ApplicationAccessDeniedException extends RuntimeException {

    public ApplicationAccessDeniedException(String message) {
        super(message);
    }
}