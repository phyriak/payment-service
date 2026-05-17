package com.phyriak.batch;

import com.phyriak.repository.model.Payment;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentWriterConfig {
    @Bean
    public org.springframework.batch.item.database.JpaItemWriter<Payment> paymentWriter(
            EntityManagerFactory emf
    ) {

        org.springframework.batch.item.database.JpaItemWriter<Payment> writer = new org.springframework.batch.item.database.JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);

        return writer;
    }
}
