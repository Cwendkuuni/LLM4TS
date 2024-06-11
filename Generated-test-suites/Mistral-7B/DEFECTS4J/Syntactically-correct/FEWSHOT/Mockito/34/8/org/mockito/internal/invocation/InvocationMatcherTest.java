package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private Object mock;
    private Method method;
    private Invocation invocation;
    private InvocationMatcher matcher;
    private CapturesArguments captor;
    private ArgumentsComparator comparator;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
        method = Mockito.method(String.class, "length");
        invocation = new Invocation(mock, method, new Object[0], new Location(1, 1, "test.java"));
        matcher = new InvocationMatcher(invocation);
        captor = Mockito.argumentCaptor(String.class);
        comparator = new ArgumentsComparator();
    }

    @Test
    public void testNewInstanceWithMatchers() {
        List<Matcher> matchers = Arrays.asList(argThat(type(String.class)), isNull());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testGetMethod() {
        Method method = matcher.getMethod();

        assertTrue(method.equals(this.method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = matcher.getInvocation();

        assertTrue(invocation.equals(this.invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = matcher.getMatchers();

        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();
        String result = matcher.toString(printSettings);

        assertTrue(result.startsWith("Mockito invocation:"));
    }

    @Test
    public void testMatchesSameInvocation() {
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethod() {
        Invocation otherInvocation = new Invocation(mock, method, new Object[0], new Location(2, 1, "test2.java"));
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodSameArguments() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation otherInvocation = new Invocation(mock, method, arguments, new Location(2, 1, "test2.java"));

        assertTrue(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodDifferentArguments() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation otherInvocation = new Invocation(mock, method, new Object[]{1, 4, 3}, new Location(2, 1, "test2.java"));

        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMethod() {
        Method otherMethod = Mockito.method(String.class, "concat", String.class, String.class);
        Invocation otherInvocation = new Invocation(mock, otherMethod, new Object[0], new Location(2, 1, "test2.java"));

        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMock() {
        Object otherMock = Mockito.mock(Integer.class);
        Invocation otherInvocation = new Invocation(otherMock, method, new Object[0], new Location(2, 1, "test2.java"));

        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation otherInvocation = new Invocation(mock, method, new Object[0], new Location(2, 1, "test2.java"));

        assertTrue(matcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation otherInvocation = new Invocation(mock, Mockito.method(String.class, "length", Integer.TYPE), new Object[0], new Location(2, 1, "test2.java"));

        assertTrue(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasSimilarMethodButDifferentArguments() {
        Invocation otherInvocation = new Invocation(mock, method, new Object[]{1}, new Location(2, 1, "test2.java"));

        assertFalse(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        matcher.captureArgumentsFrom(invocation);

        Object capturedArgument = captor.getValue();
        assertTrue(capturedArgument instanceof Object[]);
        assertArrayEquals(((Object[]) capturedArgument), invocation.getArguments());
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithMatchers() {
        List<Matcher> matchers = Arrays.asList(argThat(same(1)), argThat(same(2)));
        matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);

        Object[] capturedArguments = captor.getAllValues();
        assertArrayEquals(new Object[]{invocation.getArguments()[0], invocation.getArguments()[1]}, capturedArguments);
    }

    @Test(expected = RuntimeException.class)
    public void testArgumentsComparisonWithDifferentTypes() {
        method = Mockito.method(Object.class, "equals", Object.class);
        Invocation otherInvocation = new Invocation(mock, method, new Object[]{1}, new Location(2, 1, "test2.java"));

        matcher = new InvocationMatcher(invocation);

        matcher.captureArgumentsFrom(otherInvocation);
    }

    @Test
    public void testToStringWithEmptyMatchers() {
        PrintSettings printSettings = new PrintSettings();
        String result = matcher.toString(printSettings);

        assertTrue(result.contains("no argument matchers"));
    }

    @Test
    public void testToStringWithMatchers() {
        List<Matcher> matchers = Arrays.asList(argThat(type(String.class)), argThat(same(1)));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String result = matcher.toString(printSettings);

        assertTrue(result.contains("with arguments:"));
        assertTrue(result.contains("arg that:"));
        assertTrue(result.contains("type: java.lang.String"));
        assertTrue(result.contains("== 1"));
    }

    @Test
    public void testToStringWithMatchersAndCustomFormat() {
        List<Matcher> matchers = Arrays.asList(argThat(type(String.class)), argThat(same(1)));
        PrintSettings printSettings = new PrintSettings("{0} invocation: method={1} arguments={2}");
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String result = matcher.toString(printSettings);

        assertTrue(result.contains("invocation: method=length arguments="));
        assertTrue(result.contains("arg that: type=java.lang.String"));
        assertTrue(result.contains("arg that: == 1"));
    }

    @Test
    public void testToStringWithMatchersAndEmptyFormat() {
        List<Matcher> matchers = Arrays.asList(argThat(type(String.class)), argThat(same(1)));
        PrintSettings printSettings = new PrintSettings("");
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String result = matcher.toString(printSettings);

        assertTrue(result.contains("Mockito invocation:"));
        assertTrue(result.contains("with arguments:"));
        assertTrue(result.contains("arg that:"));
        assertTrue(result.contains("type: java.lang.String"));
        assertTrue(result.contains("== 1"));
    }
}