package com.neuraljam.jokeservice.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorModel> handle(HttpServletRequest request, ConstraintViolationException exception) {
        ErrorModel errorModel;
        StringBuilder errorMessage = new StringBuilder();
        List<ValidationErrorModel> validationErrorModelList = new ArrayList<>();
        for (ConstraintViolation constraintViolation : exception.getConstraintViolations()) {
            ValidationErrorModel validationErrorModel = new ValidationErrorModel(
                    constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage(),
                    constraintViolation.getInvalidValue());
            validationErrorModelList.add(validationErrorModel);
            errorMessage.append(validationErrorModel.toString()).append("; ");
        }

        errorModel = new ErrorModel(errorMessage.toString(), HttpStatus.BAD_REQUEST, getRequestUrl(request),
                validationErrorModelList);
        return new ResponseEntity<>(errorModel, errorModel.getHttpStatusCode());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorModel> handle(HttpServletRequest request, MethodArgumentNotValidException exception) {
        ErrorModel errorModel;
        List<ValidationErrorModel> validationErrorModelList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder("Validation Error: ");
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            ValidationErrorModel validationErrorModel = new ValidationErrorModel(fieldError.getField(), fieldError
                    .getDefaultMessage(), fieldError.getRejectedValue());
            validationErrorModelList.add(validationErrorModel);
            errorMessage.append(validationErrorModel.toString()).append("; ");
        }

        errorModel = new ErrorModel(errorMessage.toString(), HttpStatus.BAD_REQUEST, getRequestUrl(request),
                validationErrorModelList);
        return new ResponseEntity<>(errorModel, errorModel.getHttpStatusCode());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorModel> handle(HttpServletRequest request, UnrecognizedPropertyException exception) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();
        data.put("unknownProperty", exception.getPropertyName());
        data.set("knownProperties", mapper.valueToTree(exception.getKnownPropertyIds()));
        ErrorModel errorModel = new ErrorModel("Unknown property: '" + exception.getPropertyName() + "'",
                HttpStatus.BAD_REQUEST, getRequestUrl(request), data);
        return new ResponseEntity<>(errorModel, errorModel.getHttpStatusCode());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorModel> handle(HttpServletRequest request, ServiceResponseException e) {
        ErrorModel errorModel = new ErrorModel(e.getMessage(), e.getHttpStatusCode(), getRequestUrl(request), e.getData());
        return new ResponseEntity<>(errorModel, errorModel.getHttpStatusCode());
    }

    private String getRequestUrl(HttpServletRequest request) {
        if (request == null) {
            return "No HTTP Request";
        } else {
            return request.getRequestURL().toString();
        }
    }
}