package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatchers.eq;
import static org.mockito.internal.matchers.ArgumentsMatchers.isA;
import static org.mockito.internal.matchers.ArgumentsMatchers.same;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;

public class InvocationMatcherTest {

    @Test
    public void testMatchesSameInvocation() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);
        Invocation invocation = new InvocationOnMock(mock, method, "foo", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethod() {
        MockitoMock mock1 = new MockitoMock(Object.class);
        MockitoMock mock2 = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock1, method, "foo", 1);
        Invocation invocation2 = new InvocationOnMock(mock2, method, "bar", 2);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMethod() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method1 = MockitoMock.class.getMethod("methodName1", String.class);
        Method method2 = MockitoMock.class.getMethod("methodName2", int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method1, "foo");
        Invocation invocation2 = new InvocationOnMock(mock, method2, 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodWithSameArguments() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method, "foo", 1);
        Invocation invocation2 = new InvocationOnMock(mock, method, "foo", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodWithDifferentArguments() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method, "foo", 1);
        Invocation invocation2 = new InvocationOnMock(mock, method, "bar", 2);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodWithSameArgumentsButDifferentOrder() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", int.class, String.class);

        Invocation invocation1 = new InvocationOnMock(mock, method, 1, "foo");
        Invocation invocation2 = new InvocationOnMock(mock, method, "foo", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method1 = MockitoMock.class.getMethod("methodName1", String.class);
        Method method2 = MockitoMock.class.getMethod("methodName2", int.class);
        Method method3 = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method1, "foo");
        Invocation invocation2 = new InvocationOnMock(mock, method3, "foo", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testToString() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation = new InvocationOnMock(mock, method, "foo", 1);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String expected = String.format("Invocation on %s%n%s%n", mock.getClass().getName(), invocation.toString());
        String actual = matcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        MockitoMock mock = mock(Object.class);
        String[] arguments = new String[]{"foo", "bar"};

        Invocation invocation = new InvocationOnMock(mock, MockitoMock.class.getMethod("methodName", String[].class), arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(isA(String.class)));
        matchers.add(argThat(isA(String.class)));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);

        matcher.captureArgumentsFrom(invocation);

        assertEquals("foo", argumentCaptor1.getValue());
        assertEquals("bar", argumentCaptor2.getValue());
    }

    @Test
    public void testCreateFrom() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new InvocationOnMock(mock, method, "foo", 1));
        invocations.add(new InvocationOnMock(mock, method, "bar", 2));

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher1 = matchers.get(0);
        InvocationMatcher matcher2 = matchers.get(1);

        assertTrue(matcher1.matches(matcher1.getInvocation()));
        assertTrue(matcher2.matches(matcher2.getInvocation()));
        assertFalse(matcher1.matches(matcher2.getInvocation()));
        assertFalse(matcher2.matches(matcher1.getInvocation()));

        assertEquals(method, matcher1.getMethod());
        assertEquals(method, matcher2.getMethod());
    }

    @Test
    public void testToStringWithEmptyMatchers() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation = new InvocationOnMock(mock, method, "foo", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String expected = String.format("Invocation on %s%n%s(%s)%n", mock.getClass().getName(), method.getName(), invocation.getArguments());
        String actual = matcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithMatchers() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation = new InvocationOnMock(mock, method, "foo", 1);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq("foo"));
        matchers.add(eq(1));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = String.format("Invocation on %s%n%s(%s)%n%s%n", mock.getClass().getName(), method.getName(), invocation.getArguments(), matchers.toString());
        String actual = matcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithEmptyMatchersAndPrintSettings() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation = new InvocationOnMock(mock, method, "foo", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String expected = String.format("Invocation on %s%n%s(%s)%n", mock.getClass().getName(), method.getName(), invocation.getArguments());
        String actual = matcher.toString(new PrintSettings());
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithMatchersAndPrintSettings() {
        MockitoMock mock = new MockitoMock(Object.class);
        Method method = MockitoMock.class.getMethod("methodName", String.class, int.class);

        Invocation invocation = new InvocationOnMock(mock, method, "foo", 1);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq("foo"));
        matchers.add(eq(1));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = String.format("Invocation on %s%n%s(%s)%n%s%n", mock.getClass().getName(), method.getName(), invocation.getArguments(), matchers.toString());
        String actual = matcher.toString(new PrintSettings());
        assertEquals(expected, actual);
    }
}