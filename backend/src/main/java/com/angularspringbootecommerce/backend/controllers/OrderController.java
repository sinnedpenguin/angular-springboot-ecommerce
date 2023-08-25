package com.angularspringbootecommerce.backend.controllers;

import com.angularspringbootecommerce.backend.models.Order;
import com.angularspringbootecommerce.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId, Authentication authentication) {
        return orderService.getOrdersByUserId(userId, authentication);
    }
}
