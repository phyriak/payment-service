package com.phyriak.service;

import com.phyriak.config.NBPRateClient;
import com.phyriak.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.ExecutorService;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PaymentServiceTest {

    @Mock
    private PaymentProcessor paymentProcessor;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private NBPRateClient rateClient;
    @Mock
    private ExecutorService executor;
    private PaymentService paymentService;


    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, rateClient, executor, paymentProcessor);
    }

    @Test
    void pay() {
    }

    @Test
    void getAllPayments() {
    }

    @Test
    void getPaymentById() {
    }

    @Test
    void shouldHandleConcurrentUpdates() throws InterruptedException {
    /*    // given
        Long paymentId = 1L;

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.IN_PROGRESS);

        paymentRepository.save(payment);

        int threadCount = 5;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    ready.countDown();     // signal ready
                    start.await();        // wait for all threads

                    paymentService.pay(
                            new PaymentRequest(paymentId, null, null, null, null, PaymentStatus.SUCCESS)
                    );

                } catch (Exception e) {
                    // you can log if needed
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await(); // wait until all threads are ready
        start.countDown(); // 🔥 release all threads at once
        done.await(); // wait for all to finish

        // then
        Payment updated = paymentRepository.findById(paymentId).orElseThrow();

        assertEquals(PaymentStatus.SUCCESS, updated.getPaymentStatus());*/
    }
}