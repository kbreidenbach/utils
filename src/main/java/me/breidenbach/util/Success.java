package me.breidenbach.util;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/13/17
 */
@SuppressWarnings("WeakerAccess")
public class Success<T> extends Try<T> {
    public Success(final T payload) {
        super(payload);
    }
}
