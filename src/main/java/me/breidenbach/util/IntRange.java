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

    public static Stream<Integer> intStream(int start, int end) {
        return new IntRange(start, end).stream();
    }

    public static Stream<Integer> intStream(int start, int end, int step) {
        return new IntRange(start, end, step).stream();
    }

    public IntRange(int start, int end) {
        this(start, end, 1);
    }

    public IntRange(int start, int end, int step) {
        super(start, end, step, step > 0);
    }

    @Override
    Integer nextValue(Integer current, Integer step) {
        return current + step;
    }

    @Override
    boolean lessThanEqualTo(Integer left, Integer right) {
        return left <= right;
    }

    @Override
    boolean greaterThanEqualTo(Integer left, Integer right) {
        return left >= right;
    }

    @Override
    int countRemaining(Integer current, Integer end, Integer step) {
        return ((end - current) / step) + 1;
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
}
