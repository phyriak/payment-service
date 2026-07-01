package com.phyriak.exceptions;

public class ServiceOverloadedException extends RuntimeException{
    public ServiceOverloadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
