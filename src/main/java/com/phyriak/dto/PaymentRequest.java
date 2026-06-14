package com.phyriak.dto;

import com.phyriak.repository.model.PaymentStatus;
import com.phyriak.repository.model.PaymentType;

import java.math.BigDecimal;

public record PaymentRequest(
        Long id,
        String currency,
        BigDecimal amount,
        String userId,
        String userEmail,
        String orderId,
        PaymentType paymentType,
        PaymentStatus paymentStatus
) {
}
