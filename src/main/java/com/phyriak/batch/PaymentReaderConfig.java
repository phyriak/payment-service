package com.phyriak.batch;

import com.phyriak.batch.dto.PaymentCsvDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.FileSystemResource;

@Configuration
public class PaymentReaderConfig {

    @Bean
    @StepScope
    public org.springframework.batch.item.file.FlatFileItemReader<PaymentCsvDto> paymentReader(
            @Value("#{jobParameters['filePath']}")
            String filePath

    ) {

        return new FlatFileItemReaderBuilder<PaymentCsvDto>()
                .name("paymentReader")
                .resource(new FileSystemResource(filePath))
                .linesToSkip(1)
                .delimited()
                .names(
                        "amount",
                        "currency",
                        "userId",
                        "paymentStatus",
                        "paymentType"
                )
                .targetType(PaymentCsvDto.class)
                .build();
    }
}
