package com.kylegrund.iridiumat;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result which can throw an Exception. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <T> the type of the input to the operation
 * @param <U> the type of the input to the operation
 * @param <E> the type of the Exception the operation can throw
 */
@FunctionalInterface
public interface CheckedDoubleConsumer<T, U, E extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the firtst input argument
     * @param u the second input argrument.
     */
    void accept(T t, U u) throws E;
}