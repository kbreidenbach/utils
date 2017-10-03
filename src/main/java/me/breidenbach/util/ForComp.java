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

    private final List<List> iterables = new ArrayList<>();
    private final List<ForFunction> functions = new ArrayList<>();
    private final Function function;

    public static ForFunction forFunction(NFunction function, int...indexes) {
        return new ForFunction(function, indexes);
    }

    public ForComp(Function function, List iterable) {
        this.function = function;
        iterables.add(iterable);
    }

    public ForComp(Function function, Object ... items) {
        this.function = function;
        iterables.add(List.of(items));
    }

    public ForComp with(ForFunction function, List nextIterable) {
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

    public static class ForFunction {
        private final NFunction function;
        private final int[] indexes;

        private ForFunction(NFunction function, int[] indexes) {
            this.function = function;
            this.indexes = indexes;
        }

        private int[] getIndexes() {
            return indexes;
        }

        private Object apply(Object...data) {
            if (data.length - 1 != indexes.length) throw new IllegalArgumentException("incorrect number of data points");

            return function.apply(data);
        }
    }

    @FunctionalInterface
    public interface NFunction {
        Object apply(Object...inputs);
    }
}
