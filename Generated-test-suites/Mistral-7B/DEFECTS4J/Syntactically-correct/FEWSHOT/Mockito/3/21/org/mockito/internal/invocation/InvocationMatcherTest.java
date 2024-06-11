package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsAsserter.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testEmptyMatcher() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);
        assertTrue(emptyMatcher.matches(invocation));
        assertTrue(emptyMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testMatcherWithSingleMatcher() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object arg = new Object();
        Matcher<Object> singleMatcher = Matchers.is(arg);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(singleMatcher));
        Object[] args = new Object[1];
        args[0] = arg;
        assertTrue(matcher.matches(new DescribedInvocation(invocation, null, null) {
            @Override
            public Object[] getArguments() {
                return args;
            }
        }));
        assertTrue(matcher.hasSameMethod(invocation));
    }

    @Test
    public void testMatcherWithMultipleMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Matcher<Object> arg1Matcher = Matchers.is(arg1);
        Matcher<Object> arg2Matcher = Matchers.is(arg2);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(arg1Matcher, arg2Matcher));
        Object[] args = new Object[2];
        args[0] = arg1;
        args[1] = arg2;
        assertTrue(matcher.matches(new DescribedInvocation(invocation, null, null) {
            @Override
            public Object[] getArguments() {
                return args;
            }
        }));
        assertTrue(matcher.hasSameMethod(invocation));
    }

    @Test
    public void testMatcherWithNullInvocation() {
        List<Matcher<Object>> matchers = Arrays.asList(Matchers.is(new Object()));
        InvocationMatcher matcher = new InvocationMatcher(null, matchers);
        assertNull(matcher);
    }

    @Test
    public void testMatcherWithNullMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, null);
        assertNull(matcher);
    }

    @Test
    public void testMatcherWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation1.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("toString")) {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }
        });
        Mockito.when(invocation2.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("matches")) {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[1];
            }
        });
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertFalse(matcher.matches(invocation2));
        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testMatcherWithSameMethodButDifferentArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = new Method("toString", new Class[0]) {
        };
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Matcher<Object> arg1Matcher = Matchers.is(arg1);
        Matcher<Object> arg2Matcher = Matchers.is(arg2);
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Arrays.asList(arg1Matcher, arg2Matcher));
        Object[] args1 = new Object[2];
        args1[0] = arg1;
        args1[1] = arg2;
        Object[] args2 = new Object[2];
        args2[0] = arg2;
        args2[1] = arg1;
        Invocation invocationWithReversedArguments = new DescribedInvocation(invocation1, null, null) {
            @Override
            public Object[] getArguments() {
                return args2;
            }
        };
        assertFalse(matcher.matches(invocationWithReversedArguments));
        assertFalse(matcher.hasSameMethod(invocationWithReversedArguments));
    }

    @Test
    public void testMatcherWithSameMethodAndSameArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object arg = new Object();
        Matcher<Object> argMatcher = Matchers.is(arg);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(argMatcher));
        Object[] args = new Object[1];
        args[0] = arg;
        assertTrue(matcher.matches(new DescribedInvocation(invocation, null, null) {
            @Override
            public Object[] getArguments() {
                return args;
            }
        }));
        assertTrue(matcher.hasSameMethod(invocation));
    }

    @Test
    public void testCaptureArguments() throws InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[2];
        Object[] capturedArguments = new Object[2];
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher<Object> arg1Matcher = Mockito.mock(Matcher.class);
        Matcher<Object> arg2Matcher = Mockito.mock(Matcher.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(arg1Matcher, arg2Matcher));
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getLocation()).thenReturn(location);
        Mockito.when(arg1Matcher.matches(arguments[0]))
                .thenReturn(true)
                .thenReturn(true)
                .thenThrow(new AssertionError("expected failure"));
        Mockito.when(arg2Matcher.matches(arguments[1]))
                .thenReturn(true)
                .thenReturn(true);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        Mockito.when(arg1Matcher.captureFrom(Mockito.anyObject())).thenReturn(capturesArguments);
        Mockito.when(arg2Matcher.captureFrom(Mockito.anyObject())).thenReturn(capturesArguments);
        matcher.captureArgumentsFrom(invocation);
        Object capturedArg1 = capturesArguments.getValue();
        Object capturedArg2 = capturesArguments.getValue();
        Mockito.verify(arg1Matcher).matches(arguments[0]);
        Mockito.verify(arg2Matcher).matches(arguments[1]);
        assertEquals(arg, capturedArg1);
        assertEquals(arg, capturedArg2);
    }

    @Test(expected = AssertionError.class)
    public void testCaptureArgumentsWithFailure() throws InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[2];
        Object[] capturedArguments = new Object[2];
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher<Object> arg1Matcher = Mockito.mock(Matcher.class);
        Matcher<Object> arg2Matcher = Mockito.mock(Matcher.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(arg1Matcher, arg2Matcher));
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getLocation()).thenReturn(location);
        Mockito.when(arg1Matcher.matches(arguments[0]))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenThrow(new AssertionError("expected failure"));
        Mockito.when(arg2Matcher.matches(arguments[1]))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        Mockito.when(arg1Matcher.captureFrom(Mockito.anyObject())).thenReturn(capturesArguments);
        Mockito.when(arg2Matcher.captureFrom(Mockito.anyObject())).thenReturn(capturesArguments);
        matcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        LinkedList<InvocationMatcher> invocationMatchers = new LinkedList<>();
        invocationMatchers.add(new InvocationMatcher(invocation1));
        invocationMatchers.add(new InvocationMatcher(invocation2));
        List<InvocationMatcher> createdInvocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
        assertEquals(invocationMatchers, createdInvocationMatchers);
    }
}