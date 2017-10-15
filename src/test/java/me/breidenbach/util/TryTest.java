package me.breidenbach.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Kevin E. Breidenbach
 * Date: 9/30/17
 */
class TryTest {
    private static final Throwable ERROR = new Exception("BAD TEST");
    private static final String TEST = "TEST";

    private int failureCount;
    private int successCount;

    private final Consumer<String> successTester = s -> {
        assertThat(s, is(equalTo(TEST)));
        successCount++;
    };

    private final Consumer<Throwable> failureTester = t -> {
        assertThat(t, is(equalTo(ERROR)));
        failureCount++;
    };

    @BeforeEach
    void setUp() {
        failureCount = 0;
        successCount = 0;
    }

    @Test
    void onSuccess() {
        final Try<String> testTry = new Success<>(TEST);

        testTry.onSuccess(successTester);

        assertThat(successCount, is(equalTo(1)));
        assertThat(testTry.isSuccess(), is(true));
        assertThat(testTry.isFailure(), is(false));
    }

    @Test
    void onFailure() {
        final Try<String> testTry = new Failure<>(ERROR);

        testTry.onFailure(failureTester);

        assertThat(failureCount, is(equalTo(1)));
        assertThat(testTry.isSuccess(), is(false));
        assertThat(testTry.isFailure(), is(true));
    }

    @Test
    void thenSuccess() {
        final TestType testType = new TestType(false);
        final Try<String> testTry = new Try<>(testType);

        testTry.then(failureTester, successTester);

        assertThat(successCount, is(equalTo(1)));
        assertThat(failureCount, is(equalTo(0)));
        assertThat(testTry.isSuccess(), is(true));
        assertThat(testTry.isFailure(), is(false));
    }

    @Test
    void thenFailure() {
        final TestType testType = new TestType(true);
        final Try<String> testTry = new Try<>(testType);

        testTry.then(failureTester, successTester);

        assertThat(successCount, is(equalTo(0)));
        assertThat(failureCount, is(equalTo(1)));
        assertThat(testTry.isSuccess(), is(false));
        assertThat(testTry.isFailure(), is(true));
    }

    @Test
    void streamSuccess() {
        final TestType testType = new TestType(false);
        final Try<String> testTry = new Try<>(testType);

        testTry.stream().forEach(successTester);

        assertThat(successCount, is(equalTo(1)));
    }

    @Test
    void streamFailure() {
        final TestType testType = new TestType(true);
        final Try<String> testTry = new Try<>(testType);

        testTry.stream().forEach(successTester);

        assertThat(successCount, is(equalTo(0)));
    }

    @Test
    void forEachSuccess() {
        final TestType testType = new TestType(false);
        final Try<String> testTry = new Try<>(testType);

        testTry.forEach(successTester);

        assertThat(successCount, is(equalTo(1)));
    }

    @Test
    void forEachFailure() {
        final TestType testType = new TestType(true);
        final Try<String> testTry = new Try<>(testType);

        testTry.forEach(successTester);

        assertThat(successCount, is(equalTo(0)));
    }

    @Test
    void mapSuccess() {
        final TestType testType = new TestType(false);
        final Try<String> testTry = new Try<>(testType);
        final Try<Integer> result = testTry.map(String::length);

        assertThat(result.isSuccess(), is(true));
        assertThat(result.isFailure(), is( false));
        assertThat(result.get().isPresent(), is(true));
        assertThat(result.getError().isPresent(), is(false));
        assertThat(result.get().get(), is(equalTo(TEST.length())));
    }

    @Test
    void mapFailure() {
        final TestType testType = new TestType(true);
        final Try<String> testTry = new Try<>(testType);
        final Try<Integer> result = testTry.map(String::length);

        assertThat(result.isSuccess(), is(false));
        assertThat(result.isFailure(), is( true));
        assertThat(result.get().isPresent(), is(false));
        assertThat(result.getError().isPresent(), is(true));
        assertThat(result.getError().get(), is(equalTo(ERROR)));
    }

    @Test
    void getOrElseSuccess() {
        final TestType testType = new TestType(false);
        final Try<String> testTry = new Try<>(testType);
        final String defaultValue = "DEFAULT";

        assertThat(testTry.getOrElse(defaultValue).isPresent(), is(true));
        assertThat(testTry.getOrElse(defaultValue).get(), is(equalTo(TEST)));
    }

    @Test
    void getOrElseFailure() {
        final TestType testType = new TestType(true);
        final Try<String> testTry = new Try<>(testType);
        final String defaultValue = "DEFAULT";

        assertThat(testTry.getOrElse(defaultValue).isPresent(), is(true));
        assertThat(testTry.getOrElse(defaultValue).get(), is(equalTo(defaultValue)));
    }

    @Test
    void foldSuccess() {
        final TestType testType = new TestType(false);
        final Try<String> testTry = new Try<>(testType);
        final int result = testTry.fold(t -> 0, s-> 1);

        assertThat(result, is(equalTo(1)));
    }

    @Test
    void foldFailure() {
        final TestType testType = new TestType(true);
        final Try<String> testTry = new Try<>(testType);
        final int result = testTry.fold(t -> 0, s-> 1);

        assertThat(result, is(equalTo(0)));
    }

    private class TestType implements Try.TryRunnable<String> {
        private final boolean fail;

        private TestType(boolean fail) {
            this.fail = fail;
        }

        @Override
        public String run() throws Throwable {
            if (fail) throw ERROR;
            return TEST;
        }
    }
}