package com.phyriak.kafka;

import com.phyriak.repository.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(UUID eventId,
                                 Long paymentId,
                                 PaymentStatus status,
                                 Long orderId,
                                 BigDecimal amount,
                                 String currency,
                                 Instant processedAt
) {
}