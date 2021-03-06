package me.breidenbach.util;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/13/17
 */
@SuppressWarnings("WeakerAccess")
public class Failure<T> extends Try<T> {
    public Failure(final Throwable failure) {
        super(failure);
    }
}
