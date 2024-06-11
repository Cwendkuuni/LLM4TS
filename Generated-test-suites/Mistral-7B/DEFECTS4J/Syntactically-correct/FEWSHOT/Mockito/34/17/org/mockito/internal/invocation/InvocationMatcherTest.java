package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.VerificationException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testNewInstance() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertEquals(invocation.getInvocation(), matcher.getInvocation());
        assertEquals(Collections.emptyList(), matcher.getMatchers());
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        Matcher<Object> argMatcher = argThat(isInstanceOf(String.class));
        List<Matcher> matchers = Collections.singletonList(argMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertEquals(invocation.getInvocation(), matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testMatchesWhenArgumentsMatch() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Matcher<Object> argMatcher = argThat(isInstanceOf(String.class));
        List<Matcher> matchers = Collections.singletonList(argMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation actual = new Invocation(new Object(), invocation.getMethod(), new Object[0]);

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatchesWhenArgumentsDontMatch() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Matcher<Object> argMatcher = argThat(isInstanceOf(Integer.class));
        List<Matcher> matchers = Collections.singletonList(argMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation actual = new Invocation(new Object(), invocation.getMethod(), new Object[0]);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWhenDifferentMocks() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(invocation2));
    }

    @Test(expected = VerificationException.class)
    public void testMatchesThrowsVerificationExceptionWhenOverloaded() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod2", new Class[0]), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        matcher.matches(invocation2);
    }

    @Test
    public void testMatchesWhenOverloadedButSameArguments() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod2", new Class[0]), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSimilarMethod(invocation2));
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Matcher<Object> argMatcher = argThat(isInstanceOf(String.class));
        List<Matcher> matchers = Collections.singletonList(argMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("invocation: %s", invocation.toString(printSettings));
        String actual = matcher.toString(printSettings);

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testToStringWithCustomMatcher() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Matcher<Object> argMatcher = argThat(new CustomMatcher());
        List<Matcher> matchers = Collections.singletonList(argMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("invocation: %s%narguments:%n[%s]", invocation.toString(printSettings), argMatcher.description());
        String actual = matcher.toString(printSettings);

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Matcher<Object> argMatcher = argThat(isInstanceOf(String.class));
        List<Matcher> matchers = Collections.singletonList(argMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[1];

        matcher.captureArgumentsFrom(new Invocation(new Object(), invocation.getMethod(), arguments));

        assertEquals(arguments[0], argMatcher.getActual());
    }

    @Test
    public void testCaptureArgumentsFromThrowsNullPointerExceptionWhenNullInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]));

        assertNull(matcher.getInvocation().getArguments());

        try {
            matcher.captureArgumentsFrom(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod2", new Class[0]), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWhenSameMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(new Object(), invocation1.getMethod(), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testLocation() {
        Location location = new Location("TestClass.java", 12, 34);
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Object[0], location);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(location, matcher.getLocation());
    }

    private static Matcher<Object> isInstanceOf(Class<?> clazz) {
        return argThat(Matchers.instanceOf(clazz));
    }

    private static class CustomMatcher implements Matcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return argument.toString().equals("custom");
        }

        @Override
        public String description() {
            return "custom";
        }
    }
}