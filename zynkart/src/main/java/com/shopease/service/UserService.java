package com.shopease.service;

import com.shopease.dto.RegisterDTO;
import com.shopease.model.User;

import java.util.List;
import java.util.Optional;

/**
 * UserService interface — demonstrates OOP Abstraction & Polymorphism.
 */
public interface UserService {

    User register(RegisterDTO dto);

    Optional<User> login(String username, String password);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllUsers();

    User updateProfile(Long userId, User updatedUser);
}
