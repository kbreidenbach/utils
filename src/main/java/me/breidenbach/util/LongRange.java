package me.breidenbach.util;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LongRange extends BaseNumberRange<Long> {

    public static Stream<Long> longStream(long start, long end) {
        return new LongRange(start, end).stream();
    }

    public static Stream<Long> intStream(long start, long end, long step) {
        return new LongRange(start, end, step).stream();
    }

    public LongRange(long start, long end) {
        this(start, end, 1);
    }

    public LongRange(long start, long end, long step) {
        super(start, end, step, step > 0);
    }

    @Override
    Long nextValue(Long current, Long step) {
        return current + step;
    }

    @Override
    boolean lessThanEqualTo(Long left, Long right) {
        return left <= right;
    }

    @Override
    boolean greaterThanEqualTo(Long left, Long right) {
        return left >= right;
    }

    @Override
    int countRemaining(Long current, Long end, Long step) {
        return (int)((end - current) / step) + 1;
    }

    @Override
    public Long[] generateRest() {
        final Long[] rest = new Long[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    public long[] generateLongRest() {
        final long[] rest = new long[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    @Override
    public List<Long> generateRestList() {
        return List.of(generateRest());
    }

    @Override
    public Stream<Long> stream() {
        return generateRestList().stream();
    }

    @Override
    public Iterator<Long> iterator() {
        return this;
    }
}