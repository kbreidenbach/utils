package me.breidenbach.util.examples;

import static me.breidenbach.util.ForComp.forComp;
import static me.breidenbach.util.IntRange.intStream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/14/17
 */
class ForComprehensionNestedLoopExample {

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
         return forComp(intStream(0, 99)).
                with(intStream(0, 49)).
                with(intStream(0, 19)).
                yieldFlat().mapToInt(i -> i).sum();
    }

    public static void main(final String[] args) {
        final ForComprehensionNestedLoopExample example = new ForComprehensionNestedLoopExample();

        final int forLoop = example.nestedLoops();
        final int forComp = example.nestedForComp();

        if (forComp != forLoop) throw new RuntimeException("Should be equal");
    }
}