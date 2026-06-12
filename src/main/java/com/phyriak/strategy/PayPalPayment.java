package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.template.PaymentMethodTemplate;
import com.phyriak.strategy.model.PaymentResult;

public class PayPalPayment extends PaymentMethodTemplate {

    @Override
    protected PaymentResult pay() {
        //Here specific integration with payment provider

        return new PaymentResult(true,"Success");
    }

    @Override
    public PaymentType getType() {
        return PaymentType.PAYPAL;
    }

    @Override
    protected void validateOrder(double amount) {
        System.out.println("Specific validation");
    }
}
