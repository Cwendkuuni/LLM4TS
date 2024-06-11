package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.nullValue;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.typeEq;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Mock
    private Object mock;

    @Test
    public void testInvocationMatcherWithNoMatchers() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[0]), new Object[0], new Location(1, 2), new String[]{"a", "b"}, null);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        List<Matcher> matchers = Arrays.asList(typeEq(String.class), argThat(Matchers.equalTo("test")));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);

        // when
        Method method = invocationMatcher.getMethod();

        // then
        assertNotNull(method);
        assertEquals(method.getName(), "methodName"); // replace with the actual method name
    }

    @Test
    public void testMatches() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        Invocation otherInvocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test2", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean result = invocationMatcher.matches(invocation);
        boolean result2 = invocationMatcher.matches(otherInvocation);

        // then
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);

        // when
        boolean result = invocationMatcher.safelyArgumentsMatch(new Object[]{"test", 1});

        // then
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        Invocation otherInvocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean result = invocationMatcher.hasSimilarMethod(otherInvocation);

        // then
        assertTrue(result);
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        String result = invocationMatcher.toString();

        // then
        assertNotNull(result);
        assertTrue(result.contains("InvocationMatcher"));
    }

    @Test
    public void testToStringWithPrintSettings() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();

        // when
        String result = invocationMatcher.toString(printSettings);

        // then
        assertNotNull(result);
        assertTrue(result.contains("InvocationMatcher"));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", 1}, new Location(1, 2), new String[]{"a", "b"}, null);
        ArgumentCaptor<String> argumentCaptor = argThat(Matchers.isA(String.class));

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        assertEquals("test", argumentCaptor.getValue());
    }

    @Test
    public void testNullValueMatcher() {
        // given
        Invocation invocation = new Invocation(mock, new Method(null, new Class[]{String.class, Integer.TYPE}), new Object[]{"test", null}, new Location(1, 2), new String[]{"a", "b"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean result = invocationMatcher.matches(invocation);

        // then
        assertTrue(result);
    }
}