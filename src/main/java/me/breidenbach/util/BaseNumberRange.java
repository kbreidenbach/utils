package me.breidenbach.util;

import java.util.NoSuchElementException;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
abstract class BaseNumberRange<E extends Number> implements Range<E> {

    private final E start;
    private final E end;
    private final E step;
    private final boolean stepPositive;
    private E current;

    BaseNumberRange(E start, E end, E step, boolean stepPositive) {
        this.start = start;
        this.end = end;
        this.current = start;
        this.step = step;
        this.stepPositive = stepPositive;
    }

    @Override
    public void reset() {
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        return stepPositive ? lessThanEqualTo(current, end) : greaterThanEqualTo(current, end);
    }

    @Override
    public E next() {
        final E result;

        if (!hasNext()) throw new NoSuchElementException();

        result = current;
        current = nextValue(current, step);

        return result;
    }

    abstract E nextValue(E current, E step);

    /**
     * Test left parameter is less than or equal to right parameter
     * @param left left value
     * @param right right value
     * @return <code>true</code> if left is less than or equal to right, <code>false</code> otherwise
     */
    abstract boolean lessThanEqualTo(E left, E right);

    /**
     * Test left parameter is greater than or equal to right parameter
     * @param left left value
     * @param right right value
     * @return <code>true</code> if left is greater than or equal to right, <code>false</code> otherwise
     */
    abstract boolean greaterThanEqualTo(E left, E right);

    abstract int countRemaining(E current, E end, E step);

    int getRemaining() {
        return countRemaining(current, end, step);
    }
}
