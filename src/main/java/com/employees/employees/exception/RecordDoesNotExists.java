package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RecordDoesNotExists extends RuntimeException {
    private String message;

    public RecordDoesNotExists(String message) {
        super(message);
    }
}