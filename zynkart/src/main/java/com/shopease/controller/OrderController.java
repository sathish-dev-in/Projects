package com.shopease.controller;

import com.shopease.exception.ResourceNotFoundException;
import com.shopease.model.Cart;
import com.shopease.model.Order;
import com.shopease.model.User;
import com.shopease.service.CartService;
import com.shopease.service.OrderService;
import com.shopease.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        Cart cart = cartService.getOrCreateCart(user.getId());
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/order/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             @RequestParam String paymentMethod,
                             HttpSession session,
                             RedirectAttributes redirectAttrs) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        try {
            Order order = orderService.placeOrder(user.getId(), shippingAddress, paymentMethod);
            SessionUtil.setCartCount(session, 0);
            redirectAttrs.addFlashAttribute("successMsg", "Order #" + order.getId() + " placed successfully!");
            redirectAttrs.addFlashAttribute("orderId", order.getId());
            return "redirect:/orders/" + order.getId();
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/orders")
    public String myOrders(Model model, HttpSession session) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        List<Order> orders = orderService.findByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        Order order = orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        if (!order.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order-detail";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        try {
            orderService.cancelOrder(id, user.getId());
            redirectAttrs.addFlashAttribute("successMsg", "Order #" + id + " cancelled successfully.");
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }
}
