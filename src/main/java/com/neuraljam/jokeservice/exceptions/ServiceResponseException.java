package com.neuraljam.jokeservice.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;

public abstract class ServiceResponseException extends Exception {

    private ObjectNode data;

    public ServiceResponseException(String message) {
        super(message);
    }

    public ServiceResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus getHttpStatusCode();

    public void setData(ObjectNode data) {
        this.data = data;
    }

    public JsonNode getData() {
        return data;
    }

    public void addData(String key, String value) {
        initData();
        data.put(key, value);
    }

    public void addData(String key, Long value) {
        initData();
        data.put(key, value);
    }

    public void addData(String key, Object value) {
        ObjectMapper mapper = new ObjectMapper();
        initData();
        data.set(key, mapper.valueToTree(value));
    }

    private void initData() {
        if(data == null) {
            ObjectMapper mapper = new ObjectMapper();
            data = mapper.createObjectNode();
        }
    }
}