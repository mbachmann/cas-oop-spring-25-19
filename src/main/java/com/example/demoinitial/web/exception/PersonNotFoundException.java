package com.example.demoinitial.web.exception;

import java.util.function.Supplier;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }

}

