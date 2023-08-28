package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.dtos.CartDto;
import com.angularspringbootecommerce.backend.dtos.CartItemDto;
import com.angularspringbootecommerce.backend.dtos.OrderDto;
import com.angularspringbootecommerce.backend.dtos.ProductDto;
import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.models.Order;
import com.angularspringbootecommerce.backend.models.OrderItem;
import com.angularspringbootecommerce.backend.models.Product;
import com.angularspringbootecommerce.backend.models.User;
import com.angularspringbootecommerce.backend.repository.OrderRepository;
import com.angularspringbootecommerce.backend.repository.ProductRepository;
import com.angularspringbootecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setDateCreated(order.getDateCreated().toString());
        orderDto.setTotal(order.getTotal());
        return orderDto;
    }

    public Order createOrderFromCart(CartDto cart, Long userId) {
        Order order = new Order();

        order.setDateCreated(new Date());
        order.setTotal(cart.getTotalPrice());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto cartItemDto : cart.getCartItems()) {
            ProductDto productDto = cartItemDto.getProduct();
            Product product = productRepository.findById(productDto.getId())
                    .orElseThrow(() -> new AppException("Product not found.", HttpStatus.NOT_FOUND));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setPrice(cartItemDto.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        order = orderRepository.save(order);

        return order;
    }
}
