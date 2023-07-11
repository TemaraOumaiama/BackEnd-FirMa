package com.app.exception;

public class ConnectionResetException extends RuntimeException  {

    public ConnectionResetException(String message) {
        super(message);
    }

}
