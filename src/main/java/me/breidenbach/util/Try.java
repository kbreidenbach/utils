package me.breidenbach.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 9/30/17
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class Try<T> {

    private T payload;
    private Throwable failure;

    public static <T> Try<T> tryRun(TryRunnable<? extends T> supplier) {
        try {
            final T payload = supplier.run();
            return new Success<>(payload);
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    private Try(TryRunnable<? extends T> supplier) {
        try {
            this.payload = supplier.run();
        } catch (Throwable throwable) {
            failure = throwable;
        }
    }

    Try(T payload) {
        this.payload = payload;
    }

    Try(Throwable failure) {
        this.failure = failure;
    }

    public Try<T> onSuccess(Consumer<? super T> successHandler) {
        if (failure == null) {
            successHandler.accept(payload);
        }

        return this;
    }

    public Try<T> onFailure(Consumer<Throwable> failureHandler) {
        if (failure != null) {
            failureHandler.accept(failure);
        }

        return this;
    }

    public Optional<T> get() {
        return failure == null ? Optional.ofNullable(payload) : Optional.empty();
    }

    public Optional<Throwable> getError() {
        return Optional.ofNullable(failure);
    }

    public Optional<T> getOrElse(T defaultValue) {
        return failure == null ? Optional.ofNullable(payload) : Optional.ofNullable(defaultValue);
    }

    public <U> Try<U> map(Function<T, U> function) {
        return failure == null ? tryRun(() -> function.apply(payload)) : new Failure<>(failure);
    }

    public <U> U fold(Function<Throwable, U> failureHandler, Function<T, U> successHandler) {
        return failure == null ? successHandler.apply(payload) : failureHandler.apply(failure);
    }

    public Try<T> then(Consumer<Throwable> failureHandler, Consumer<? super T> successHandler) {
        onSuccess(successHandler).onFailure(failureHandler);
        return this;
    }

    public Stream<T> stream() {
        return failure == null ?  Stream.of(payload) : Stream.empty();
    }

    public Try<T> forEach(Consumer<? super T> successHandler) {
        stream().forEach(successHandler);
        return this;
    }

    public boolean isFailure() {
        return failure != null;
    }

    public boolean isSuccess() {
        return failure == null;
    }

    @FunctionalInterface
    public interface TryRunnable<T> {
        T run() throws Throwable;
    }
}