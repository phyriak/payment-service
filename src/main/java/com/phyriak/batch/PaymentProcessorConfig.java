package com.phyriak.batch;

import com.phyriak.batch.dto.PaymentCsvDto;
import com.phyriak.repository.model.Payment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentProcessorConfig {
    @Bean
    public org.springframework.batch.item.ItemProcessor<PaymentCsvDto, Payment> paymentProcessor() {

        return dto -> {

            Payment payment = new Payment();

            payment.setAmount(dto.amount());
            payment.setCurrency(dto.currency());
            payment.setUserId(dto.userId());
            payment.setPaymentStatus(dto.paymentStatus());
            payment.setPaymentType(dto.paymentType());

            return payment;
        };
    }
}
