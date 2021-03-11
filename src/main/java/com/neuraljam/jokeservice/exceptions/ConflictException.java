package com.neuraljam.jokeservice.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends ServiceResponseException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
