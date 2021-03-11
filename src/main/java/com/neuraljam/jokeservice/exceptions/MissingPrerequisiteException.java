package com.neuraljam.jokeservice.exceptions;

public class MissingPrerequisiteException extends Exception {

    public MissingPrerequisiteException() {
    }

    public MissingPrerequisiteException(String message) {
        super(message);
    }
}
