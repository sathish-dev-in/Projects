package com.shopease.controller;

import com.shopease.model.Cart;
import com.shopease.model.User;
import com.shopease.service.CartService;
import com.shopease.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        Cart cart = cartService.getOrCreateCart(user.getId());
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttrs) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        try {
            Cart cart = cartService.addToCart(user.getId(), productId, quantity);
            SessionUtil.setCartCount(session, cart.getTotalItems());
            redirectAttrs.addFlashAttribute("successMsg", "Item added to cart successfully!");
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/products/" + productId;
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        try {
            Cart cart = cartService.updateQuantity(user.getId(), cartItemId, quantity);
            SessionUtil.setCartCount(session, cart.getTotalItems());
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        User user = SessionUtil.getLoggedInUser(session);
        try {
            Cart cart = cartService.removeFromCart(user.getId(), cartItemId);
            SessionUtil.setCartCount(session, cart.getTotalItems());
            redirectAttrs.addFlashAttribute("successMsg", "Item removed from cart.");
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/cart";
    }
}
