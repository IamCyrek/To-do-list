package com.example.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<?> getResponseEntity(
            HttpStatus status, String error, String message, String path) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );
        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    ResponseEntity<?> handleResourceNotFoundException(EmptyResultDataAccessException ex, WebRequest request) {
        return getResponseEntity(
                HttpStatus.NOT_FOUND,
                "The requested resource is not available.",
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return getResponseEntity(
                HttpStatus.BAD_REQUEST,
                "The request sent by the client was syntactically incorrect.",
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        return getResponseEntity(
                HttpStatus.CONFLICT,
                "The request could not be completed due to a conflict with the current state of the resource.",
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> handleGlobalExceptionHandler(Exception ex, WebRequest request) {
        return getResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error inside the HTTP server which prevented it from fulfilling the request.",
                ex.getMessage(),
                request.getDescription(false));
    }
}
