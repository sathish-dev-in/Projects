package com.shopease.controller;

import com.shopease.dto.ProductDTO;
import java.util.stream.Collectors;
import com.shopease.exception.ResourceNotFoundException;
import com.shopease.model.Order;
import com.shopease.model.Product;
import com.shopease.service.CategoryService;
import com.shopease.service.OrderService;
import com.shopease.service.ProductService;
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
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(ProductService productService, CategoryService categoryService,
                           OrderService orderService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.userService = userService;
    }

    private String checkAdmin(HttpSession session) {
        if (!SessionUtil.isLoggedIn(session)) return "redirect:/login";
        if (!SessionUtil.isAdmin(session)) return "redirect:/";
        return null;
    }

    // ── Dashboard ────────────────────────────────────────────────────────────

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, HttpSession session) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;

        model.addAttribute("totalProducts", productService.findAll().size());
        model.addAttribute("totalOrders", orderService.findAll().size());
        model.addAttribute("totalUsers", userService.findAllUsers().size());
        model.addAttribute("pendingOrders", orderService.countByStatus(Order.OrderStatus.PENDING));
        model.addAttribute("recentOrders", orderService.findAll().stream().limit(5).collect(Collectors.toList()));
        return "admin/dashboard";
    }

    // ── Products ─────────────────────────────────────────────────────────────

    @GetMapping("/products")
    public String manageProducts(Model model, HttpSession session) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        model.addAttribute("products", productService.findAll());
        return "admin/manage-products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model, HttpSession session) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products/add")
    public String saveProduct(@Valid @ModelAttribute("productDTO") ProductDTO dto,
                              BindingResult bindingResult, Model model,
                              HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product-form";
        }
        try {
            productService.save(dto);
            redirectAttrs.addFlashAttribute("successMsg", "Product added successfully!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, HttpSession session) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        Product product = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        ProductDTO dto = new ProductDTO();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        model.addAttribute("productDTO", dto);
        model.addAttribute("productId", id);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("productDTO") ProductDTO dto,
                                BindingResult bindingResult, Model model,
                                HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("productId", id);
            return "admin/product-form";
        }
        try {
            productService.update(id, dto);
            redirectAttrs.addFlashAttribute("successMsg", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        productService.delete(id);
        redirectAttrs.addFlashAttribute("successMsg", "Product deactivated.");
        return "redirect:/admin/products";
    }

    // ── Categories ───────────────────────────────────────────────────────────

    @GetMapping("/categories")
    public String manageCategories(Model model, HttpSession session) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        model.addAttribute("categories", categoryService.findAll());
        return "admin/manage-categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        try {
            categoryService.save(name, description);
            redirectAttrs.addFlashAttribute("successMsg", "Category '" + name + "' added.");
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        try {
            categoryService.delete(id);
            redirectAttrs.addFlashAttribute("successMsg", "Category deleted.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", "Cannot delete category with existing products.");
        }
        return "redirect:/admin/categories";
    }

    // ── Orders ───────────────────────────────────────────────────────────────

    @GetMapping("/orders")
    public String manageOrders(Model model, HttpSession session) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("orderStatuses", Order.OrderStatus.values());
        return "admin/manage-orders";
    }

    @PostMapping("/orders/status/{id}")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam String status,
                                    HttpSession session,
                                    RedirectAttributes redirectAttrs) {
        String redirect = checkAdmin(session);
        if (redirect != null) return redirect;
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);
            orderService.updateStatus(id, newStatus);
            redirectAttrs.addFlashAttribute("successMsg", "Order #" + id + " status updated.");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("errorMsg", "Invalid status value.");
        }
        return "redirect:/admin/orders";
    }
}
