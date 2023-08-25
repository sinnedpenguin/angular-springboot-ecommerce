package com.angularspringbootecommerce.backend.dtos;

import lombok.Data;

@Data
public class ChargeDto {
    private String token;
    private double amount;
    private String currency;
    private String description;
    private String chargedTo;
    public ChargeDto(String id, Long amount, String currency, String status) {
    }
}