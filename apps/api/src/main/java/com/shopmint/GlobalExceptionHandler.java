package com.shopmint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific custom exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleResourceNotFoundException(RuntimeException ex) {
        return new ResponseEntity<String>(ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    // Handle generic exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<String>("Unexpected error occured.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
