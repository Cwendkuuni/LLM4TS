package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithNoMatchers() {
        // given
        Method mockMethod = Mockito.mock(String.class);
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mockMethod, new Object[]{null}, new Location(1, 1, "TestClass.java"), null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean result = invocationMatcher.matches(new InvocationOnMock(mockMethod, new Object[]{null}, new Location(1, 1, "TestClass.java"), null));

        // then
        assertTrue("InvocationMatcher should match with no matchers", result);
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        // given
        Method mockMethod = Mockito.mock(String.class);
        Object mock = Mockito.mock(Object.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object[] arguments = new Object[]{arg1, arg2};
        Invocation invocation = new Invocation(mockMethod, arguments, new Location(1, 1, "TestClass.java"), null);

        Matcher<Object> argMatcher1 = ArgumentMatchers.is(arg1);
        Matcher<Object> argMatcher2 = ArgumentMatchers.not(arg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argMatcher1, argMatcher2));

        // when
        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        methodIntercept(actualInvocation, argMatcher1, argMatcher2);
        boolean result = invocationMatcher.matches(actualInvocation);

        // then
        assertTrue("InvocationMatcher should match with given matchers", result);
    }

    @Test
    public void testInvocationMatcherMismatch() {
        // given
        Method mockMethod = Mockito.mock(String.class);
        Object mock = Mockito.mock(Object.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();
        Object[] arguments = new Object[]{arg1, arg2, arg3};
        Invocation invocation = new Invocation(mockMethod, arguments, new Location(1, 1, "TestClass.java"), null);

        Matcher<Object> argMatcher1 = ArgumentMatchers.is(arg1);
        Matcher<Object> argMatcher2 = ArgumentMatchers.is(arg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argMatcher1, argMatcher2));

        // when
        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        methodIntercept(actualInvocation, argMatcher1, argMatcher2, argMatcher2);
        boolean result = invocationMatcher.matches(actualInvocation);

        // then
        assertFalse("InvocationMatcher should not match with mismatched arguments", result);
    }

    @Test
    public void testInvocationMatcherWithOverloadedMethods() {
        // given
        Method mockMethod1 = Mockito.mock(String.class);
        Object mock = Mockito.mock(Object.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();
        Object[] arguments1 = new Object[]{arg1, arg2};
        Object[] arguments2 = new Object[]{arg1, arg2, arg3};
        Invocation invocation1 = new Invocation(mockMethod1, arguments1, new Location(1, 1, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(mockMethod1, arguments2, new Location(1, 1, "TestClass.java"), null);

        Matcher<Object> argMatcher1 = ArgumentMatchers.is(arg1);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(argMatcher1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(argMatcher1));

        // when
        InvocationOnMock actualInvocation1 = Mockito.mock(InvocationOnMock.class);
        methodIntercept(actualInvocation1, argMatcher1, argMatcher1);
        boolean result1 = invocationMatcher1.matches(actualInvocation1);
        assertTrue("InvocationMatcher should match with same method and arguments", result1);

        InvocationOnMock actualInvocation2 = Mockito.mock(InvocationOnMock.class);
        methodIntercept(actualInvocation2, argMatcher1, argMatcher1, argMatcher1);
        boolean result2 = invocationMatcher2.matches(actualInvocation2);
        assertFalse("InvocationMatcher should not match with different method", result2);
    }

    @Test
    public void testToString() {
        // given
        Method mockMethod = Mockito.mock(String.class);
        Object mock = Mockito.mock(Object.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();
        Object[] arguments = new Object[]{arg1, arg2, arg3};
        Invocation invocation = new Invocation(mockMethod, arguments, new Location(1, 1, "TestClass.java"), null);

        Matcher<Object> argMatcher1 = ArgumentMatchers.is(arg1);
        Matcher<Object> argMatcher2 = ArgumentMatchers.is(arg2);
        Matcher<Object> argMatcher3 = ArgumentMatchers.is(arg3);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argMatcher1, argMatcher2, argMatcher3));

        // when
        String result = invocationMatcher.toString();

        // then
        String expected = String.format("InvocationMatcher{method=%s, invocation=%s, matchers=[%s, %s, %s]}", mockMethod, invocation, argMatcher1, argMatcher2, argMatcher3);
        assertEquals("toString should return correct representation", expected, result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Method mockMethod = Mockito.mock(String.class);
        Object mock = Mockito.mock(Object.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();
        Object[] arguments = new Object[]{arg1, arg2, arg3};
        Invocation invocation = new Invocation(mockMethod, arguments, new Location(1, 1, "TestClass.java"), null);

        Matcher<Object> argMatcher1 = ArgumentMatchers.is(arg1);
        Matcher<Object> argMatcher2 = ArgumentMatchers.is(arg2);
        Matcher<Object> argMatcher3 = ArgumentMatchers.is(arg3);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argMatcher1, argMatcher2, argMatcher3));

        Object capturedArg1 = new Object();
        Object capturedArg2 = new Object();
        Object capturedArg3 = new Object();

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        invocationMatcher.captureArgumentsFrom(actualInvocation);

        // when
        assertEquals("Captured arguments should match original arguments", arg1, actualInvocation.getArguments()[0]);
        assertEquals("Captured arguments should match original arguments", arg2, actualInvocation.getArguments()[1]);
        assertEquals("Captured arguments should match original arguments", arg3, actualInvocation.getArguments()[2]);
    }

    private void methodIntercept(InvocationOnMock mock, Matcher... matchers) {
        MethodInterceptor interceptor = invocation -> {
            for (Matcher matcher : matchers) {
                matcher.matches(invocation.getArguments()[invocation.getArgumentIndices()[0]]);
            }
            return new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return null;
                }
            }.answer(invocation);
        };
        Mockito.doAnswer(interceptor).when(mock);
    }
}