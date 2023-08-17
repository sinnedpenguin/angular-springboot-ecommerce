package com.angularspringbootecommerce.backend.dtos;

import com.angularspringbootecommerce.backend.models.CartItem;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}