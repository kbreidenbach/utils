package me.breidenbach.util.examples;

import java.util.stream.IntStream;

import static me.breidenbach.util.ForComp.*;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/14/17
 */
public class ForComprehensionNestedLoopExample {

    private int nestedLoops() {
        int result = 0;

        for (int i = 0; i < 100; i ++) {
            for(int j = 0; j < 50; j++) {
                for(int k = 0; k < 20; k++) {
                    result += (i + j + k);
                }
            }
        }

        return result;
    }

    private int nestedForComp() {
        return forComp(IntStream.range(0, 100).boxed()).
                with(IntStream.range(0, 50).boxed()).
                with(IntStream.range(0, 20).boxed(), forFunction(l -> l.get(0) + l.get(1) + l.get(2))).
                yield().mapToInt(value -> (Integer) value).sum();
    }

    public static void main(String[] args) {
        final ForComprehensionNestedLoopExample example = new ForComprehensionNestedLoopExample();

        final int forLoop = example.nestedLoops();
        final int forComp = example.nestedForComp();

        if (forComp != forLoop) throw new RuntimeException("Should be equal");
    }
}