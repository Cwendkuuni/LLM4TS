package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatchers.args;
import static org.mockito.internal.matchers.ArgumentsMatchers.eq;
import static org.mockito.internal.matchers.ArgumentsMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentsMatchers.same;
import static org.mockito.internal.matchers.ArgumentsMatchers.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;
    private Invocation invocation;
    private List<Matcher> matchers;
    private ArgumentCaptor<Object> argumentCaptor;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setUp() {
        mock = Mockito.mock(Object.class);
        invocation = Mockito.mock(Invocation.class);
        matchers = Collections.singletonList(eq(1));
        argumentsComparator = new ArgumentsComparator();
    }

    @Test
    public void testInvocationMatcherConstructionWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructionWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.method(Object.class, "test");
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMethod(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Collections.singletonList(eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String methodName = "test";
        String className = "ClassUnderTest";
        String printSettingsString = "printSettingsString";

        Method method = Mockito.method(Object.class, methodName);
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMethod(method);
        invocation.setLocation(new Location(className, 1, 1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintActualInvocation(true);

        String expected = String.format("%s(%s)%n", methodName, printSettingsString);
        String actual = invocationMatcher.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.matches(invocation1));
        assertTrue(invocationMatcher1.matches(invocation2));
        assertFalse(invocationMatcher1.matches(Mockito.mock(Invocation.class)));

        assertFalse(invocationMatcher2.matches(invocation1));
        assertFalse(invocationMatcher2.matches(invocation2));
        assertFalse(invocationMatcher2.matches(Mockito.mock(Invocation.class)));

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher3.matches(invocation2));
        assertFalse(invocationMatcher4.matches(invocation1));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1, 2};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = Mockito.method(Object.class, "test");
        Method method2 = Mockito.method(Object.class, "test2");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(same(method1)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(same(method2)));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        method1 = invocation1.getMethod();
        method2 = invocation2.getMethod();

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        method1 = invocation1.getMethod();
        method2 = invocationMatcher2.getMethod();

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        Method method3 = Mockito.method(Object.class, "test");
        Invocation invocation3 = Mockito.mock(Invocation.class);
        invocation3.setMethod(method3);

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocationMatcher1);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation3);

        assertFalse(invocationMatcher3.hasSimilarMethod(invocationMatcher4));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = Mockito.method(Object.class, "test");
        Method method2 = Mockito.method(Object.class, "test");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(same(method1)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(same(method2)));

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));

        method1 = invocation1.getMethod();
        method2 = invocation2.getMethod();

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        String className = "ClassUnderTest";
        int lineNumber = 1;
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setLocation(new Location(className, lineNumber, lineNumber));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(new Location(className, lineNumber, lineNumber), invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        String methodName = "test";
        String className = "ClassUnderTest";
        String printSettingsString = "printSettingsString";

        Method method = Mockito.method(Object.class, methodName);
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMethod(method);
        invocation.setLocation(new Location(className, 1, 1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintActualInvocation(true);

        String expected = String.format("%s(%s)%n%s(%s)%n", methodName, printSettingsString, className, 1);
        String actual = invocationMatcher.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[]{1, 2};
        ArgumentCaptor<Object> argumentCaptor = argThat(isNull());

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(argumentCaptor).capture(args(actualArguments[0]));
        verify(argumentCaptor).capture(args(actualArguments[1]));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertNotNull(invocationMatcher1);
        assertNotNull(invocationMatcher2);
    }
}