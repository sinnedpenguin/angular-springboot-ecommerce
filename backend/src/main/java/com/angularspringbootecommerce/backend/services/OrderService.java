package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.dtos.*;
import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.models.*;
import com.angularspringbootecommerce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<OrderDto> getOrdersByUserId(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        if (authentication == null || !user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied.", HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = orderRepository.findAllByUserId(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setTotal(order.getTotal());
            String dateCreatedStr = dateFormat.format(order.getDateCreated());
            orderDto.setDateCreated(dateCreatedStr);
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    public Order createOrderFromCart(CartDto cart, Long userId, Authentication authentication) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (authentication == null || !user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied.", HttpStatus.BAD_REQUEST);
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotal(cart.getTotalPrice());
        order.setDateCreated(new Date());
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDto cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

}