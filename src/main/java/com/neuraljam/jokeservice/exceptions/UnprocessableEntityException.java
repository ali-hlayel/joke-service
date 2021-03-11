package com.neuraljam.jokeservice.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends ServiceResponseException {

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
