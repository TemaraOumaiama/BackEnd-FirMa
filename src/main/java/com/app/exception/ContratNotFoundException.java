package com.app.exception;


public class ContratNotFoundException extends RuntimeException {
    public ContratNotFoundException(String message) {
        super(message);
    }
}