package com.phyriak.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentProcessed(PaymentProcessedEvent event) {
        kafkaTemplate.send(
                "payment",
                //all payment event goes to the same partition, ordering guaranteed per payment
                event.paymentId().toString(),
                event
        );
    }

    public void publishPaymentFailed(PaymentFailedEvent event) {
        kafkaTemplate.send(
                "payment",
                //all payment event goes to the same partition, ordering guaranteed per payment
                event.paymentId().toString(),
                event
        );
    }
}
