package com.phyriak.batch.dto;

import com.phyriak.repository.model.Currency;
import com.phyriak.repository.model.PaymentStatus;
import com.phyriak.repository.model.PaymentType;

import java.math.BigDecimal;

public record PaymentCsvDto(
        BigDecimal amount,
        Currency currency,
        Long userId,
        PaymentStatus paymentStatus,
        PaymentType paymentType
) {
}
