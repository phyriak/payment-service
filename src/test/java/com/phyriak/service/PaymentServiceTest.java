package com.phyriak.service;

import com.phyriak.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

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