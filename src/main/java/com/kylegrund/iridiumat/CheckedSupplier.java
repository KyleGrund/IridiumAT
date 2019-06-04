package com.kylegrund.iridiumat;

/**
 * Represents a supplier of results which can throw an Exception.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <E> the type of results supplied by this supplier
 */
@FunctionalInterface
public interface CheckedSupplier<T, E extends Exception> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws E;
}