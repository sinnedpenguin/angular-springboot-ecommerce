package com.angularspringbootecommerce.backend.controllers;

import com.angularspringbootecommerce.backend.dtos.CartDto;
import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
        CartDto cartDto = cartService.getCartByUserId(userId);
        if (cartDto != null) {
            return ResponseEntity.ok().body(cartDto);
        } else {
            throw new AppException("User's cart not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/{productId}/{quantity}")
    public ResponseEntity<CartDto> addToCart(@PathVariable Long userId, @PathVariable Long productId, @PathVariable int quantity) {
        CartDto cartDto = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok().body(cartDto);
    }
}
