package com.angularspringbootecommerce.backend;

import com.angularspringbootecommerce.backend.dtos.PaymentDto;
import com.angularspringbootecommerce.backend.services.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StripeIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @BeforeAll
    static void setupTest() {
        Stripe.apiKey = "sk_test_51NiZKsLtTFoBOb8wTg4V2C3bJqrQ53qMHLusPXct6hp0O3yNtHXfEITKl7bLHNPRG7Egk28A5d1X7H4yoCvUl5kF00OP3VpgCr";
    }

    @Test
    void testChargeCreation() throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 1000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Test payment from Spring Boot");
        chargeParams.put("source", "tok_visa");

        Charge charge = Charge.create(chargeParams);

        Assertions.assertNotNull(charge);
        Assertions.assertEquals(charge.getStatus(), "succeeded");
    }
}