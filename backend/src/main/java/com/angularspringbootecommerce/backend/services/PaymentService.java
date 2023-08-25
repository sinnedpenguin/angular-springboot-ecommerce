package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.dtos.ChargeDto;
import com.angularspringbootecommerce.backend.dtos.PaymentDto;
import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.models.Order;
import com.angularspringbootecommerce.backend.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;

    public ChargeDto processPayment(PaymentDto payment) {
        try {
            Charge stripeCharge = Charge.create(payment.toChargeParams());

            if (stripeCharge.getStatus().equals("succeeded")) {
                Long orderId = ((Map<String, Long>) payment.getMetadata()).get("orderId");
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND));
                order.setStatus("paid");
                orderRepository.save(order);
            }

            return new ChargeDto(stripeCharge.getId(), stripeCharge.getAmount(), stripeCharge.getCurrency(), stripeCharge.getStatus());
        } catch (StripeException e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
