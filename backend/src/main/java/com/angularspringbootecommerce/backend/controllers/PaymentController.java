package com.angularspringbootecommerce.backend.controllers;

import com.angularspringbootecommerce.backend.dtos.ChargeDto;
import com.angularspringbootecommerce.backend.dtos.PaymentDto;
import com.angularspringbootecommerce.backend.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ChargeDto> processPayment(@RequestBody PaymentDto payment) {
        ChargeDto charge = paymentService.processPayment(payment);
        return new ResponseEntity<>(charge, HttpStatus.OK);
    }
}