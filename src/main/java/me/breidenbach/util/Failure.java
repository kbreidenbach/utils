package me.breidenbach.util;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/13/17
 */
@SuppressWarnings("WeakerAccess")
public class Failure<T> extends Try<T> {
    Failure(Throwable failure) {
        super(failure);
    }
}
