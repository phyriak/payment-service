package com.phyriak.service;

import com.phyriak.config.NBPRateClient;
import com.phyriak.dto.PaymentRequest;
import com.phyriak.repository.PaymentRepository;
import com.phyriak.repository.model.Payment;
import com.phyriak.repository.model.PaymentStatus;
import com.phyriak.repository.model.PaymentType;
import com.phyriak.strategy.PaymentFactory;
import com.phyriak.strategy.PaymentStrategy;
import com.phyriak.strategy.template.PaymentMethodTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PaymentServiceUnitTest {

    public static final String TEST_EMAIL = "dummy@test.pl";
    public static final String ORDER_ID = "2132131";
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private NBPRateClient rateClient;
    @MockitoBean
    private PaymentFactory paymentFactory;
    @Mock
    private ExecutorService executor;
    @Mock
    private PaymentStrategy paymentStrategy;
    @Mock
    private PaymentMethodTemplate paymentMethod;
    @Mock
    private PaymentProcessor paymentProcessor;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, rateClient, executor, paymentProcessor);
    }

    @Test
    void shouldValidateCurrencyBeforeMapping() {
        PaymentRequest request = new PaymentRequest(
                null,
                null,
                BigDecimal.TEN,
                "1",
                TEST_EMAIL,
                ORDER_ID,
                PaymentType.BLIK,
                PaymentStatus.PAID
        );

        assertThrows(NullPointerException.class, () -> paymentService.pay(request));
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(executor, never()).submit(any(Runnable.class));
    }

    //todo move to process payment
 /*   @Test
    void shouldMarkPaymentAsFailedWhenStrategyIsMissing() {
        AtomicReference<Payment> storedPayment = new AtomicReference<>();
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            if (payment.getId() == null) {
                payment.setId(1L);
            }
            storedPayment.set(payment);
            return payment;
        });
        when(paymentRepository.findById(1L)).thenAnswer(invocation -> Optional.ofNullable(storedPayment.get()));
        when(paymentFactory.getStrategy(PaymentType.CARD)).thenReturn(null);
        when(paymentProcessor.processPaymentAsync(anyLong())).thenThrow(new PaymentIllegalStatus(""));
        when(executor.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return CompletableFuture.completedFuture(null);
        });

        PaymentRequest request = new PaymentRequest(
                null,
                "PLN",
                new BigDecimal("20.00"),
                "123",
                PaymentType.CARD,
                null
        );

        //todo moved to Payment proccesor
         assertThrows(PaymentIllegalStatus.class, () -> paymentService.pay(request));
    }*/

   /* @Test
    void shouldMarkPaymentAsFailedWhenStrategyThrowsException() {
        AtomicReference<Payment> storedPayment = new AtomicReference<>();
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            if (payment.getId() == null) {
                payment.setId(1L);
            }
            storedPayment.set(payment);
            return payment;
        });
        when(paymentRepository.findById(1L)).thenAnswer(invocation -> Optional.ofNullable(storedPayment.get()));
        when(paymentFactory.getStrategy(PaymentType.PAYPAL)).thenReturn(null);
        when(paymentStrategy.processPayment(anyLong())).thenThrow(new RuntimeException("gateway timeout"));
        when(executor.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return CompletableFuture.completedFuture(null);
        });

        PaymentRequest request = new PaymentRequest(
                null,
                "PLN",
                new BigDecimal("13.50"),
                "900",
                PaymentType.PAYPAL,
                null
        );

        paymentService.pay(request);

        assertEquals(PaymentStatus.FAILED, storedPayment.get().getPaymentStatus());
    }*/

    @Test
    void shouldNormalizeCurrencyToUpperCase() {
        AtomicReference<Payment> storedPayment = new AtomicReference<>();
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            if (payment.getId() == null) {
                payment.setId(1L);
            }
            storedPayment.set(payment);
            return payment;
        });
        when(executor.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return CompletableFuture.completedFuture(null);
        });

        PaymentRequest request = new PaymentRequest(
                null,
                "PLN",
                new BigDecimal("4.99"),
                "42",
                TEST_EMAIL,
                ORDER_ID,
                PaymentType.BLIK,
                PaymentStatus.PAID
        );

        paymentService.pay(request);

        verify(rateClient, never()).getCurrencyRate(any());
        assertEquals(PaymentStatus.IN_PROGRESS, storedPayment.get().getPaymentStatus());
    }
}

