package com.angularspringbootecommerce.backend.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public PaymentIntent createPaymentIntent(BigDecimal amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
        params.put("currency", "usd");
        params.put("payment_method_types", Collections.singletonList("card"));

        return PaymentIntent.create(params);
    }
}