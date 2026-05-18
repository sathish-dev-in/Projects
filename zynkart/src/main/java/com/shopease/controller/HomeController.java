package com.shopease.controller;

import com.shopease.service.CategoryService;
import com.shopease.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("latestProducts", productService.findLatest());
        model.addAttribute("categories", categoryService.findAll());
        return "home";
    }
}
