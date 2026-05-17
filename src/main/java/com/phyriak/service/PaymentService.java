package com.phyriak.service;

import com.phyriak.config.NBPRateClient;
import com.phyriak.dto.NbpResponse;
import com.phyriak.dto.PaymentDto;
import com.phyriak.dto.PaymentRequest;
import com.phyriak.exceptions.CurrencyNotFoundError;
import com.phyriak.exceptions.PaymentIllegalStatus;
import com.phyriak.exceptions.PaymentNotFoundException;
import com.phyriak.mapper.PaymentMapper;
import com.phyriak.repository.PaymentRepository;
import com.phyriak.repository.model.Currency;
import com.phyriak.repository.model.Payment;
import com.phyriak.repository.model.PaymentStatus;
import com.phyriak.strategy.PaymentFactory;
import com.phyriak.strategy.PaymentStrategy;
import com.phyriak.strategy.model.PaymentResult;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final NBPRateClient rateClient;
    private final PaymentFactory paymentFactory;
    private final ExecutorService executor;

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 50)
    )
    @Transactional
    public void pay(PaymentRequest paymentRequest) {
        var paymentEntity = PaymentMapper.INSTANCE.paymentRequestToEntity(paymentRequest);

        if (!paymentRequest.currency().equals("PLN")) {
            setPaymentAmountIfIsNotInPLN(paymentRequest, paymentEntity);
        }
        log.info("Processing payment for userId: {}, amount: {}, currency: {}",
                paymentRequest.userId(), paymentRequest.amount(), paymentRequest.currency());

        paymentEntity.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        Payment payment = paymentRepository.save(paymentEntity);

        executor.submit(() -> processPaymentAsync(payment.getId()));
    }

    private void processPaymentAsync(Long paymentId) {
        log.info("Processing async payment {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id= " + paymentId + " not found!"));

        PaymentStrategy strategy = paymentFactory.getStrategy(payment.getPaymentType());
        if (strategy == null) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentIllegalStatus("Wrong Payment Type: " + payment.getPaymentType());
        }
        PaymentResult result = strategy.processPayment(payment.getId());

        if (result.success()) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            log.info("Payment success {}", paymentId);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            log.error("Payment failed: {}", payment.getId());
        }
        paymentRepository.save(payment);
    }

    private void setPaymentAmountIfIsNotInPLN(PaymentRequest paymentRequest, Payment payment) {
        BigDecimal amountInPln = calculateAmountInPln(paymentRequest.amount(), paymentRequest.currency());
        payment.setCurrency(Currency.PLN);
        payment.setAmount(amountInPln);
    }

    private BigDecimal calculateAmountInPln(@NotBlank BigDecimal amount, @NotBlank String currency) {
        NbpResponse.Rate rate = rateClient.getCurrencyRate(currency);
        if (rate.mid() == null) {
            log.error("Empty value in rate mid {}", rate);
            throw new CurrencyNotFoundError("Empty mid");
        }
        BigDecimal multiplied = amount.multiply(rate.mid());
        multiplied = multiplied.setScale(2, RoundingMode.HALF_UP);
        return multiplied;
    }

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper.INSTANCE::paymentEntityToPaymentDto)
                .toList();
    }


    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id: " + id + " does not exist!"));
    }
}
