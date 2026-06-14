package com.phyriak.kafka;

import com.phyriak.repository.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(UUID eventId,
                                 Long paymentId,
                                 Long userId,
                                 Long orderId,
                                 PaymentStatus status,
                                 BigDecimal amount,
                                 String currency,
                                 Instant processedAt,
                                 String email
) {
}