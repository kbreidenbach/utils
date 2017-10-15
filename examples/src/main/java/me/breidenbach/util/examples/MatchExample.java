package me.breidenbach.util.examples;

import me.breidenbach.util.Failure;
import me.breidenbach.util.Match;
import me.breidenbach.util.Success;
import me.breidenbach.util.Try;

import static me.breidenbach.util.Match.match;
import static me.breidenbach.util.Match.matchCase;
import static me.breidenbach.util.Match.typeCase;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
public class MatchExample {

    private Try<String> successString() {
        return new Success<>("Yup!");
    }

    private Try<String> failureString() {
        return new Failure<>(new Exception("Nope!"));
    }

    public static void main(String[] args) {
        final MatchExample example = new MatchExample();
        final Try<String> tryOne = example.successString();
        final Try<String> tryTwo = example.failureString();

        boolean worked = match(tryOne,
                typeCase(Success.class, o -> true),
                typeCase(Failure.class, o -> false));

        if (!worked) throw new RuntimeException("Should have worked");

        worked = match(tryTwo,
                typeCase(Success.class, o -> true),
                typeCase(Failure.class, o -> false));

        if (worked) throw new RuntimeException("Shouldn't have worked");


        final int i = 28;

        final int result = match(i,
                matchCase(e -> e + 1 == 30, e -> 30),
                matchCase(e -> e / 4 == 7, e -> 7));

        if (result != 7) throw new RuntimeException("Should've been 7");

        final Double pi = 3.1415927;

        final String answer = match(pi,
                typeCase(Integer.class, j -> "Not today"),
                matchCase(j -> (Double)j == 3.1415927, j-> "Eureka!"));

        if (!answer.equals("Eureka!")) throw new RuntimeException("Sunk");


    }
}
