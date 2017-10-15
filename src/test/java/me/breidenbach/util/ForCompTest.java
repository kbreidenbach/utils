package me.breidenbach.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static me.breidenbach.util.ForComp.forComp;
import static me.breidenbach.util.ForComp.forFunction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/1/17
 */
@SuppressWarnings("unchecked")
class ForCompTest {
    @Test
    void simple() {
        final List<Integer> numbers = List.of(1, 2);
        final Stream result = forComp(numbers, forFunction(i -> i.get(0))).yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(numbers)));
    }

    @Test
    void simpleWithIff() {
        final List<Integer> numbers = List.of(1, 2, 3, 4);
        final List<Integer> expected = List.of(2, 4);
        final Predicate<Integer> predicate = i -> i % 2 == 0;
        final Stream result = forComp(numbers, forFunction(i -> i.get(0))).iff(predicate).yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void nestedLoop() {
        final List<Integer> outer = List.of(1, 2);
        final List<Integer> inner = List.of(20, 40);
        final List<List<Integer>> expected = List.of(
                List.of(1, 20),
                List.of(1, 40),
                List.of(2, 20),
                List.of(2, 40));

        final Stream result = forComp(outer, forFunction(i -> i.get(0))).
                with(inner, forFunction(j -> j.get(0))).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void nestedLoopWithSum() {
        final List<Integer> outer = List.of(1, 2);
        final List<Integer> inner = List.of(20, 40);
        final List<Integer> expected = List.of(21, 41, 22, 42);

        final Stream result = forComp(outer).
                with(inner, forFunction(j -> j.get(0) + j.get(1), 0)).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void nestedStreamWithSum() {
        final Stream<Integer> outer = IntStream.generate(new IntegerSupplier(1, 1)).limit(2).boxed();
        final Stream<Integer> inner = IntStream.generate(new IntegerSupplier(20, 20)).limit(2).boxed();
        final List<Integer> expected = List.of(21, 41, 22, 42);

        final Stream result = forComp(outer).
                with(inner, forFunction(j -> j.get(0) + j.get(1), 0)).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void stringConcatenation() {
        final List<String> outer = List.of("Hello", "Goodbye");
        final List<String> inner = List.of("Dave", "John");
        final List<String> expected = List.of("Hello Dave", "Hello John", "Goodbye Dave", "Goodbye John");

        final Stream result = forComp(outer).
                with(inner, forFunction(j -> j.get(1) + " " + j.get(0), 0)).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void stringConcatenationWithYieldFunction() {
        final List<String> outer = List.of("Hello", "Goodbye");
        final List<String> inner = List.of("Dave", "John");
        final List<String> expected = List.of("HELLO DAVE", "HELLO JOHN", "GOODBYE DAVE", "GOODBYE JOHN");

        final Stream result = forComp(outer).
                with(inner, forFunction(j -> j.get(1) + " " + j.get(0), 0)).
                yield(s -> ((String)s).toUpperCase());

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void stringConcatenationWithYieldFunctionAndIff() {
        final List<String> outer = List.of("Hello", "Goodbye");
        final List<String> inner = List.of("Dave", "John");
        final List<String> expected = List.of("HELLO DAVE", "HELLO JOHN");

        final Stream result = forComp(outer).<String>iff(str -> str.equals("Hello")).
                with(inner, forFunction(j -> j.get(1) + " " + j.get(0), 0)).
                yield(s -> ((String)s).toUpperCase());

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void simpleTry() {
        final Try<Integer> test = Try.tryRun(() -> 2);
        final List<Integer> expected = List.of(4);

        final Stream result = forComp(test).
                with(List.of(2), forFunction(i -> i.get(0) + i.get(1), 0)).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    private class IntegerSupplier implements IntSupplier {
        private final int step;
        private int current;
        private IntegerSupplier(int start, int step) {
            this.current = start;
            this.step = step;
        }

        @Override
        public int getAsInt() {
            final int result = current;
            current += step;
            return result;
        }
    }

}