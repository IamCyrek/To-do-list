package com.example.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "The requested resource is not available",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "The request sent by the client was syntactically incorrect.",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "The request could not be completed due to a conflict with the current state of the resource.",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "An error inside the HTTP server which prevented it from fulfilling the request.",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
