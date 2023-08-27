package com.angularspringbootecommerce.backend.dtos;

import lombok.Data;

@Data
public class ChargeDto {
    private String id;
    private Long amount;
    private String currency;
    private String status;

    public ChargeDto(String id, Long amount, String currency, String status) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}
