package com.shopease.service;

import com.shopease.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order placeOrder(Long userId, String shippingAddress, String paymentMethod);

    Optional<Order> findById(Long id);

    List<Order> findByUserId(Long userId);

    List<Order> findAll();

    Order updateStatus(Long orderId, Order.OrderStatus newStatus);

    Order cancelOrder(Long orderId, Long userId);

    long countByStatus(Order.OrderStatus status);
}
