package me.breidenbach.util;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/17/17
 */
@SuppressWarnings("WeakerAccess")
public class BigIntegerRange extends BaseNumberRange<BigInteger> {

    public BigIntegerRange(final BigInteger start, final BigInteger end, final BigInteger step) {
        super(start, end, step, step.signum() == 1);
    }

    @Override
    public BigInteger[] generateRest() {
        final BigInteger[] rest = new BigInteger[getRemaining()];

        int index = 0;
        while(hasNext()) {
            rest[index++] = next();
        }

        return rest;
    }

    @Override
    public List<BigInteger> generateRestList() {
        return List.of(generateRest());
    }

    @Override
    public Stream<BigInteger> stream() {
        return generateRestList().stream();
    }

    @Override
    public Iterator<BigInteger> iterator() {
        return this;
    }

    @Override
    BigInteger nextValue(final BigInteger current, final BigInteger step) {
        return current.add(step);
    }

    @Override
    boolean lessThanEqualTo(final BigInteger left, final BigInteger right) {
        return left.compareTo(right) <= 0;
    }

    @Override
    boolean greaterThanEqualTo(final BigInteger left, final BigInteger right) {
        return left.compareTo(right) >= 0;
    }

    @Override
    int countRemaining(final BigInteger current, final BigInteger end, final BigInteger step) {
        return ((end.subtract(current)).divide(step)).add(BigInteger.ONE).intValue();
    }
}