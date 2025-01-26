package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RecordAlreadyExistsException extends RuntimeException{
    private String message;

    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}