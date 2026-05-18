package com.shopease.service;

import com.shopease.dto.ProductDTO;
import com.shopease.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAllActive();

    List<Product> findByCategory(Long categoryId);

    List<Product> searchProducts(String keyword, Long categoryId);

    List<Product> findLatest();

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Product save(ProductDTO dto);

    Product update(Long id, ProductDTO dto);

    void delete(Long id);

    void toggleStatus(Long id);
}
