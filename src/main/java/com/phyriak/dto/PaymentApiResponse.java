package com.phyriak.dto;

import java.util.List;

public record PaymentApiResponse(
        List<PaymentDto> payloads,
        String message
) {
}
