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

    public static Stream<Double> doubleStream(double start, double end, double step) {
        return new DoubleRange(start, end, step).stream();
    }

    public DoubleRange(double start, double end, double step) {
        super(start, end, step, step > 0);
    }

    @Override
    Double nextValue(Double current, Double step) {
        return current + step;
    }

    @Override
    boolean lessThanEqualTo(Double left, Double right) {
        return left <= right;
    }

    @Override
    boolean greaterThanEqualTo(Double left, Double right) {
        return left >= right;
    }

    @Override
    int countRemaining(Double current, Double end, Double step) {
        return (int)(((end - current) / step) + 1.0);
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
}
