package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;
import org.mockito.stub.Stub;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @MockitoMock
    private MyClass mock;

    private InvocationMatcher matcher;

    @Before
    public void setup() {
        Object[] args = new Object[]{1, 2, "three"};
        Invocation invocation = new InvocationOnMock(mock, new MethodTestClass().getClass().getMethod("method", int.class, int.class, String.class), args);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndArguments() {
        InvocationMatcher expectedMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isEqual(2), isEqual("three")));
        InvocationMatcher actualMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isEqual(2), isEqual("three")));

        assertThat(expectedMatcher, is(equalTo(actualMatcher)));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        InvocationMatcher expectedMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isEqual(2), isEqual("three")));
        InvocationMatcher actualMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isInstanceOf(String.class), isEqual("four")));

        assertFalse(expectedMatcher.matches(actualMatcher));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        InvocationMatcher expectedMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isEqual(2), isEqual("three")));
        InvocationMatcher actualMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isEqual(2), isEqual(3)));

        assertFalse(expectedMatcher.matches(actualMatcher));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndVarargs() {
        Object[] varargArgs = new Object[]{1, 2, 3, 4};
        InvocationMatcher expectedMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isInstanceOf(List.class), contains(isEqual(2))));
        InvocationMatcher actualMatcher = new InvocationMatcher(mock, Arrays.asList(isEqual(1), isInstanceOf(List.class), isA(VarargMatcher.class), contains(isEqual(2))));

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass().getClass().getMethod("method", int.class, int[].class), varargArgs));
        expectedMatcher.captureArgumentsFrom(invocationMatcher);
        actualMatcher.captureArgumentsFrom(invocationMatcher);

        assertThat(expectedMatcher.getMatchers(), is(equalTo(actualMatcher.getMatchers())));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testErrorWhenTryingToCaptureArgumentsFromVarargsWithoutVarargMatcher() {
        Object[] varargArgs = new Object[]{1, 2, 3, 4};
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass().getClass().getMethod("method", int.class, int[].class), varargArgs));

        matcher.captureArgumentsFrom(matcher);
    }

    private static Matcher<?> isA(Matcher<?> matcher) {
        return new MatcherDecorator<>(matcher, "is a", new VarargMatcher<>(matcher));
    }

    private static Matcher<?> isVariableArgument(Matcher<?> matcher, int position) {
        return new MatcherDecorator<>(matcher, "is variable argument at position " + position, new VarargMatcher<>(matcher));
    }

    private static Matcher<?> isEqual(Object value) {
        return equalTo(value);
    }

    private static Matcher<?> isInstanceOf(Class<?> targetType) {
        return instanceOf(targetType);
    }

    private static Matcher<?> contains(Matcher<?> matcher) {
        return new ContainsMatcher(matcher);
    }

    private static boolean isEqual(Object value) {
        return equalTo(value);
    }

    private static class MethodTestClass {
        public void method(int a, int b, String c) {
        }

        public void method(int a, int[] b) {
        }
    }

    private static class ContainsMatcher<T> implements Matcher<Iterable<? super T>> {
        private final Matcher<T> matcher;

        public ContainsMatcher(Matcher<T> matcher) {
            this.matcher = matcher;
        }

        @Override
        public boolean matches(Object item) {
            return item instanceof Iterable && ((Iterable) item).contains(matcher);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("contains ");
            matcher.describeTo(description);
        }
    }
}