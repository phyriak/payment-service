package com.phyriak.controller;

import com.phyriak.dto.PaymentApiResponse;
import com.phyriak.dto.PaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/payments")
public interface PaymentController {

    //Swagger description
    @Operation(summary = "Get payment by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/")
    ResponseEntity<PaymentApiResponse> getPayment();

    @GetMapping("/{id}")
    ResponseEntity<PaymentApiResponse> getPayment(@PathVariable Long id);

    @PostMapping("/")
    ResponseEntity<PaymentApiResponse> pay(@RequestBody PaymentRequest paymentRequest);


    @GetMapping("/test")
    String test();

}
