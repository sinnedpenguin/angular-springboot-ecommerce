package com.angularspringbootecommerce.backend.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private List<CartItemDto> cartItems;
}