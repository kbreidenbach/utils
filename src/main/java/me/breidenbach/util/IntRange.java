package me.breidenbach.util;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class IntRange extends BaseNumberRange<Integer> {

    public static Stream<Integer> intStream(final int start, final int end) {
        return new IntRange(start, end).stream();
    }

    public static Stream<Integer> intStream(final int start, final int end, final int step) {
        return new IntRange(start, end, step).stream();
    }

    public IntRange(final int start, final int end) {
        this(start, end, 1);
    }

    public IntRange(final int start, final int end, final int step) {
        super(start, end, step, step > 0);
    }

    @Override
    public Integer[] generateRest() {
        final Integer[] rest = new Integer[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    public int[] generateIntRest() {
        final int[] rest = new int[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    @Override
    public List<Integer> generateRestList() {
        return List.of(generateRest());
    }

    @Override
    public Stream<Integer> stream() {
        return generateRestList().stream();
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

    @Override
    Integer nextValue(final Integer current, final Integer step) {
        return current + step;
    }

    @Override
    boolean lessThanEqualTo(final Integer left, final Integer right) {
        return left <= right;
    }

    @Override
    boolean greaterThanEqualTo(final Integer left, final Integer right) {
        return left >= right;
    }

    @Override
    int countRemaining(final Integer current, final Integer end, final Integer step) {
        return ((end - current) / step) + 1;
    }
}
