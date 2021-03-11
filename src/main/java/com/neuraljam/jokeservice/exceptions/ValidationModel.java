package com.neuraljam.jokeservice.exceptions;

class ValidationErrorModel {

    private final String field;

    private final String errorMessage;

    private final Object givenValue;

    ValidationErrorModel(String field, String errorMessage, Object givenValue) {
        this.field = field;
        this.errorMessage = errorMessage;
        this.givenValue = givenValue;
    }

    public String getField() {
        return field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getGivenValue() {
        return givenValue;
    }

    public String toString() {
        return "Validation error: " +
                field + " " +
                errorMessage +
                " (given value: " +
                givenValue +
                ")";
    }
}