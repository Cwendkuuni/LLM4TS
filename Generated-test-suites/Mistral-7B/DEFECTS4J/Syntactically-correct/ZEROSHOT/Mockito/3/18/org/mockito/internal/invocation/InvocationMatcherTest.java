package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockitoMethodInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withCustomMatchers() {
        // given
        Invocation invocation = new MockitoMethodInvocation(getMock(), getMethod("testMethod"), new Object[]{1, "two"});
        List<Matcher> matchers = Arrays.asList(
                argThat(isInstanceOf(Integer.class)),
                argThat(isInstanceOf(String.class))
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        Assert.assertEquals("Invoked method does not match.", invocationMatcher.getMethod(), getMethod("testMethod"));
        Assert.assertEquals("Matchers do not match.", invocationMatcher.getMatchers(), matchers);
        Assert.assertTrue("Matches should be true.", invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcher_withEmptyMatchersList() {
        // given
        Invocation invocation = new MockitoMethodInvocation(getMock(), getMethod("testMethod"), new Object[]{1, "two"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        Assert.assertEquals("Invoked method does not match.", invocationMatcher.getMethod(), getMethod("testMethod"));
        Assert.assertNotNull("Matchers should be empty.", invocationMatcher.getMatchers());
        Assert.assertFalse("Matches should be false.", invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcher_matchesInvocationsWithSameMethod() {
        // given
        Invocation invocation1 = new MockitoMethodInvocation(getMock(), getMethod("testMethod"), new Object[]{1, "two"});
        Invocation invocation2 = new MockitoMethodInvocation(getMock(), getMethod("testMethod"), new Object[]{1, "two"});

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        Assert.assertTrue("Invocations should match.", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_doesNotMatchInvocationsWithDifferentMethods() {
        // given
        Invocation invocation1 = new MockitoMethodInvocation(getMock(), getMethod("testMethod1"), new Object[]{1, "two"});
        Invocation invocation2 = new MockitoMethodInvocation(getMock(), getMethod("testMethod2"), new Object[]{1, "two"});

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        Assert.assertFalse("Invocations should not match.", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_doesNotMatchInvocationsWithDifferentMocks() {
        // given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Invocation invocation1 = new MockitoMethodInvocation(mock1, getMethod("testMethod"), new Object[]{1, "two"});
        Invocation invocation2 = new MockitoMethodInvocation(mock2, getMethod("testMethod"), new Object[]{1, "two"});

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        Assert.assertFalse("Invocations should not match.", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_doesNotMatchInvocationsWithDifferentArguments() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new MockitoMethodInvocation(mock, getMethod("testMethod"), new Object[]{1, "two"});
        Invocation invocation2 = new MockitoMethodInvocation(mock, getMethod("testMethod"), new Object[]{2, "two"});

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        Assert.assertFalse("Invocations should not match.", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_capturesArgumentsFromInvocation() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation = new MockitoMethodInvocation(mock, getMethod("testMethod"), new Object[]{1, "two"});

        List<Matcher> matchers = Arrays.asList(argThat(isA(Integer.class)), argThat(isA(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        Matcher firstCaptor = invocationMatcher.getMatchers().get(0);
        Matcher secondCaptor = invocationMatcher.getMatchers().get(1);

        Assert.assertNotNull("First captor should not be null.", firstCaptor);
        Assert.assertNotNull("Second captor should not be null.", secondCaptor);

        Object capturedArg1 = firstCaptor.getValue();
        Object capturedArg2 = secondCaptor.getValue();

        Assert.assertEquals("First captured argument does not match.", 1, capturedArg1);
        Assert.assertEquals("Second captured argument does not match.", "two", capturedArg2);
    }

    private Object getMock() {
        return mock(Object.class);
    }

    private Method getMethod(String methodName) {
        try {
            return getClass().getMethod(methodName, (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Matcher argThat(Matcher matcher) {
        return new MatcherDecorator<>(matcher);
    }

    private Matcher argThat(Class<?> clazz) {
        return isA(clazz);
    }
}