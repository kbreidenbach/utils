package me.breidenbach.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
class RangeTest {

    @Test
    void generateRest() {
        final Range<Integer> range = new IntRange(0, 20, 2);
        final Integer[] expected = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };
        Integer[] rest = range.generateRest();
        assertThat(rest, is(equalTo(expected)));

        rest = range.generateRest();
        assertThat(rest.length, is(equalTo(0)));

        range.reset();
        assertThat(range.generateRestList(), is(equalTo(List.of(expected))));
    }

    @Test
    void iterateLong() {
        final Range<Long> range = new LongRange(0L, 20L, 2L);
        final Long[] expected = { 0L, 2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 20L };
        Long[] rest = new Long[11];

        int index = 0;

        while (range.hasNext()) {
            rest[index++] = range.next();
        }

        assertThat(rest, is(equalTo(expected)));

        range.reset();

        assertThat(range.next(), is(equalTo(0L)));
        assertThat(range.next(), is(equalTo(2L)));

        range.reset();
        assertThat(range.generateRestList(), is(equalTo(List.of(expected))));
    }

    @Test
    void iterateDouble() {
        final Range<Double> range = new DoubleRange(0D, 0.2, 0.1);
        final Double[] expected = { 0D, 0.1, 0.2 };
        Double[] rest = new Double[3];

        int index = 0;

        while (range.hasNext()) {
            rest[index++] = range.next();
        }

        assertThat(rest, is(equalTo(expected)));

        range.reset();

        assertThat(range.next(), is(equalTo(0D)));
        assertThat(range.next(), is(equalTo(0.1)));

        range.reset();
        assertThat(range.generateRestList(), is(equalTo(List.of(expected))));
    }

    @Test
    void iterateBigDecimal() {
        final Range<BigDecimal> range = new BigDecimalRange(0D, 1.1, 0.1);
        final BigDecimal[] expected = { BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.2),
                BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.4), BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.6),
                BigDecimal.valueOf(0.7), BigDecimal.valueOf(0.8), BigDecimal.valueOf(0.9), BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(1.1) };
        BigDecimal[] rest = new BigDecimal[12];

        int index = 0;

        while (range.hasNext()) {
            rest[index++] = range.next();
        }

        assertThat(rest, is(equalTo(expected)));

        range.reset();

        assertThat(range.next(), is(equalTo(BigDecimal.valueOf(0.0))));
        assertThat(range.next(), is(equalTo(BigDecimal.valueOf(0.1))));
        assertThat(range.next(), is(equalTo(BigDecimal.valueOf(0.2))));

        range.reset();
        assertThat(range.generateRestList(), is(equalTo(List.of(expected))));
    }

    @Test
    void iterateBigInteger() {
        final Range<BigInteger> range = new BigIntegerRange(BigInteger.ZERO, BigInteger.valueOf(6), BigInteger.TWO);
        final BigInteger[] expected = { BigInteger.ZERO, BigInteger.TWO, BigInteger.valueOf(4), BigInteger.valueOf(6) };
        BigInteger[] rest = new BigInteger[4];

        int index = 0;

        while (range.hasNext()) {
            rest[index++] = range.next();
        }

        assertThat(rest, is(equalTo(expected)));

        range.reset();

        assertThat(range.next(), is(equalTo(BigInteger.ZERO)));
        assertThat(range.next(), is(equalTo(BigInteger.TWO)));

        range.reset();
        assertThat(range.generateRestList(), is(equalTo(List.of(expected))));
    }

    @Test
    void customRange() {
        final String[] expected = {"one", "two", "three"};
        final Range<String> range = new StringRange(expected);

        assertThat(range.generateRest(), is(equalTo(expected)));
        assertThat(range.generateRest().length, is(equalTo(0)));

        range.reset();

        assertThat(range.next(), is(equalTo(expected[0])));
        assertThat(range.next(), is(equalTo(expected[1])));
        assertThat(range.next(), is(equalTo(expected[2])));
        Assertions.assertThrows(NoSuchElementException.class, range::next);

        range.reset();
        assertThat(range.generateRestList(), is(equalTo(List.of(expected))));

        range.reset();
    }

    public static class StringRange implements Range<String> {

        private final String[] data;
        private int index = 0;

        private StringRange(String[] data) {
            this.data = data;
        }

        @Override
        public void reset() {
            index = 0;
        }

        @Override
        public String[] generateRest() {
            final String[] rest =  Arrays.copyOfRange(data, index, data.length);
            index = data.length;
            return rest;
        }

        @Override
        public List<String> generateRestList() {
            return List.of(generateRest());
        }

        @Override
        public Stream<String> stream() {
            return generateRestList().stream();
        }

        @Override
        public Iterator<String> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < data.length;
        }

        @Override
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return data[index++];
        }
    }
}