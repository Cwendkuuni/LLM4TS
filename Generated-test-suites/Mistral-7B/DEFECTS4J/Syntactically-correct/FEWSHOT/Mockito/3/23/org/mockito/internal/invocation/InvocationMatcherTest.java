package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsEqual;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsMatch;
import static org.mockito.internal.matchers.ArgumentsMatcher.isCollectionWithSize;
import static org.mockito.internal.matchers.ArgumentsMatcher.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.isSame;
import static org.mockito.internal.matchers.ArgumentsMatcher.isVararg;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    private Mockito mockito;
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        mockito = Mockito.mock(Mockito.class);
        invocation = Mockito.mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = Arrays.asList(isInstanceOf(String.class), isSame(mockito));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = print(matchers, invocation);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesSameInvocation() {
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentMethodName() {
        Invocation invocation2 = Mockito.mock(Invocation.class);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesSameMethodNameButDifferentMock() {
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesSameMethodNameButUnverified() {
        Invocation invocation2 = Mockito.mock(Invocation.class);
        assertFalse(invocationMatcher.matches(invocation2));
        Mockito.verifyNoInteractions(invocation2.getMock());
        assertTrue(invocationMatcher.matches(invocationMatcher.getInvocation()));
        assertTrue(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesSameMethodNameButDifferentArguments() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(isInstanceOf(String.class), arguments));
        assertFalse(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesSameMethodNameButSameArguments() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(isInstanceOf(String.class), arguments));
        assertTrue(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Mockito.method(Object.class, "method", new Class[]{int.class, int[].class});
        Method method2 = Mockito.method(Object.class, "method", new Class[]{int[].class, int.class});

        Invocation invocation1 = Mockito.mock(Invocation.class);
        invocation1.setMethod(method1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        invocation2.setMethod(method2);

        assertTrue(invocationMatcher.hasSameMethod(invocation1));
        assertTrue(invocationMatcher.hasSameMethod(invocation2));

        assertFalse(invocationMatcher.hasSameMethod(Mockito.mock(Invocation.class)));
    }

    @Test
    public void testHasSimilarMethodButDifferentArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation1));
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodButSameArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation1));
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = Arrays.asList(isInstanceOf(String.class), isNull(), isCollectionWithSize(1));
        Object[] rawArguments = new Object[]{1, "foo", Collections.singletonList(1)};

        invocationMatcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                if (i < invocation.getRawArguments().length) {
                    Object captured = capturesArguments.getValue();
                    assertEquals(invocation.getArgumentAt(i, Object.class), captured);
                } else {
                    assertNull(capturesArguments.getValue());
                }
            }
        }
    }

    @Test
    public void testCaptureArgumentsFromVarargs() {
        List<Matcher> matchers = Arrays.asList(isInstanceOf(String.class), isNull(), isInstanceOf(List.class), isVararg());
        Object[] rawArguments = new Object[]{1, "foo", new Object[]{1, 2, 3}, "bar"};

        invocationMatcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                if (i < invocation.getRawArguments().length) {
                    Object captured = capturesArguments.getValue();
                    if (i < invocation.getRawArguments().length - 2) {
                        assertEquals(invocation.getArgumentAt(i, Object.class), captured);
                    } else {
                        assertArrayEquals(new Object[]{invocation.getArgumentAt(i - 1, Object[].class)}, captured);
                    }
                } else {
                    assertNull(capturesArguments.getValue());
                }
            }
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}