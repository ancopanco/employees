package com.employees.employees.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<ErrorDetails> buildErrorResponse(Exception exception, WebRequest webRequest, String errorCode, HttpStatus status) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                errorCode);
        return new ResponseEntity<>(errorDetails, status);
    }
    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleRecordAlreadyExistsException(RecordAlreadyExistsException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest,"RECORD_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordDoesNotExists.class)
    public ResponseEntity<ErrorDetails> handleRecordIdDoesNotExists(RecordDoesNotExists exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest,"RECORD_DOES_NOT_EXISTS", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CreateFailedException.class)
    public ResponseEntity<ErrorDetails> handleCreateFailedException(UpdateFailedException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest,"CREATE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateFailedException.class)
    public ResponseEntity<ErrorDetails> handleUpdateFailedException(UpdateFailedException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest,"UPDATE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteFailedException.class)
    public ResponseEntity<ErrorDetails> handleDeleteFailedException(DeleteFailedException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest,"DELETE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest webRequest) {
        Map<String, String> fieldErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((FieldError error) ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", true);
        responseBody.put("message", "Validation failed");
        responseBody.put("details", fieldErrors);

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}