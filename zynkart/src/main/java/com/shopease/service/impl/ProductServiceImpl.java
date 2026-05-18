package com.shopease.service.impl;

import com.shopease.dto.ProductDTO;
import com.shopease.exception.ResourceNotFoundException;
import com.shopease.model.Category;
import com.shopease.model.Product;
import com.shopease.repository.CategoryRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllActive() {
        return productRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword, Long categoryId) {
        if (StringUtils.hasText(keyword) && categoryId != null) {
            return productRepository.searchByKeywordAndCategory(keyword.trim(), categoryId);
        } else if (StringUtils.hasText(keyword)) {
            return productRepository.searchByKeyword(keyword.trim());
        } else if (categoryId != null) {
            return productRepository.findByCategoryIdAndActiveTrue(categoryId);
        }
        return productRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findLatest() {
        return productRepository.findTop8ByActiveTrueOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(ProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
        Product product = new Product();
        mapDtoToProduct(dto, product, category);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
        mapDtoToProduct(dto, product, category);
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void toggleStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setActive(!product.isActive());
        productRepository.save(product);
    }

    private void mapDtoToProduct(ProductDTO dto, Product product, Category category) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(StringUtils.hasText(dto.getImageUrl()) ? dto.getImageUrl() : null);
        product.setCategory(category);
        product.setActive(true);
    }
}
