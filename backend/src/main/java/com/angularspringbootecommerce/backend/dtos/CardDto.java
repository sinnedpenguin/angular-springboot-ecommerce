package com.angularspringbootecommerce.backend.dtos;

import lombok.Data;

@Data
public class CardDto {
    private String number;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String name;
    private String addressCity;
    private String addressLine1;
    private String addressLine2;
    private String addressCountry;
    private String addressPostalCode;
}
