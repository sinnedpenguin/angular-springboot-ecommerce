package com.angularspringbootecommerce.backend.dtos;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private double price;
    private String imgUrl;
}
