package com.phyriak.dto;

import java.math.BigDecimal;
import java.util.List;

public record NbpResponse(
        List<Rate> rates
) {
    public record Rate(
            String no,
            String effectiveDate,
            BigDecimal mid
    ) {}
}