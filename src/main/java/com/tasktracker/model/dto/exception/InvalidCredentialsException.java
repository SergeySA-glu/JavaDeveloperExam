package com.tasktracker.model.dto.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AbstractApiException {

    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
    }
}
