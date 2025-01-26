package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TeamDoesNotExists extends RuntimeException {
    private String message;

    public TeamDoesNotExists(String message) {
        super(message);
    }
}