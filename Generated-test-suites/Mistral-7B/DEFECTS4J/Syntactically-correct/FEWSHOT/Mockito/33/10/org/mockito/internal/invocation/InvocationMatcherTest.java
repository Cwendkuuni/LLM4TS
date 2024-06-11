package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatcher.argument;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testMatchesSameInvocation() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Invocation invocation = new Invocation(mock, method, new Object[0], new Object[0], new Location(1, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethod() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Invocation invocation1 = new Invocation(mock, method, new Object[0], new Object[0], new Location(1, 1), false, false);
        Invocation invocation2 = new Invocation(mock, method, new Object[0], new Object[0], new Location(2, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMethod() {
        Object mock = mock(String.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Invocation invocation1 = new Invocation(mock, method1, new Object[0], new Object[0], new Location(1, 1), false, false);
        Invocation invocation2 = new Invocation(mock, method2, new Object[0], new Object[0], new Location(2, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodSameArguments() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[0];
        Invocation invocation1 = new Invocation(mock, method, arguments, arguments, new Location(1, 1), false, false);
        Invocation invocation2 = new Invocation(mock, method, arguments, arguments, new Location(2, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodDifferentArguments() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Object[] arguments1 = new Object[] {"foo"};
        Object[] arguments2 = new Object[] {"bar"};
        Invocation invocation1 = new Invocation(mock, method, arguments1, arguments1, new Location(1, 1), false, false);
        Invocation invocation2 = new Invocation(mock, method, arguments2, arguments2, new Location(2, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodSameArgumentsButDifferentOrder() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[] {"arg1", "arg2"};
        Invocation invocation1 = new Invocation(mock, method, arguments, arguments, new Location(1, 1), false, false);
        Invocation invocation2 = new Invocation(mock, method, new Object[] {arguments[1], arguments[0]}, arguments, new Location(2, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationsWithArgumentsMatcher() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation invocation1 = new Invocation(mock, method, arguments, arguments, new Location(1, 1), false, false);
        Invocation invocation2 = new Invocation(mock, method, new Object[] {"qux", "qux"}, new Object[0], new Location(2, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0).and(argument(1).is("bar")));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[] {"foo"};
        Invocation invocation = new Invocation(mock, method, arguments, arguments, new Location(1, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0).is("foo"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = String.format("Invocation [%s, %s%narguments = [%s]%n]", mock.getClass().getName(), method.getName(), arguments[0]);
        String result = matcher.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = mock(String.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[] {"foo"};
        Invocation invocation = new Invocation(mock, method, arguments, arguments, new Location(1, 1), false, false);
        List<Matcher> matchers = Collections.singletonList(argument(0).is("foo"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = String.format("Invocation [%s, %s%narguments = [%s]%n] matches: %s%n", mock.getClass().getName(), method.getName(), arguments[0], matchers.toString());
        String result = matcher.toString(new PrintSettings());

        assertEquals(expected, result);
    }

    @Test
    public void testCreateFrom() {
        Object mock = mock(String.class);
        List<Invocation> invocations = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Invocation invocation = new Invocation(mock, mock(Method.class), new Object[0], new Object[0], new Location(1, 1), false, false);
            invocations.add(invocation);
        }

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(10, matchers.size());
    }
}