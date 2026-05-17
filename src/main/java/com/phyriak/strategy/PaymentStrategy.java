package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.model.PaymentResult;

public interface PaymentStrategy {

   PaymentResult processPayment(Long id);

   PaymentType getType();
}
