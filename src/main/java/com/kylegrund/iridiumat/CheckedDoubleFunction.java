package com.kylegrund.iridiumat;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result which can throw an Exception. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <T> the type of the first input to the operation
 * @param <U> the type of the second input to the operation
 * @param <R> the type of the return
 * @param <E> the type of the Exception the operation can throw
 */
@FunctionalInterface
public interface CheckedDoubleFunction<T, U, R, E extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the first input argument
     * @param u the second input argument.
     */
    R accept(T t, U u) throws E;
}