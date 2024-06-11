package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_sameMethod_sameArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), is(1.0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = Mockito.mock(Invocation.class);
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);

        verify(invocation).getMock().equals(actualInvocation.getMock());
        verify(invocation).getMethod().equals(actualInvocation.getMethod());
        verify(invocation).matches(actualMatcher);
    }

    @Test
    public void testInvocationMatcher_sameMethod_differentArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = Mockito.mock(Invocation.class);
        List<Object> actualArguments = new Object[]{1, 2};
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation, Arrays.asList(eq(1), eq(2)));

        assertFalse(invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testInvocationMatcher_differentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(anyInt()));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_sameMethod_overloaded() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(eq(1.0)));

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(1)));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));

        Object[] arguments1 = new Object[]{1.0};
        Object[] arguments2 = new Object[]{1};

        assertFalse(invocationMatcher1.safelyArgumentsMatch(arguments2));

        Object[] arguments3 = new Object[]{1.0};

        assertTrue(invocationMatcher1.safelyArgumentsMatch(arguments3));
    }
}