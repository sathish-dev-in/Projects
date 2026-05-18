package com.shopease.service;

import com.shopease.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Category save(String name, String description);

    void delete(Long id);
}
