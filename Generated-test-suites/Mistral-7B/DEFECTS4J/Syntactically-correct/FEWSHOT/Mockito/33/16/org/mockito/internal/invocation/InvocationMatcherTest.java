package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithValues;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Invocation;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    @Test
    public void testNewInstance() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertTrue(Collections.emptyList().equals(matcher.getMatchers()));
        assertEquals(Method.class, matcher.getMethod().getClass());
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argumentsWithValues("arg1", "arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
        assertEquals(Method.class, matcher.getMethod().getClass());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        InvocationMatcher matcher = new InvocationMatcher(mock(Invocation.class), Collections.emptyList());
        matcher.setMethod(method);
        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testInvocationMatcherWithMatchersMatches() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argumentsWithValues("arg1", "arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Invocation invocationToMatch = mock(Invocation.class);
        Object[] arguments = new Object[] {"arg1", "arg2"};
        when(invocationToMatch.getArguments()).thenReturn(arguments);
        when(invocationToMatch.getMethod()).thenReturn(new Method("testMethod", new Class[0]));
        when(invocationToMatch.getMock()).thenReturn(invocation.getMock());
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherWithoutMatchersDoesNotMatch() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = mock(Invocation.class);
        Object[] arguments = new Object[] {"arg1", "arg2"};
        when(invocationToMatch.getArguments()).thenReturn(arguments);
        when(invocationToMatch.getMethod()).thenReturn(new Method("testMethod", new Class[0]));
        when(invocationToMatch.getMock()).thenReturn(invocation.getMock());
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethodsDoesNotMatch() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = mock(Invocation.class);
        Method method1 = new Method("testMethod1", new Class[0]);
        Method method2 = new Method("testMethod2", new Class[0]);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationToMatch.getMethod()).thenReturn(method2);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButDifferentArgumentsDoesNotMatch() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = mock(Invocation.class);
        Method method = new Method("testMethod", new Class[0]);
        Object[] arguments1 = new Object[] {"arg1", "arg2"};
        Object[] arguments2 = new Object[] {"arg1", "arg3"};
        when(invocation.getMethod()).thenReturn(method);
        when(invocationToMatch.getMethod()).thenReturn(method);
        when(invocationToMatch.getArguments()).thenReturn(arguments2);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButSameArgumentsMatches() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = mock(Invocation.class);
        Method method = new Method("testMethod", new Class[0]);
        Object[] arguments1 = new Object[] {"arg1", "arg2"};
        Object[] arguments2 = new Object[] {"arg1", "arg2"};
        when(invocation.getMethod()).thenReturn(method);
        when(invocationToMatch.getMethod()).thenReturn(method);
        when(invocationToMatch.getArguments()).thenReturn(arguments1);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation invocationToMatch = mock(Invocation.class);
        Method method1 = new Method("testMethod1", new Class[0]);
        Method method2 = new Method("testMethod2", new Class[0]);
        Method method3 = new Method("testMethod", new Class[0]);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationToMatch.getMethod()).thenReturn(method2);
        assertFalse(matcher.hasSameMethod(invocationToMatch));
        when(invocationToMatch.getMethod()).thenReturn(method3);
        assertTrue(matcher.hasSameMethod(invocationToMatch));
        when(invocationToMatch.getMethod()).thenReturn(method1);
        assertFalse(matcher.hasSameMethod(invocationToMatch));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation invocationToMatch = mock(Invocation.class);
        Method method1 = new Method("testMethod1", new Class[0]);
        Method method2 = new Method("testMethod", new Class[0]);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationToMatch.getMethod()).thenReturn(method2);
        assertFalse(matcher.hasSimilarMethod(invocationToMatch));
        when(invocationToMatch.getArguments()).thenReturn(new Object[0]);
        assertFalse(matcher.hasSimilarMethod(invocationToMatch));
        Object[] arguments = new Object[] {"arg1"};
        when(invocationToMatch.getArguments()).thenReturn(arguments);
        assertTrue(matcher.hasSimilarMethod(invocationToMatch));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        String expected = "InvocationMatcher:\nInvocation:\nMethod: public void testMethod()\nArguments: []\nMatchers:\n";
        assertEquals(expected, matcher.toString());
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argumentsWithValues("arg1", "arg2"));
        matcher = new InvocationMatcher(invocation, matchers);
        expected = "InvocationMatcher:\nInvocation:\nMethod: public void testMethod()\nArguments: []\nMatchers:\nMatcher: ArgumentsMatcher: arguments are equal to: [arg1, arg2]\n";
        assertEquals(expected, matcher.toString());
    }
}