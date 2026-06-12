package com.phyriak.service;

import com.phyriak.exceptions.PaymentIllegalStatus;
import com.phyriak.exceptions.PaymentNotFoundException;
import com.phyriak.kafka.PaymentEventPublisher;
import com.phyriak.kafka.PaymentFailedEvent;
import com.phyriak.kafka.PaymentProcessedEvent;
import com.phyriak.repository.PaymentRepository;
import com.phyriak.repository.model.Payment;
import com.phyriak.repository.model.PaymentStatus;
import com.phyriak.strategy.PaymentFactory;
import com.phyriak.strategy.model.PaymentResult;
import com.phyriak.strategy.template.PaymentMethodTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;
    private final PaymentFactory paymentFactory;
    private final PaymentEventPublisher eventPublisher;

    @Transactional
    public void processPaymentAsync(Long paymentId) {
        log.info("Processing async payment {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id= " + paymentId + " not found!"));

        PaymentMethodTemplate paymentMethod = paymentFactory.getStrategy(payment.getPaymentType());
        if (paymentMethod == null) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentIllegalStatus("Wrong Payment Type: " + payment.getPaymentType());
        }
        PaymentResult result = paymentMethod.processPayment(payment.getId());

        if (result.success()) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            log.info("Payment success {}", paymentId);

        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            log.error("Payment failed: {}", payment.getId());

        }
        paymentRepository.save(payment);

        publishPaymentEvent(result, payment);

    }

    private void publishPaymentEvent(PaymentResult result, Payment payment) {
        if (result.success()) {
            eventPublisher.publishPaymentProcessed(getPaymentProcessedEvent(payment));
        } else {
            eventPublisher.publishPaymentFailed(getPaymentFailedEvent(payment));
        }
    }

    private PaymentProcessedEvent getPaymentProcessedEvent(Payment payment) {
        return new PaymentProcessedEvent(
                UUID.randomUUID(),
                payment.getId(),
                payment.getPaymentStatus(),
                null,
                payment.getAmount(),
                payment.getCurrency().toString(),
                Instant.now());
    }

    private PaymentFailedEvent getPaymentFailedEvent(Payment payment) {
        return new PaymentFailedEvent(
                UUID.randomUUID(),
                payment.getId(),
                payment.getPaymentStatus(),
                null,
                payment.getAmount(),
                payment.getCurrency().toString(),
                Instant.now());
    }
}
