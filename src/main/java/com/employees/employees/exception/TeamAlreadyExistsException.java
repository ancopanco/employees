package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TeamAlreadyExistsException extends RuntimeException{
    private String message;

    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}