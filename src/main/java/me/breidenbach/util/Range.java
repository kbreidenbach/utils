package me.breidenbach.util;


import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
public interface Range<E> extends Iterable<E>, Iterator<E> {

    static Range<Integer> range(final int start, final int end, final int step) {
        return new IntRange(start, end, step);
    }

    static Range<Long> range(final long start, final long end, final long step) {
        return new LongRange(start, end, step);
    }


    void reset();

    E[] generateRest();

    List<E> generateRestList();

    Stream<E> stream();
}
