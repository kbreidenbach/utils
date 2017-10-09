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
        new ForComp().
                with(forFunction(i -> i.get(0)), List.of(1, 2, 3, 4)).
                with(forFunction(i -> i.get(0) * i.get(1),0), List.of(10,20)).
                with(forFunction(j -> j.get(0) + j.get(1) + j.get(2), 0, 1), List.of(100, 200)).
                yield().forEach(System.out::println);
    }
}