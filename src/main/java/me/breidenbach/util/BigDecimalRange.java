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

    public BigDecimalRange(final double start, final double end, final double step) {
        this(BigDecimal.valueOf(start), BigDecimal.valueOf(end), BigDecimal.valueOf(step));
    }

    public BigDecimalRange(final BigDecimal start, final BigDecimal end, final BigDecimal step) {
        super(start, end, step, step.signum() == 1);
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

    @Override
    BigDecimal nextValue(final BigDecimal current, final BigDecimal step) {
        return current.add(step);
    }

    @Override
    boolean lessThanEqualTo(final BigDecimal left, final BigDecimal right) {
        return left.compareTo(right) <= 0;
    }

    @Override
    boolean greaterThanEqualTo(final BigDecimal left, final BigDecimal right) {
        return left.compareTo(right) >= 0;
    }

    @Override
    int countRemaining(final BigDecimal current, final BigDecimal end, final BigDecimal step) {
        return ((end.subtract(current)).divide(step, RoundingMode.FLOOR)).add(BigDecimal.ONE).intValue();
    }
}