package me.breidenbach.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/1/17
 */
@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
public class ForComp {

    public static <T, R> ForFunction<T, R> forFunction(NFunction<T, R> function, int...indexes) {
        return new ForFunction<>(function, indexes);
    }

    public static <T> ForComprehension with(ForFunction<T, ?> function, List<T> nextIterable) {
        return new ForComprehension().with(function, nextIterable);
    }

    public static <T> ForComprehension with(List<T> nextIterable) {
        return new ForComprehension().with(nextIterable);
    }

    public static <T> ForComprehension with(T... nextItems) {
        return new ForComprehension().with(nextItems);
    }

    public static <T> ForComprehension with(ForFunction<T, ?> function, T... nextItems) {
        return new ForComprehension().with(function, nextItems);
    }

    public static class ForFunction<T, R> {
        private final NFunction<T, R> function;
        private final int[] indexes;

        private ForFunction(NFunction<T, R> function, int[] indexes) {
            this.function = function;
            this.indexes = indexes;
        }

        private int[] getIndexes() {
            return indexes;
        }

        private R apply(List<T> data) {
            if (data.size() - 1 != indexes.length) throw new IllegalArgumentException("incorrect number of data points");
            return function.apply(data);
        }
    }

    public static class ForComprehension {
        private final List<List> iterables = new ArrayList<>();
        private final List<ForFunction> functions = new ArrayList<>();

        private ForComprehension() {
        }

        public <T> ForComprehension with(ForFunction<T, ?> function, List<T> nextIterable) {
            functions.add(function);
            iterables.add(nextIterable);
            return this;
        }

        public <T> ForComprehension with(List<T> nextIterable) {
            return with(null, nextIterable);
        }

        @SafeVarargs
        public final <T> ForComprehension with(T... nextItems) {
            return with(null, List.of(nextItems));
        }

        @SafeVarargs
        public final <T> ForComprehension with(ForFunction<T, ?> function, T... nextItems) {
            return with(function, List.of(nextItems));
        }

        public <T> ForComprehension iff(Predicate<T> predicate) {
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

        public Stream<?> yield() {
            return yield(null);
        }

        public Stream<?> yield(Function function) {
            switch (iterables.size()) {
                case 0 : return Stream.empty();
                default : {
                    final Stream<?> stream = mapStream(handleIterables().stream());
                    if (function != null) {
                        return stream.map(function);
                    } else {
                        return stream;
                    }
                }
            }
        }

        private Stream<?> mapStream(Stream<List> stream) {
            return stream.filter(Objects::nonNull).map(this::processResult);
        }

        private Object processResult(List data) {
            final List result = new ArrayList<> (data.size());
            for (int i = 0; i < data.size(); i++) {
                if (functions.get(i) != null) result.add(data.get(i));
            }
            if (result.size() == 1) {
                return result.get(0);
            }
            else {
                return result;
            }
        }

        private List<List> handleIterables() {
            final List<Iterable> iter = new ArrayList<>();
            final List<List> result = new ArrayList<>();
            final List row = new ArrayList(iterables.size());

            for (int i = 0; i < iterables.size(); i++) row.add(null);

            handleIterables(iterables, result, row, 0, 0);

            return result;
        }

        private void handleIterables(List<List> iterables, List<List> results, List row, int index, int functionIndex) {
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
                final int[] indexes = forFunction.indexes;
                final List dataPoints = new ArrayList<>(1 + indexes.length);

                dataPoints.add(item);

                for (int i : indexes) dataPoints.add(row.get(i));

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
