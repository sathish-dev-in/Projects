package com.shopease.controller;

import com.shopease.exception.ResourceNotFoundException;
import com.shopease.model.Product;
import com.shopease.service.CategoryService;
import com.shopease.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long categoryId,
                               Model model) {
        List<Product> products = productService.searchProducts(keyword, categoryId);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        return "products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        model.addAttribute("product", product);
        return "product-detail";
    }
}
