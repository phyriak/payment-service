package com.phyriak.dto;

import com.phyriak.repository.model.Currency;
import com.phyriak.repository.model.PaymentStatus;
import com.phyriak.repository.model.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentDto(
        @NotBlank
        Long id,
        @NotBlank
        Currency currency,
        @NotNull
        @Positive
        BigDecimal amount,
        @NotBlank
        Long userId,
        @NotBlank
        String userEmail,
        @NotBlank
        Long orderId,
        @NotBlank
        LocalDateTime updatedAt,
        @NotBlank
        PaymentType paymentType,
        @NotBlank
        PaymentStatus paymentStatus
) {
}
