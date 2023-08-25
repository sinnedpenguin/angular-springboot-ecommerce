package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.models.Order;
import com.angularspringbootecommerce.backend.models.User;
import com.angularspringbootecommerce.backend.repository.OrderRepository;
import com.angularspringbootecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<Order> getOrdersByUserId(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        if (authentication == null || !user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied.", HttpStatus.BAD_REQUEST);
        }
        return orderRepository.findAllByUserId(userId);
    }
}
