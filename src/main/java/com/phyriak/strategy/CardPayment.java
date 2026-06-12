package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.model.PaymentResult;
import com.phyriak.strategy.template.PaymentMethodTemplate;
import org.springframework.stereotype.Component;

@Component
public class CardPayment extends PaymentMethodTemplate {
    @Override
    protected PaymentResult pay() {
        return new PaymentResult(true, "SUCCESS");
    }

    @Override
    public PaymentType getType() {
        return PaymentType.CARD;
    }

    @Override
    protected void validateOrder(double amount) {

    }
}
