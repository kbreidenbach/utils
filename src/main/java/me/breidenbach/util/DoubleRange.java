package me.breidenbach.util;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/17/17
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DoubleRange extends BaseNumberRange<Double> {

    public static Stream<Double> doubleStream(final double start, final double end, final double step) {
        return new DoubleRange(start, end, step).stream();
    }

    public DoubleRange(final double start, final double end, final double step) {
        super(start, end, step, step > 0);
    }

    @Override
    public Double[] generateRest() {
        final Double[] rest = new Double[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    public double[] generateDoubleRest() {
        final double[] rest = new double[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    @Override
    public List<Double> generateRestList() {
        return List.of(generateRest());
    }

    @Override
    public Stream<Double> stream() {
        return generateRestList().stream();
    }

    @Override
    public Iterator<Double> iterator() {
        return this;
    }

    @Override
    Double nextValue(final Double current, final Double step) {
        return current + step;
    }

    @Override
    boolean lessThanEqualTo(final Double left, final Double right) {
        return left <= right;
    }

    @Override
    boolean greaterThanEqualTo(final Double left, final Double right) {
        return left >= right;
    }

    @Override
    int countRemaining(final Double current, final Double end, final Double step) {
        return (int)(((end - current) / step) + 1.0);
    }
}
