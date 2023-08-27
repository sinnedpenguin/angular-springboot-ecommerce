package com.angularspringbootecommerce.backend.configuration;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public StripeConfig() {
        Stripe.apiKey = stripeSecretKey;
    }
}
