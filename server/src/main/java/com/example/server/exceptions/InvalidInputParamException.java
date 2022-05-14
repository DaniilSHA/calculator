package com.example.server.exceptions;

public class InvalidInputParamException extends RuntimeException {
    public InvalidInputParamException(String msg) {
        super(msg);
    }
}
