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

    public static <T, R> ForFunction<T, R> forFunction(NFunction<T, R> function) {
        return new ForFunction<>(function);
    }

    public static <T> ForComprehension<T> forComp(List<T> nextIterable, ForFunction<T, ?> function) {
        return new ForComprehension().with(nextIterable, function);
    }

    public static <T> ForComprehension<T> forComp(List<T> nextIterable) {
        return new ForComprehension().with(nextIterable);
    }

    public static <T> ForComprehension<T> forComp(Stream<T> stream, ForFunction<T, ?> function) {
        return new ForComprehension().with(stream, function);
    }

    public static <T> ForComprehension<T> forComp(Stream<T> stream) {
        return new ForComprehension().with(stream);
    }

    public static <T> ForComprehension<T> forComp(T[] nextItems) {
        return new ForComprehension().with(nextItems);
    }

    public static <T> ForComprehension<T> forComp(T[] nextItems, ForFunction<T, ?> function) {
        return new ForComprehension().with(nextItems, function);
    }

    public static <T> ForComprehension<T> forComp(Try<T> nextItem) {
        return new ForComprehension().with(nextItem);
    }

    public static <T> ForComprehension<T> forComp(Try<T> nextItem, ForFunction<Try<T>, ?> function) {
        return new ForComprehension().with(nextItem, function);
    }

    public static class ForFunction<T, R> {
        private final NFunction<T, R> function;

        private ForFunction(NFunction<T, R> function) {
            this.function = function;
        }

        private R apply(List<T> data) {
            return function.apply(data);
        }
    }

    public static class ForComprehension<T> {
        private final List<List<T>> iterables = new ArrayList<>();
        private final List<ForFunction<T, ?>> functions = new ArrayList<>();

        private ForComprehension() {
        }

        public <R> ForComprehension<T> with(List<T> nextIterable, ForFunction<T, R> function) {
            functions.add(function);
            iterables.add(nextIterable);
            return this;
        }

        public ForComprehension<T> with(List<T> nextIterable) {
            return with(nextIterable, null);
        }

        public <R> ForComprehension<T> with(Stream<T> stream, ForFunction<T, R> function) {
            return with(stream.collect(Collectors.toList()), function);
        }

        public ForComprehension<T> with(Stream<T> stream) {
            return with(stream.collect(Collectors.toList()), null);
        }

        public final ForComprehension<T> with(T[] nextItems) {
            return with(List.of(nextItems), null);
        }

        public final <R> ForComprehension<T> with(T[] nextItems, ForFunction<T, R> function) {
            return with(List.of(nextItems), function);
        }

        public final ForComprehension<T> with(T nextItem) {
            return with(List.of(nextItem), null);
        }

        public final <R> ForComprehension<T> with(T nextItem, ForFunction<T, R> function) {
            return with(List.of(nextItem), function);
        }

        public ForComprehension<T> iff(Predicate<T> predicate) {
            final int index = iterables.size() - 1;
            final List list = iterables.get(index);
            final List newList = new ArrayList(list.size());
            list.forEach(item -> {
                if (predicate.test((T)item)) {
                    newList.add(item);
                }
            });
            iterables.set(index, newList);
            return this;
        }

        public Stream<T> yieldFlat() {
            return yield().flatMap(l -> ((List<T>)l).stream());
        }

        public <R> Stream<R> yieldFlat(Function<List<T>, R> function) {
            return yield(function).flatMap(l -> ((List<R>)l).stream());
        }

        public Stream<T> yield() {
            return yield((l) -> (T)l);
        }

        public <R> Stream<R> yield(Function<List<T>, R> function) {
            switch (iterables.size()) {
                case 0 : return Stream.empty();
                default : {
                    final Stream<List<T>> stream = mapStream(handleIterables().stream());
                    return stream.map(function);
                }
            }
        }

        private Stream<List<T>> mapStream(Stream<List<T>> stream) {
            return stream.filter(Objects::nonNull).map(this::processResult);
        }

        private List<T> processResult(List<T> data) {
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

        private void handleIterables(List<List<T>> iterables, List<List<T>> results, List row, int index, int functionIndex) {
            for (Object item : iterables.get(0)) {
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

        private void prepareItem(List row, int index, Object item, ForFunction forFunction) {
            if (item instanceof Try) {
                final Try t = (Try)item;
                if (((Try) item).isSuccess()) {
                    ((Try) item).get().ifPresent(i -> processItem(row, index, i, forFunction));
                }
            } else {
                processItem(row, index, item, forFunction);
            }
        }

        private void processItem(List row, int index, Object item, ForFunction forFunction) {
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