package com.shopease.service.impl;

import com.shopease.exception.ResourceNotFoundException;
import com.shopease.model.*;
import com.shopease.repository.OrderRepository;
import com.shopease.service.CartService;
import com.shopease.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Override
    public Order placeOrder(Long userId, String shippingAddress, String paymentMethod) {
        Cart cart = cartService.getOrCreateCart(userId);

        if (cart.isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Please add items before placing an order.");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(Order.OrderStatus.PENDING);

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (!product.isAvailable()) {
                throw new IllegalStateException("Product '" + product.getName() + "' is no longer available.");
            }
            if (cartItem.getQuantity() > product.getStock()) {
                throw new IllegalStateException("Insufficient stock for '" + product.getName() +
                        "'. Available: " + product.getStock());
            }
            OrderItem orderItem = new OrderItem(order, product, cartItem.getQuantity(), product.getPrice());
            order.addOrderItem(orderItem);
            product.reduceStock(cartItem.getQuantity());
        }

        order.setTotalAmount(cart.getTotalPrice());
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findByIdWithItems(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Order updateStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to cancel this order.");
        }
        if (!order.isCancellable()) {
            throw new IllegalStateException("Order cannot be cancelled in '" + order.getStatus().getDisplayName() + "' status.");
        }

        for (OrderItem item : order.getOrderItems()) {
            item.getProduct().increaseStock(item.getQuantity());
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
}
