package com.phyriak.batch;

import com.phyriak.batch.dto.PaymentCsvDto;
import com.phyriak.repository.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class PaymentBatchConfig {

    @Bean
    public Job paymentImportJob(
            JobRepository jobRepository,
            Step paymentImportStep
    ) {

        return new JobBuilder(
                "paymentImportJob",
                jobRepository
        )
                .start(paymentImportStep)
                .build();
    }

    @Bean
    public Step paymentImportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<PaymentCsvDto> paymentReader,
            ItemProcessor<PaymentCsvDto, Payment> paymentProcessor,
            JpaItemWriter<Payment> paymentWriter
    ) {

        return new StepBuilder(
                "paymentImportStep",
                jobRepository
        )
                .<PaymentCsvDto, Payment>chunk(1000, transactionManager)
                .reader(paymentReader)
                .processor(paymentProcessor)
                .writer(paymentWriter)
                .build();
    }
}