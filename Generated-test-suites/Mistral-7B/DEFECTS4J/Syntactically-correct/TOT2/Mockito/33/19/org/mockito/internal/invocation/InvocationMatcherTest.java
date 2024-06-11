package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation actual = mock(Invocation.class);
        assertTrue(matcher.matches(actual));
    }

    // Test case for Invocations with different mocks
    @Test
    public void testDifferentMocks() {
        Invocation invocation1 = mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation actual1 = mock(Invocation.class);
        InvocationMatcher matcher2 = new InvocationMatcher(actual1);
        assertFalse(matcher1.matches(matcher2));
    }

    // Test case for Invocations with different methods
    @Test
    public void testDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation actual1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        InvocationMatcher matcher2 = new InvocationMatcher(actual1, Collections.singletonList(method1));
        assertFalse(matcher1.matches(matcher2));
    }

    // Test case for Invocations with different arguments
    @Test
    public void testDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{2, 3};
        Invocation actual = mock(Invocation.class);
        InvocationMatcher actualMatcher = new InvocationMatcher(actual, Collections.singletonList(ArgumentMatchers.arrayContaining(arguments1)));
        assertFalse(matcher.matches(actualMatcher));
    }

    // Test case for Invocations with null arguments
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation actual = mock(Invocation.class);
        InvocationMatcher actualMatcher = new InvocationMatcher(actual, null);
        matcher.matches(actualMatcher);
    }

    // Test case for Invocations with exceptions
    @Test
    public void testExceptions() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation actual = mock(Invocation.class);
        InvocationMatcher actualMatcher = new InvocationMatcher(actual);
        doThrow(new RuntimeException()).when(actual).getMethod();
        assertFalse(matcher.matches(actualMatcher));
    }
}
//