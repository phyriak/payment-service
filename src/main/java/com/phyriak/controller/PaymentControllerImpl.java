package com.phyriak.controller;

import com.phyriak.dto.PaymentApiResponse;
import com.phyriak.dto.PaymentDto;
import com.phyriak.dto.PaymentRequest;
import com.phyriak.mapper.PaymentMapper;
import com.phyriak.repository.model.Payment;
import com.phyriak.service.PaymentService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    private final PaymentService paymentService;
    private final MeterRegistry meterRegistry;


    @Override
    public ResponseEntity<PaymentApiResponse> getPayment() {

        Timer timer = Timer.builder("payment.processing")
                .publishPercentileHistogram()
                .register(meterRegistry);

        timer.record(paymentService::getAllPayments);


        return ResponseEntity.ok(new PaymentApiResponse(paymentService.getAllPayments(), ""));
    }

    @Override
    public ResponseEntity<PaymentApiResponse> getPayment(Long id) {
        Payment paymentById = paymentService.getPaymentById(id);
        PaymentDto dto = PaymentMapper.INSTANCE.paymentEntityToPaymentDto(paymentById);
        return ResponseEntity.ok(new PaymentApiResponse(Collections.singletonList(dto), ""));
    }

    @Override
    public ResponseEntity<PaymentApiResponse> pay(PaymentRequest paymentRequest) {
        paymentService.pay(paymentRequest);
        return ResponseEntity.accepted()
                .body(new PaymentApiResponse(Collections.emptyList(), ""));
    }

    @Override
    public String test() {
        return "WORKS";
    }

}
