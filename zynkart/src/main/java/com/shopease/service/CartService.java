package com.shopease.service;

import com.shopease.model.Cart;

public interface CartService {

    Cart getOrCreateCart(Long userId);

    Cart addToCart(Long userId, Long productId, int quantity);

    Cart updateQuantity(Long userId, Long cartItemId, int quantity);

    Cart removeFromCart(Long userId, Long cartItemId);

    void clearCart(Long userId);

    int getCartItemCount(Long userId);
}
