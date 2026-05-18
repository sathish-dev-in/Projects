package com.nexabank.account.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic base service interface.
 * Demonstrates Java 17 Generics with bounded type parameters.
 *
 * @param <T>  the entity type
 * @param <ID> the ID type
 */
public interface BaseService<T, ID> {

    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    void delete(ID id);
}
