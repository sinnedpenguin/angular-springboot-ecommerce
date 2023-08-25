package com.angularspringbootecommerce.backend.dtos;

import com.stripe.param.ChargeCreateParams;
import lombok.Data;

@Data
public class PaymentDto {
    private String tokenId;
    private Integer amount;
    private String currency;
    private Object metadata;
    public ChargeCreateParams toChargeParams() {

        return new ChargeCreateParams.Builder()
                .setAmount(Long.valueOf(amount))
                .setCurrency(currency)
                .setDescription("Payment from Stripe Checkout")
                .setSource(tokenId)
                .build();
    }
}
