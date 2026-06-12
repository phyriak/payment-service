package com.phyriak.strategy;

import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.template.PaymentMethodTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentFactory {

    private final Map<PaymentType, PaymentMethodTemplate> paymentpaymentMethodMap;

    public PaymentFactory(List<PaymentMethodTemplate> paymentMethod) {
        this.paymentpaymentMethodMap = paymentMethod.stream()
                .collect(Collectors.toMap(PaymentStrategy::getType, Function.identity()));
    }

    public PaymentMethodTemplate getStrategy(PaymentType type) {
        return paymentpaymentMethodMap.get(type);
    }
}
