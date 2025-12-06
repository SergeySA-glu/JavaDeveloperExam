package com.tasktracker.model.dto.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
@AllArgsConstructor
@JsonIgnoreProperties(value = {"cause", "stackTrace", "suppressed", "localizedMessage"})
public abstract class AbstractApiException extends Exception {

    protected final HttpStatus status;

    protected final String message;

    public AbstractApiException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Error");
    }

    public ResponseEntity<AbstractApiException> asResponse() {
        return ResponseEntity.status(status).body(this);
    }
}
