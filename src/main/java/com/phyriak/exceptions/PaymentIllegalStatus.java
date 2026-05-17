package com.phyriak.exceptions;

public class PaymentIllegalStatus extends RuntimeException{
    public PaymentIllegalStatus(String message) {
        super(message);
    }
}
