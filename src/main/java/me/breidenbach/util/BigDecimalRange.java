package me.breidenbach.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/17/17
 */
@SuppressWarnings("WeakerAccess")
public class BigDecimalRange extends BaseNumberRange<BigDecimal> {

    public BigDecimalRange(double start, double end, double step) {
        this(BigDecimal.valueOf(start), BigDecimal.valueOf(end), BigDecimal.valueOf(step));
    }

    public BigDecimalRange(BigDecimal start, BigDecimal end, BigDecimal step) {
        super(start, end, step, step.signum() == 1);
    }

    @Override
    BigDecimal nextValue(BigDecimal current, BigDecimal step) {
        return current.add(step);
    }

    @Override
    boolean lessThanEqualTo(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) <= 0;
    }

    @Override
    boolean greaterThanEqualTo(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) >= 0;
    }

    @Override
    int countRemaining(BigDecimal current, BigDecimal end, BigDecimal step) {
        return ((end.subtract(current)).divide(step, RoundingMode.FLOOR)).add(BigDecimal.ONE).intValue();
    }

    @Override
    public BigDecimal[] generateRest() {
        final BigDecimal[] rest = new BigDecimal[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    @Override
    public List<BigDecimal> generateRestList() {
        return List.of(generateRest());
    }

    @Override
    public Stream<BigDecimal> stream() {
        return generateRestList().stream();
    }

    @Override
    public Iterator<BigDecimal> iterator() {
        return this;
    }
}