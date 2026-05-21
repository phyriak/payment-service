package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.model.PaymentResult;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(Long id) {
        return new PaymentResult(true, "Succeed");
    }

    @Override
    public PaymentType getType() {
        return PaymentType.PAYPAL;
    }
}
