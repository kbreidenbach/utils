package me.breidenbach.util;


import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
@SuppressWarnings({"unused", "UnnecessaryInterfaceModifier"})
public interface Range<E> extends Iterable<E>, Iterator<E> {

    public static Range<Integer> range(final int start, final int end, final int step) {
        return new IntRange(start, end, step);
    }

    public static Range<Long> range(final long start, final long end, final long step) {
        return new LongRange(start, end, step);
    }

    public static Range<Integer> range(final int start, final int end) {
        return new IntRange(start, end);
    }

    public static Range<Long> range(final long start, final long end) {
        return new LongRange(start, end);
    }

    public static Range<Double> range(final double start, final double end, final double step) {
        return new DoubleRange(start, end, step);
    }


    void reset();

    E[] generateRest();

    List<E> generateRestList();

    Stream<E> stream();
}
