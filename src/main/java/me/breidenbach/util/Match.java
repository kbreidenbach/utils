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
    public static <T, R> R match(T subject, Case<T, R>...cases) throws MatchException {
        for(Case<T, R> c : cases) {
            if (c.predicate.test(subject)) return c.closure.apply(subject);
        }

        throw new MatchException(String.format("MatchError %s (of class %s)", subject, subject.getClass().getName()));
    }

    public static <T, R> Case<T, R> matchCase(Predicate<T> predicate, Function<? super T, R> closure) {
        return new Case<>(predicate, closure);
    }

    public static <T, R> Case<T, R> defaultCase(Function<? super T, R> closure) {
        return new Default<>(closure);
    }

    public static <T, R> Case<T, R> typeCase(Class<? extends T> type, Function<? super T, R> closure) {
        return new Type<>(type, closure);
    }

    public static class Default<T, R> extends Case<T, R> {
        public Default(Function<? super T, R> closure) {
            super(x -> true, closure);
        }
    }

    public static class Type<T, R> extends Case<T, R> {
        public Type(Class<? extends T> type, Function<? super T, R> closure) {
            super(type::isInstance, closure);
        }
    }

    public static class Case<T, R> {
        private final Predicate<T> predicate;
        private final Function<? super T, R> closure;

        public Case(Predicate<T> predicate, Function<? super T, R> closure) {
            this.predicate = predicate;
            this.closure = closure;
        }
    }

    public static class MatchException extends Exception {
        private MatchException(String message) {
            super(message);
        }
    }
}
