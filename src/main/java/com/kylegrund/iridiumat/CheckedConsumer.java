package com.kylegrund.iridiumat;

import java.util.Objects;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result which can throw an Exception. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 * @param <E> the type of the Exception the operation can throw
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws E;
}