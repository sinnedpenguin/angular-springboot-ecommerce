package com.angularspringbootecommerce.backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private double price;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}