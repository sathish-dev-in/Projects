package com.autonest.functional;

import java.util.function.Function;

/**
 * Custom functional interface for entity-to-DTO mapping.
 * Demonstrates custom functional interface with default andThen() composition method.
 *
 * @param <E> Entity type
 * @param <D> DTO / Response type
 */
@FunctionalInterface
public interface EntityMapper<E, D> {

    /**
     * Maps an entity to a DTO.
     *
     * @param entity the source entity
     * @return the mapped DTO
     */
    D map(E entity);

    /**
     * Composes this mapper with a downstream function.
     * Enables pipeline-style transformation: mapper.andThen(dto -> transform(dto))
     *
     * @param after the function to apply after mapping
     * @param <R>   the result type of the composed function
     * @return a composed mapper
     */
    default <R> EntityMapper<E, R> andThen(Function<D, R> after) {
        return entity -> after.apply(this.map(entity));
    }

    /**
     * Creates a mapper that applies this mapper and passes the result to another mapper.
     *
     * @param before mapper applied before this one
     * @param <V>    input type of the before mapper
     * @return composed mapper
     */
    default <V> EntityMapper<V, D> compose(EntityMapper<V, E> before) {
        return entity -> this.map(before.map(entity));
    }
}
