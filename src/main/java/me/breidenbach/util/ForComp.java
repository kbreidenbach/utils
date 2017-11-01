package me.breidenbach.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/1/17
 */
@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
public class ForComp {

    public static <T, R> ForFunction<T, R> forFunction(final NFunction<T, R> function) {
        return new ForFunction<>(function);
    }

    public static <T> ForComprehension<T> forComp(final List<T> nextIterable, final ForFunction<? super T, ?> function) {
        return new ForComprehension().with(nextIterable, function);
    }

    public static <T> ForComprehension<T> forComp(final List<T> nextIterable) {
        return new ForComprehension().with(nextIterable);
    }

    public static <T> ForComprehension<T> forComp(final Stream<T> stream, final ForFunction<? super T, ?> function) {
        return new ForComprehension().with(stream, function);
    }

    public static <T> ForComprehension<T> forComp(final Stream<T> stream) {
        return new ForComprehension().with(stream);
    }

    public static <T> ForComprehension<T> forComp(final T[] nextItems) {
        return new ForComprehension().with(nextItems);
    }

    public static <T> ForComprehension<T> forComp(final T[] nextItems, final ForFunction<? super T, ?> function) {
        return new ForComprehension().with(nextItems, function);
    }

    public static <T> ForComprehension<T> forComp(final Try<T> nextItem) {
        return new ForComprehension().with(nextItem);
    }

    public static <T> ForComprehension<T> forComp(final Try<T> nextItem,
                                                  final ForFunction<Try<? super T>, ?> function) {
        return new ForComprehension().with(nextItem, function);
    }

    public static class ForFunction<T, R> {
        private final NFunction<T, R> function;

        private ForFunction(final NFunction<T, R> function) {
            this.function = function;
        }

        private R apply(final List<T> data) {
            return function.apply(data);
        }
    }

    public static class ForComprehension<T> {
        private final List<List<T>> iterables = new ArrayList<>();
        private final List<ForFunction<? super T, ?>> functions = new ArrayList<>();

        private ForComprehension() {
        }

        public <R> ForComprehension<T> with(final List<T> nextIterable, final ForFunction<? super T, R> function) {
            functions.add(function);
            iterables.add(nextIterable);
            return this;
        }

        public ForComprehension<T> with(final List<T> nextIterable) {
            return with(nextIterable, null);
        }

        public <R> ForComprehension<T> with(final Stream<T> stream, final ForFunction<? super T, R> function) {
            return with(stream.collect(Collectors.toList()), function);
        }

        public ForComprehension<T> with(final Stream<T> stream) {
            return with(stream.collect(Collectors.toList()), null);
        }

        public final ForComprehension<T> with(final T[] nextItems) {
            return with(List.of(nextItems), null);
        }

        public final <R> ForComprehension<T> with(final T[] nextItems, final ForFunction<? super T, R> function) {
            return with(List.of(nextItems), function);
        }

        public final ForComprehension<T> with(final T nextItem) {
            return with(List.of(nextItem), null);
        }

        public final <R> ForComprehension<T> with(final T nextItem, final ForFunction<? super T, R> function) {
            return with(List.of(nextItem), function);
        }

        public ForComprehension<T> iff(final Predicate<? super T> predicate) {
            final int index = iterables.size() - 1;
            final List<T> list = iterables.get(index);
            final List<T> newList = new ArrayList(list.size());
            list.forEach(item -> {
                if (predicate.test(item)) {
                    newList.add(item);
                }
            });
            iterables.set(index, newList);
            return this;
        }

        public Stream<T> yieldFlat() {
            return yield().flatMap(l -> ((List<T>)l).stream());
        }

        public <R> Stream<R> yieldFlat(final Function<? super List<T>, R> function) {
            return yield(function).flatMap(l -> ((List<R>)l).stream());
        }

        public Stream yield(final int index, final int...indexes) {
            if (indexes.length == 0) {
                return yield(l -> l.get(index));
            } else {
                return yield(l -> {
                    final List<T> items = new ArrayList<>(indexes.length);
                    items.add(l.get(index));
                    for (final int i : indexes) items.add(l.get(i));
                    return items;
                });
            }
        }

        public <R> Stream<R> yield(final Function<? super List<T>, R> function, final int index, final int...indexes) {
            if (indexes.length == 0) {
                return yield(l -> function.apply(List.of(l.get(index))));
            } else {
                return yield(l -> {
                    final List<T> items = new ArrayList<>(indexes.length);
                    items.add(l.get(index));
                    for (final int i : indexes) items.add(l.get(i));
                    return function.apply(items);
                });
            }
        }

        public Stream<T> yield() {
            return yield((l) -> (T)l);
        }

        public <R> Stream<R> yield(final Function<? super List<T>, R> function) {
            switch (iterables.size()) {
                case 0 : return Stream.empty();
                default : {
                    final Stream<List<T>> stream = mapStream(handleIterables().stream());
                    return stream.map(function);
                }
            }
        }

        private Stream<List<T>> mapStream(final Stream<List<T>> stream) {
            return stream.filter(Objects::nonNull).map(this::processResult);
        }

        private List<T> processResult(final List<T> data) {
            final List<T> result = new ArrayList<>(data.size());
            result.addAll(data);
            return result;
        }

        private List<List<T>> handleIterables() {
            final List<Iterable<T>> iter = new ArrayList<>();
            final List<List<T>> result = new ArrayList<>();
            final List<?> row = new ArrayList(iterables.size());

            for (int i = 0; i < iterables.size(); i++) row.add(null);

            handleIterables(iterables, result, row, 0, 0);

            return result;
        }

        private void handleIterables(final List<List<T>> iterables, final List<List<T>> results, final List row,
                                     final int index, final int functionIndex) {
            for (final Object item : iterables.get(0)) {
                final ForFunction forFunction = functions.get(functionIndex);

                prepareItem(row, index, item, forFunction);

                if (iterables.size() > 1) {
                    handleIterables(iterables.subList(1, iterables.size()), results, row, index + 1, functionIndex + 1);
                } else {
                    final List newRow = new ArrayList();

                    newRow.addAll(row);

                    results.add(newRow);
                }
            }
        }

        private void prepareItem(final List row, final int index, final Object item, final ForFunction forFunction) {
            if (item instanceof Try) {
                final Try t = (Try)item;
                if (((Try) item).isSuccess()) {
                    ((Try) item).get().ifPresent(i -> processItem(row, index, i, forFunction));
                }
            } else {
                processItem(row, index, item, forFunction);
            }
        }

        private void processItem(final List row, final int index, final Object item, final ForFunction forFunction) {
            if (forFunction != null) {
                final List dataPoints = new ArrayList<>(1 + row.size());

                dataPoints.addAll(row.subList(0, index));
                dataPoints.add(item);

                row.set(index, forFunction.apply(dataPoints));
            } else {
                row.set(index, item);
            }
        }
    }

    @FunctionalInterface
    public interface NFunction<T, R> {
        R apply(List<T> inputs);
    }
}