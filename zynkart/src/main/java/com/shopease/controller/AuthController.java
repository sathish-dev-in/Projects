package com.shopease.controller;

import com.shopease.dto.RegisterDTO;
import com.shopease.model.User;
import com.shopease.service.CartService;
import com.shopease.service.UserService;
import com.shopease.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final CartService cartService;

    public AuthController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (SessionUtil.isLoggedIn(session)) return "redirect:/";
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttrs) {
        return userService.login(username, password).map(user -> {
            SessionUtil.setLoggedInUser(session, user);
            int count = cartService.getCartItemCount(user.getId());
            SessionUtil.setCartCount(session, count);
            if (user.isAdmin()) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }).orElseGet(() -> {
            redirectAttrs.addFlashAttribute("errorMsg", "Invalid username or password.");
            return "redirect:/login";
        });
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        if (SessionUtil.isLoggedIn(session)) return "redirect:/";
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO dto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!dto.isPasswordMatching()) {
            model.addAttribute("errorMsg", "Passwords do not match.");
            return "register";
        }
        try {
            userService.register(dto);
            redirectAttrs.addFlashAttribute("successMsg", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttrs) {
        SessionUtil.invalidate(session);
        redirectAttrs.addFlashAttribute("successMsg", "You have been logged out successfully.");
        return "redirect:/login";
    }
}
