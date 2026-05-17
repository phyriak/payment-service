package com.phyriak.exceptions;

public class CurrencyNotFoundError extends RuntimeException{
    public CurrencyNotFoundError(String message) {
        super(message);
    }
}
