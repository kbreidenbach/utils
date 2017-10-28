package me.breidenbach.util;


import java.math.BigDecimal;
import java.math.BigInteger;
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

    public static Range<BigInteger> range(final BigInteger start, final BigInteger end, final BigInteger step) {
        return new BigIntegerRange(start, end, step);
    }

    public static Range<BigDecimal> range(final BigDecimal start, final BigDecimal end, final BigDecimal step) {
        return new BigDecimalRange(start, end, step);
    }

    public static Stream<Integer> stream(final int start, final int end, final int step) {
        return new IntRange(start, end, step).stream();
    }

    public static Stream<Long> stream(final long start, final long end, final long step) {
        return new LongRange(start, end, step).stream();
    }

    public static Stream<Integer> stream(final int start, final int end) {
        return new IntRange(start, end).stream();
    }

    public static Stream<Long> stream(final long start, final long end) {
        return new LongRange(start, end).stream();
    }

    public static Stream<Double> stream(final double start, final double end, final double step) {
        return new DoubleRange(start, end, step).stream();
    }

    public static Stream<BigInteger> stream(final BigInteger start, final BigInteger end, final BigInteger step) {
        return new BigIntegerRange(start, end, step).stream();
    }

    public static Stream<BigDecimal> stream(final BigDecimal start, final BigDecimal end, final BigDecimal step) {
        return new BigDecimalRange(start, end, step).stream();
    }


    void reset();

    E[] generateRest();

    List<E> generateRestList();

    Stream<E> stream();
}
