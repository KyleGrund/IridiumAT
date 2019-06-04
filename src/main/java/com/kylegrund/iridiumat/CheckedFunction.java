package com.kylegrund.iridiumat;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result which can throw an Exception. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 * @param <R> the type of the return
 * @param <E> the type of the Exception the operation can throw
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    R accept(T t) throws E;
}