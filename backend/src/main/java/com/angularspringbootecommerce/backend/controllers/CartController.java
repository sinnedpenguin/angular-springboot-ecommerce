package com.angularspringbootecommerce.backend.controllers;

import com.angularspringbootecommerce.backend.dtos.CartItemDto;
import com.angularspringbootecommerce.backend.models.Cart;
import com.angularspringbootecommerce.backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("items")
    public List<Cart> getAllItems() {
        return cartService.getAllItems();
    }

    @PostMapping("add-item")
    public Cart addToCart(@RequestBody CartItemDto cartItemDto) {
        Long userId = cartItemDto.getId();
        Long productId = cartItemDto.getProduct().getId();
        int quantity = cartItemDto.getQuantity();

        return cartService.addToCart(userId, productId, quantity);
    }
}
