package com.shopease.service.impl;

import com.shopease.exception.ResourceNotFoundException;
import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.model.Product;
import com.shopease.model.User;
import com.shopease.repository.CartItemRepository;
import com.shopease.repository.CartRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.UserRepository;
import com.shopease.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserIdWithItems(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!product.isAvailable()) {
            throw new IllegalStateException("Product '" + product.getName() + "' is out of stock.");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQty = item.getQuantity() + quantity;
            if (newQty > product.getStock()) {
                throw new IllegalStateException("Only " + product.getStock() + " units available.");
            }
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        } else {
            if (quantity > product.getStock()) {
                throw new IllegalStateException("Only " + product.getStock() + " units available.");
            }
            CartItem item = new CartItem(cart, product, quantity);
            cartItemRepository.save(item);
        }

        return cartRepository.findByUserIdWithItems(userId).orElse(cart);
    }

    @Override
    public Cart updateQuantity(Long userId, Long cartItemId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalStateException("Item does not belong to your cart.");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            if (quantity > item.getProduct().getStock()) {
                throw new IllegalStateException("Only " + item.getProduct().getStock() + " units available.");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return cartRepository.findByUserIdWithItems(userId).orElse(cart);
    }

    @Override
    public Cart removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalStateException("Item does not belong to your cart.");
        }

        cartItemRepository.delete(item);
        return cartRepository.findByUserIdWithItems(userId).orElse(cart);
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public int getCartItemCount(Long userId) {
        return cartRepository.findByUserIdWithItems(userId)
                .map(Cart::getTotalItems)
                .orElse(0);
    }
}
