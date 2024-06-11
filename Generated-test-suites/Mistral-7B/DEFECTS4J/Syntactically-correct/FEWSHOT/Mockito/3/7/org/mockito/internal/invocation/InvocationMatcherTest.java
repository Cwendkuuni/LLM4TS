package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testCreateInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), matcher.getMatchers());
    }

    @Test
    public void testCreateInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), eq("bar"));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class, new Object[] { method });
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), eq("bar"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String print = new PrintSettings().print(matchers, invocation);
        String actual = matcher.toString();

        assertTrue(print.equals(actual));
    }

    @Test
    public void testMatchesWithSameMethodAndArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq("bar"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertTrue(matcher1.matches(invocation2));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class, new Object[] { "differentMock" }));

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq("bar"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq("bar"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));

        List<Matcher> matchers = Arrays.asList(eq(1), eq(2));

        Object[] arguments1 = new Object[] { 1, 2 };
        Object[] arguments2 = new Object[] { 1, 3 };

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void testMatchesWithVarargs() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));

        List<Matcher> matchers = Arrays.asList(eq(1), eq("foo"), eq("bar"));

        Object[] arguments1 = new Object[] { 1, "foo", "bar" };
        Object[] arguments2 = new Object[] { 1, "foo", "bar", "baz" };

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        when(method1.getParameterTypes()).thenReturn(new Class[] { String.class, Integer.TYPE });
        when(method2.getParameterTypes()).thenReturn(new Class[] { String.class, Integer.TYPE });

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");

        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method1");

        Object[] arguments1 = new Object[] { 1, "foo" };
        Object[] arguments2 = new Object[] { 2, "bar" };

        assertFalse(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");

        assertFalse(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");

        Object[] arguments1 = new Object[] { 1, "foo" };
        Object[] arguments2 = new Object[] { 1, "foo" };

        assertTrue(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = mock(Invocation.class, new Object[] { mock(Object.class), mock(Object.class) });
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(eq("foo"), eq("bar"));
        matcher.captureArgumentsFrom(invocation);

        ArgumentCaptor<Matcher> fooCaptor = ArgumentCaptor.forClass(Matcher.class);
        ArgumentCaptor<Matcher> barCaptor = ArgumentCaptor.forClass(Matcher.class);

        verify(matchers).get(0).matches(fooCaptor.capture());
        verify(matchers).get(1).matches(barCaptor.capture());

        assertEquals("foo", fooCaptor.getValue().getValue());
        assertEquals("bar", barCaptor.getValue().getValue());
    }

    @Test
    public void testCreateInvocationMatcherFromInvocations() {
        Invocation invocation1 = mock(Invocation.class, new Object[] { mock(Object.class), mock(Object.class) });
        Invocation invocation2 = mock(Invocation.class, new Object[] { mock(Object.class), mock(Object.class) });
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher1 = matchers.get(0);
        InvocationMatcher matcher2 = matchers.get(1);

        assertEquals(invocation1, matcher1.getInvocation());
        assertEquals(invocation2, matcher2.getInvocation());
    }
}