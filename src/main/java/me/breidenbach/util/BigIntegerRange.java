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

    public BigIntegerRange(BigInteger start, BigInteger end, BigInteger step) {
        super(start, end, step, step.signum() == 1);
    }

    @Override
    BigInteger nextValue(BigInteger current, BigInteger step) {
        return current.add(step);
    }

    @Override
    boolean lessThanEqualTo(BigInteger left, BigInteger right) {
        return left.compareTo(right) <= 0;
    }

    @Override
    boolean greaterThanEqualTo(BigInteger left, BigInteger right) {
        return left.compareTo(right) >= 0;
    }

    @Override
    int countRemaining(BigInteger current, BigInteger end, BigInteger step) {
        return ((end.subtract(current)).divide(step)).add(BigInteger.ONE).intValue();
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
}