package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.model.PaymentResult;
import org.springframework.stereotype.Component;

@Component
public class CardPayment implements PaymentStrategy{
    @Override
    public PaymentResult processPayment(Long id) {
        return new PaymentResult(true,"SUCCESS");
    }

    @Override
    public PaymentType getType() {
        return PaymentType.CARD;
    }
}
