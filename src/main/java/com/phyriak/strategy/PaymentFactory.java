package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentFactory {

    private final Map<PaymentType, PaymentStrategy> paymentStrategyMap;

    public PaymentFactory(List<PaymentStrategy> paymentStrategyList) {
        this.paymentStrategyMap = paymentStrategyList.stream()
                .collect(Collectors.toMap(PaymentStrategy::getType, Function.identity()));
    }

    public PaymentStrategy getStrategy(PaymentType type) {
        return paymentStrategyMap.get(type);
    }
}
