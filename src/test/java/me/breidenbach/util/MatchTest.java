package me.breidenbach.util;

import me.breidenbach.util.Match.MatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static me.breidenbach.util.Match.classCase;
import static me.breidenbach.util.Match.def;
import static me.breidenbach.util.Match.match;
import static me.breidenbach.util.Match.matchCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


/**
 * @author Kevin E. Breidenbach
 * Date: 10/14/17
 */
class MatchTest {

    private int methodCallResult;

    @BeforeEach
    void setUp() {
        methodCallResult = 0;
    }

    @Test
    void simpleMatch() throws MatchException {
        final int integer = 3;
        final String message = "The number is %d";
        final String expected = String.format(message, integer);
        final String result =  match(integer,
                matchCase(i -> i == integer, i -> String.format(message, i)));

        assertThat(result, is(equalTo(expected)));
    }

    @Test
    void simpleMatchFail() throws MatchException {
        final int integer = 3;
        final String message = "The number is %d";
        Assertions.assertThrows(MatchException.class,
                () -> match(integer,
                         matchCase(i -> i == integer + 1, i -> String.format(message, i))));
    }

    @Test
    void classMatch() throws MatchException {
        final List<Integer> list = new ArrayList<>();
        final int expected = 10;
        final int result = match(list,
                classCase(ArrayList.class, (i) -> expected));

        assertThat(result, is(equalTo(expected)));
    }

    @Test
    void defaultFallThrough() throws MatchException {
        final int number = 3;
        final int expected = 5;

        final int result = match(number,
                matchCase(i -> i == 1, i -> i),
                def(i -> expected)
        );

        assertThat(result, is(equalTo(expected)));
    }

    @Test
    void matchToMethod() throws MatchException {
        final Message messageOne = new MessageOne();
        final Message messageTwo = new MessageTwo();
        final Message messageThree = new MessageThree();

        matchMessage(messageOne);
        assertThat(methodCallResult, is(equalTo(MessageOne.methodNumber)));

        matchMessage(messageTwo);
        assertThat(methodCallResult, is(equalTo(MessageTwo.methodNumber)));

        matchMessage(messageThree);
        assertThat(methodCallResult, is(equalTo(MessageThree.methodNumber)));

        Assertions.assertThrows(MatchException.class, () -> matchMessage(new Message() { }));
    }

    private void matchMessage(Message message) throws MatchException {
        // unfortunately we can't get away from the need to return null on a Java Void type :-(
        match(message,
                classCase(MessageOne.class, m -> { methodOne(); return null; }),
                classCase(MessageTwo.class, m -> { methodTwo(); return null; }),
                classCase(MessageThree.class, m -> { methodThree(); return null; })
        );
    }

    private void methodOne() {
        methodCallResult = MessageOne.methodNumber;
    }

    private void methodTwo() {
        methodCallResult = MessageTwo.methodNumber;
    }

    private void methodThree() {
        methodCallResult = MessageThree.methodNumber;
    }

    private interface Message {
    }

    private static class MessageOne implements Message {
        private static int methodNumber = 1;
    }

    private static class MessageTwo implements Message {
        private static int methodNumber = 2;
    }

    private static class MessageThree implements Message {
        private static int methodNumber = 3;
    }
}