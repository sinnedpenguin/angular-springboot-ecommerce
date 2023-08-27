package com.angularspringbootecommerce.backend.controllers;

import com.angularspringbootecommerce.backend.dtos.CartDto;
import com.angularspringbootecommerce.backend.dtos.ChargeDto;
import com.angularspringbootecommerce.backend.dtos.PaymentDto;
import com.angularspringbootecommerce.backend.models.Order;
import com.angularspringbootecommerce.backend.services.CartService;
import com.angularspringbootecommerce.backend.services.OrderService;
import com.angularspringbootecommerce.backend.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;

    @GetMapping("/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId, Authentication authentication) {
        return orderService.getOrdersByUserId(userId, authentication);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<?> checkout(@PathVariable Long userId, @RequestBody PaymentDto payment) {
        CartDto cartDto = cartService.getCartByUserId(userId);
        Order order = orderService.createOrderFromCart(cartDto);
        payment.setMetadata(Map.of("orderId", order.getId()));
        ChargeDto charge = paymentService.processPayment(payment);
        if (charge.getStatus().equals("succeeded")) {
            order.setStatus("paid");
            orderService.save(order);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
        }
    }
}
