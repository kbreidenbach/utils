package me.breidenbach.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static me.breidenbach.util.ForComp.forFunction;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/1/17
 */
class ForCompTest {

    @Test
    void forCompTest() {
        new ForComp(i -> i, List.of(1, 2, 3, 4)).
                with(forFunction(i -> i), List.of(10,20)).
                with(forFunction(j -> j), List.of(100, 200)).
                yield().forEach(System.out::println);
    }
}