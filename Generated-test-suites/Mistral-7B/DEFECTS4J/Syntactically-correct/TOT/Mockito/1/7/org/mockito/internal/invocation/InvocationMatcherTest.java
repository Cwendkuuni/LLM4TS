package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Anything;
import org.mockito.internal.matchers.ArrayContaining;
import org.mockito.internal.matchers.Capture;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.IsA;
import org.mockito.internal.matchers.IsInstanceOf;
import org.mockito.internal.matchers.IsNot;
import org.mockito.internal.matchers.IsNotAnInstanceOf;
import org.mockito.internal.matchers.IsNull;
import org.mockito.internal.matchers.LessThan;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.RegexPattern;
import org.mockito.internal.matchers.VerifiesZeroInteractions;
import org.mockito.internal.matchers.VerifiesZeroTimes;
import org.mockito.internal.reporting.PrintSettings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherMatches() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, int.class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        List<Matcher> matchers = Arrays.asList(
                eq("test"),
                is(instanceOf(String.class)),
                greaterThan(10),
                any(isA(Number.class)),
                isInstanceOf(String[].class),
                new RegexPattern(".*test.*")
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mockMethod("testMethod", String.class, int.class));
        when(candidate.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        assertTrue(invocationMatcher.matches(candidate));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchSameMethodButDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, int.class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        List<Matcher> matchers = Arrays.asList(
                eq("test"),
                is(instanceOf(String.class)),
                greaterThan(10),
                any(isA(Number.class))
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mockMethod("testMethod", double.class, int.class));
        when(candidate.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMethods() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod1", String.class, int.class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        List<Matcher> matchers = Arrays.asList(
                eq("test"),
                is(instanceOf(String.class)),
                greaterThan(10),
                any(isA(Number.class))
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mockMethod("testMethod2", String.class, String.class));
        when(candidate.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMocks() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, int.class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));
        when(candidate.getMethod()).thenReturn(mockMethod("testMethod", String.class, int.class));
        when(candidate.getLocation()).thenReturn(mockLocation(1, 1, "TestClass2.java"));

        List<Matcher> matchers = Arrays.asList(eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = mock(Invocation.class);
        Object arg1 = "test";
        Object arg2 = 12;
        Object[] arguments = new Object[]{arg1, arg2};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, int.class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> stringArgumentCaptor = argumentCaptor(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = argumentCaptor(Integer.class);

        verify(invocationMatcher, times(1)).captureArgumentsFrom(any(Invocation.class));

        assertEquals(arg1, stringArgumentCaptor.getValue());
        assertEquals(arg2, integerArgumentCaptor.getValue());
    }

    @Test
    public void testCaptureArgumentsFromVarargsInvocation() {
        Invocation invocation = mock(Invocation.class);
        Object arg1 = "test1";
        Object arg2 = "test2";
        Object arg3 = "test3";
        Object[] arguments = new Object[]{arg1, new Object[]{arg2, arg3}};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, String[].class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> stringArgumentCaptor = argumentCaptor(String.class);
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = argumentCaptor(String[].class);

        verify(invocationMatcher, times(1)).captureArgumentsFrom(any(Invocation.class));

        assertEquals(arg1, stringArgumentCaptor.getValue());
        assertEquals(arg2, stringArrayArgumentCaptor.getValue()[0]);
        assertEquals(arg3, stringArrayArgumentCaptor.getValue()[1]);
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        Object arg1 = "test1";
        Object arg2 = "test2";
        Object arg3 = "test3";
        Object[] arguments = new Object[]{arg1, new Object[]{arg2, arg3}};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, String[].class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> stringArgumentCaptor = argumentCaptor(String.class);
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = argumentCaptor(String[].class);

        verify(invocationMatcher, times(1)).captureArgumentsFrom(any(Invocation.class));

        assertEquals(arg1, stringArgumentCaptor.getValue());
        assertEquals(arg2, stringArrayArgumentCaptor.getValue()[0]);
        assertEquals(arg3, stringArrayArgumentCaptor.getValue()[1]);
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithCustomMatchers() {
        Invocation invocation = mock(Invocation.class);
        Object arg1 = "test1";
        Object arg2 = "test2";
        Object arg3 = "test3";
        Object[] arguments = new Object[]{arg1, new Object[]{arg2, arg3}};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mockMethod("testMethod", String.class, String[].class));
        when(invocation.getLocation()).thenReturn(mockLocation(1, 1, "TestClass.java"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(eq("test"), is("test2")));

        ArgumentCaptor<String> stringArgumentCaptor = argumentCaptor(String.class);
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = argumentCaptor(String[].class);
        ArgumentCaptor<String> stringArgumentCaptor2 = argumentCaptor(String.class);

        verify(invocationMatcher, times(1)).captureArgumentsFrom(any(Invocation.class));

        assertEquals(arg1, stringArgumentCaptor.getValue());
        assertEquals("test2", stringArgumentCaptor2.getValue());
        assertEquals(new String[]{"test2", "test3"}, stringArrayArgumentCaptor.getValue());
    }

    @Test
    public void testCreateFromInvocations() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
    }
}