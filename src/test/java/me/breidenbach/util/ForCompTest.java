package me.breidenbach.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.breidenbach.util.ForComp.forFunction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/1/17
 */
@SuppressWarnings("unchecked")
class ForCompTest {
    private static final Throwable ERROR = new Exception("BAD TEST");

    @Test
    void simple() {
        final List<Integer> numbers = List.of(1, 2);
        final Stream result = new ForComp().
                with(forFunction(i -> i.get(0)), numbers).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(numbers)));
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

        final Stream result = new ForComp().
                with(forFunction(i -> i.get(0)), outer).
                with(forFunction(j -> j.get(0)), inner).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void nestedLoopWithSum() {
        final List<Integer> outer = List.of(1, 2);
        final List<Integer> inner = List.of(20, 40);
        final List<Integer> expected = List.of(21, 41, 22, 42);

        final Stream result = new ForComp().
                with(outer).
                with(forFunction(j -> j.get(0) + j.get(1), 0), inner).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void stringConcatenation() {
        final List<String> outer = List.of("Hello", "Goodbye");
        final List<String> inner = List.of("Dave", "John");
        final List<String> expected = List.of("Hello Dave", "Hello John", "Goodbye Dave", "Goodbye John");

        final Stream result = new ForComp().
                with(outer).
                with(forFunction(j -> j.get(1) + " " + j.get(0), 0), inner).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void stringConcatenationWithYieldFunction() {
        final List<String> outer = List.of("Hello", "Goodbye");
        final List<String> inner = List.of("Dave", "John");
        final List<String> expected = List.of("HELLO DAVE", "HELLO JOHN", "GOODBYE DAVE", "GOODBYE JOHN");

        final Stream result = new ForComp().
                with(outer).
                with(forFunction(j -> j.get(1) + " " + j.get(0), 0), inner).
                yield(s -> ((String)s).toUpperCase());

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

    @Test
    void simpleTry() {
        final Try<Integer> test = new Try<>(() -> 2);
        final List<Integer> expected = List.of(2);

        final Stream result = new ForComp().
                with(forFunction(i -> i.get(0)), test).
                yield();

        assertThat(result.collect(Collectors.toList()), is(equalTo(expected)));
    }

}