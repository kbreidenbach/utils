package me.breidenbach.util;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/14/17
 */
@SuppressWarnings("WeakerAccess")
public class Match {
    @SafeVarargs
    public static <T, R> R match(final T subject, final Case<T, R>...cases) throws MatchException {
        for(final Case<T, R> c : cases) {
            if (c.predicate.test(subject)) return c.closure.apply(subject);
        }

        throw new MatchException(String.format("MatchError %s (of class %s)", subject, subject.getClass().getName()));
    }

    public static <T, R> Case<T, R> matchCase(final Predicate<? super T> predicate,
                                              final Function<? super T, R> closure) {
        return new Case<>(predicate, closure);
    }

    public static <T, R> Case<T, R> defaultCase(final Function<? super T, R> closure) {
        return new Default<>(closure);
    }

    public static <T, R> Case<T, R> typeCase(final Class<? extends T> type, final Function<? super T, R> closure) {
        return new Type<>(type, closure);
    }

    public static class Default<T, R> extends Case<T, R> {
        public Default(final Function<? super T, R> closure) {
            super(x -> true, closure);
        }
    }

    public static class Type<T, R> extends Case<T, R> {
        public Type(final Class<? extends T> type, final Function<? super T, R> closure) {
            super(type::isInstance, closure);
        }
    }

    public static class Case<T, R> {
        private final Predicate<? super T> predicate;
        private final Function<? super T, R> closure;

        public Case(final Predicate<? super T> predicate, final Function<? super T, R> closure) {
            this.predicate = predicate;
            this.closure = closure;
        }
    }

    public static class MatchException extends RuntimeException {
        private MatchException(final String message) {
            super(message);
        }
    }
}