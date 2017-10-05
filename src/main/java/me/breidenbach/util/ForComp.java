package me.breidenbach.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/1/17
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ForComp {

    private final List<Class<?>> classes = new ArrayList<>();
    private final List<List> iterables = new ArrayList<>();
    private final List<ForFunction> functions = new ArrayList<>();
    private final Function function;

    public static <T, R> ForFunction<T, R> forFunction(NFunction<T, R> function, int...indexes) {
        return new ForFunction<>(function, indexes);
    }

    public <T> ForComp(Function<T, ?> function, List<T> iterable) {
        this.function = function;
        this.iterables.add(iterable);
        if (!iterable.isEmpty()) classes.add(iterable.get(0).getClass());
    }

    @SafeVarargs
    public <T> ForComp(Function<T, ?> function, T ... items) {
        this(function, List.of(items));
    }

    public <T> ForComp with(ForFunction<T, ?> function, List nextIterable) {
        functions.add(function);
        iterables.add(nextIterable);
        return this;
    }

    public final ForComp with(Object[]...nextItems) {
        iterables.add(List.of(nextItems));
        return this;
    }

    public Stream<?> yield() {
        switch (iterables.size()) {
            case 0 : return Stream.empty();
            case 1 : return iterables.get(0).stream(); //.map(functions.get(0));
            default : return handleIterables().stream();
        }
    }

    private List<List> handleIterables() {
        final List<Iterable> iter = new ArrayList<>();
        final List<List> result = new ArrayList<>();
        final List row = new ArrayList(iterables.size());
        for (int i = 0; i < iterables.size(); i++) row.add(null);
        handleIterables(iterables, result, row, 0, -1);

        return result;
    }

    private void handleIterables(List<List> iterables, List<List> results, List row, int index, int functionIndex) {

        for (Object item : iterables.get(0)) {

            if (functionIndex == -1) {
                row.set(index, function.apply(item));
            } else {
                final ForFunction forFunction = functions.get(functionIndex);
                final int[] indexes = forFunction.indexes;
                final Object[] dataPoints = new Object[1 + indexes.length];
                dataPoints[0] = item;
                for (int i = 0; i < indexes.length; i++) dataPoints[index + 1] = row.get(i);
                row.set(index, forFunction.apply(dataPoints));
            }

            if (iterables.size() > 1) {
                handleIterables(iterables.subList(1, iterables.size()), results, row, index + 1, functionIndex + 1);
            } else {
                List newRow = new ArrayList();

                newRow.addAll(row);

                results.add(newRow);
            }
        }
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

        @SafeVarargs
        private R apply(T...data) {
            if (data.length - 1 != indexes.length) throw new IllegalArgumentException("incorrect number of data points");

            return function.apply(data);
        }
    }

    @FunctionalInterface
    public interface NFunction<T, R> {
        R apply(T...inputs);
    }
}
