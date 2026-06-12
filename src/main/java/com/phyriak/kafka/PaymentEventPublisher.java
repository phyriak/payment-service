package com.phyriak.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentProcessed(PaymentProcessedEvent event) {
        log.info("Publishing payment event: {}", event);
        kafkaTemplate.send(
                "payment",
                //all payment event goes to the same partition, ordering guaranteed per payment
                event.paymentId().toString(),
                event
        );
    }

    public void publishPaymentFailed(PaymentFailedEvent event) {
        log.info("Publishing payment event: {}", event);
        kafkaTemplate.send(
                "payment",
                //all payment event goes to the same partition, ordering guaranteed per payment
                event.paymentId().toString(),
                event
        );
    }
}
