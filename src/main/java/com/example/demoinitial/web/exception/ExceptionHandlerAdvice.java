package com.example.demoinitial.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<String> handlePersonNotFoundException(PersonNotFoundException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
    }
/*
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
    }*/
}
