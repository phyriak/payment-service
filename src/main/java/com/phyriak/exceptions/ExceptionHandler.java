package com.phyriak.exceptions;

import com.phyriak.dto.PaymentApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PaymentApiResponse> handleExceptions(ValidationException validationException) {
        return ResponseEntity.badRequest()
                .body(new PaymentApiResponse(null, validationException.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<PaymentApiResponse> handlePaymentExceptions(PaymentNotFoundException paymentNotFoundException) {
        log.warn(paymentNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new PaymentApiResponse(null, paymentNotFoundException.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CurrencyNotFoundError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PaymentApiResponse> handleExceptions(CurrencyNotFoundError currencyNotFoundError) {
        return ResponseEntity.badRequest()
                .body(new PaymentApiResponse(null, currencyNotFoundError.getMessage()));
    }

 /*   @org.springframework.web.bind.annotation.ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PaymentApiResponse> handleExceptions(ObjectOptimisticLockingFailureException optimisticLockingException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new PaymentApiResponse(null, optimisticLockingException.getMessage()));
    }*/

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentIllegalStatus.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PaymentApiResponse> handleExceptions(PaymentIllegalStatus paymentIllegalStatus) {
        return ResponseEntity.badRequest()
                .body(new PaymentApiResponse(null, paymentIllegalStatus.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<PaymentApiResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new PaymentApiResponse(null, "Internal server error"));
    }
}
