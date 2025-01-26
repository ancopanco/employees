package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeleteFailedException extends RuntimeException{
    private String message;

    public DeleteFailedException(String message) {
        super(message);
    }
}