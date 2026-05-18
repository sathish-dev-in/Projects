package com.autonest.service;

import com.autonest.dto.response.GenericResponse;

import java.util.List;

/**
 * Generic base service interface.
 * Demonstrates Generics with a generic interface and default interface methods.
 *
 * @param <T>  Entity type
 * @param <R>  Request DTO type
 * @param <ID> ID type
 */
public interface BaseService<T, R, ID> {

    GenericResponse<T> create(R request);

    GenericResponse<T> findById(ID id);

    GenericResponse<List<T>> findAll();

    GenericResponse<T> update(ID id, R request);

    GenericResponse<Void> delete(ID id);

    /**
     * Default method — generates a human-readable operation message.
     * Demonstrates default interface methods.
     */
    default String generateMessage(String entityName, String action) {
        return entityName + " " + action + " successfully";
    }

    /**
     * Default method — builds a paginated description string.
     */
    default String buildListMessage(String entityName, int count) {
        return "Found " + count + " " + entityName + (count == 1 ? "" : "s");
    }
}
