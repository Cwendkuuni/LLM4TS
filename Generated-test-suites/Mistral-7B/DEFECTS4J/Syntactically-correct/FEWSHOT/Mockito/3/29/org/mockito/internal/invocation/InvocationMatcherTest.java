package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentMatchers.eq;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_emptyList() {
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        assertTrue(im.matches(invocation));
    }

    @Test
    public void testInvocationMatcher() {
        Invocation invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = Arrays.asList(argThat(m -> m.matches(new Object[]{"foo"})), eq("bar"));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[]{"foo", "bar"};
        Invocation invocationWithArgs = mock(InvocationOnMock.class);
        when(invocationWithArgs.getArguments()).thenReturn(arguments);
        assertTrue(im.matches(invocationWithArgs));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(InvocationOnMock.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");
        when(method1.getParameterTypes()).thenReturn(new Class[]{String.class, int.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, String.class});

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);
        assertFalse(im1.hasSameMethod(im2));

        im1 = new InvocationMatcher(invocation1, Arrays.asList(eq("foo")));
        im2 = new InvocationMatcher(invocation2);
        assertFalse(im1.hasSameMethod(im2));

        im1 = new InvocationMatcher(invocation1, Arrays.asList(eq("foo")));
        im2 = new InvocationMatcher(invocation2, Arrays.asList(eq("foo")));
        assertTrue(im1.hasSameMethod(im2));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        String expected = "InvocationMatcher{invocation=MockitoInvocationInterceptor$MockInvocation{method=MockForMockito{methodName='', mock=MockForMockito{}} at org.mockito.internal.invocation.InvocationMatcherTest.testToString(InvocationMatcherTest.java:165), matchers=[ArgumentMatchers.eq(foo), ArgumentMatchers.eq(1)]}";
        assertTrue(im.toString().startsWith(expected));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[]{"foo"});
        List<Matcher> matchers = Arrays.asList(argThat(m -> m.matches("foo")));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        im.captureArgumentsFrom(invocation);
        Matcher capturedMatcher = matchers.get(0);
        assertTrue(capturedMatcher.matches("foo"));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
        assertTrue(matchers.size() == 2);
        assertTrue(matchers.get(0).matches(invocation1));
        assertTrue(matchers.get(1).matches(invocation2));
    }
}