package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.closeTo;
import static org.mockito.internal.matchers.ArgumentsMatcher.contains;
import static org.mockito.internal.matchers.ArgumentsMatcher.eq;
import static org.mockito.internal.matchers.ArgumentsMatcher.isA;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.startsWith;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CaptureArgumentMatchers;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private Object mock;
    private InvocationMatcher invocationMatcher;
    private InvocationMatcher invocationMatcherWithMatchers;
    private Invocation invocation;
    private Invocation anotherInvocation;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
        invocation = Mockito.on(mock).invoke();
        anotherInvocation = Mockito.on(mock).invoke();

        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcherWithMatchers = new InvocationMatcher(invocation, Arrays.asList(eq("foo"), isA(String.class)));
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertTrue(method.getDeclaringClass().isInstance(mock.getClass()));
        assertTrue(method.getName().equals("invoke"));
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcherWithMatchers.getMatchers();
        assertTrue(matchers.size() == 2);
        assertTrue(matchers.get(0).matches("foo"));
        assertTrue(matchers.get(1).matchesAny());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocationMatcher.toString(printSettings);
        String toStringWithMatchers = invocationMatcherWithMatchers.toString(printSettings);

        assertTrue(toString.startsWith("Invocation on " + mock.getClass().getName() + " mock."));
        assertTrue(toStringWithMatchers.startsWith("Invocation on " + mock.getClass().getName() + " mock."));
        assertTrue(toStringWithMatchers.contains("arguments"));
        assertTrue(toStringWithMatchers.contains("matchers:"));
    }

    @Test
    public void testMatches() {
        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{"foo", "bar"};
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocationWithSameMethod = Mockito.on(mock).invoke(1);
        Invocation anotherInvocationWithDifferentMethod = Mockito.on(mock).toString();

        assertTrue(invocationMatcher.hasSimilarMethod(anotherInvocationWithSameMethod));
        assertFalse(invocationMatcher.hasSimilarMethod(anotherInvocationWithDifferentMethod));
    }

    @Test
    public void testHasSameMethod() {
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher));
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcherWithMatchers));
        Method method = invocationMatcher.getMethod();
        InvocationMatcher sameMethodInvocationMatcher = new InvocationMatcher(invocationMatcher.getInvocation(),
                invocationMatcher.getMatchers());
        InvocationMatcher differentMethodInvocationMatcher = new InvocationMatcher(new InvocationOnMock(method),
                invocationMatcher.getMatchers());
        assertFalse(invocationMatcher.hasSameMethod(sameMethodInvocationMatcher));
        assertFalse(invocationMatcher.hasSameMethod(differentMethodInvocationMatcher));
    }

    @Test
    public void testGetLocation() {
        Location location = invocationMatcher.getLocation();
        assertNotNull(location);
        assertTrue(location.getLine() > 0);
        assertTrue(location.getFile().startsWith(""));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setShowStackFrames(true);
        String toString = invocationMatcher.toString(printSettings);
        String toStringWithMatchers = invocationMatcherWithMatchers.toString(printSettings);

        assertTrue(toString.startsWith("Invocation on " + mock.getClass().getName() + " mock."));
        assertTrue(toStringWithMatchers.startsWith("Invocation on " + mock.getClass().getName() + " mock."));
        assertTrue(toStringWithMatchers.contains("arguments"));
        assertTrue(toStringWithMatchers.contains("matchers:"));
        assertTrue(toString.contains("at " + invocation.getLocation().getFile()));
        assertTrue(toStringWithMatchers.contains("at " + invocation.getLocation().getFile()));
        assertTrue(toStringWithMatchers.contains("Line " + invocation.getLocation().getLine()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CaptureArgumentMatchers captureArgumentMatchers = new CaptureArgumentMatchers();
        invocationMatcherWithMatchers.captureArgumentsFrom(anotherInvocation.getArguments());
        List<Object> capturedArgs = captureArgumentMatchers.getArguments();

        assertTrue(capturedArgs.size() == 2);
        assertFalse(capturedArgs.get(0).equals("foo"));
        assertTrue(capturedArgs.get(1) instanceof String);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);
        invocations.add(anotherInvocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertTrue(invocationMatchers.size() == 2);
        assertTrue(invocationMatchers.get(0).matches(invocation));
        assertTrue(invocationMatchers.get(1).matches(anotherInvocation));
    }

    @Test
    public void testMatchesWithDifferentTypes() {
        InvocationMatcher invocationMatcherWithDifferentTypes = new InvocationMatcher(invocation, Arrays.asList( sameInstance(String.class), isNull()));
        Invocation anotherInvocationWithObject = Mockito.on(mock).invoke(new Object());

        assertFalse(invocationMatcherWithDifferentTypes.matches(anotherInvocationWithObject));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        Method anotherMethod = mock.getClass().getMethod("test");
        Invocation anotherInvocationWithDifferentMethod = Mockito.on(mock).invoke(anotherMethod);

        InvocationMatcher invocationMatcherWithDifferentMethod = new InvocationMatcher(invocation, Arrays.asList(eq("foo")));

        assertFalse(invocationMatcherWithDifferentMethod.matches(anotherInvocationWithDifferentMethod));
    }

    @Test
    public void testMatchesWithNullInvocation() {
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(null, Arrays.asList(eq("foo")));

        assertNull(invocationMatcherWithMatchers.getMethod());
        assertNull(invocationMatcherWithMatchers.getInvocation());
        assertFalse(invocationMatcherWithMatchers.matches(invocation));
    }

    @Test
    public void testMatchesWithNullMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);

        assertNull(invocationMatcher.getMatchers());
        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesWithEmptyMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesWithComplexMatchers() {
        InvocationMatcher invocationMatcherWithComplexMatchers = new InvocationMatcher(invocation, Arrays.asList(
                contains("foo"),
                not(contains("bar")),
                startsWith("test"),
                closeTo(1.0D, 0.1D)
        ));

        Object[] actualArgs = new Object[]{"foo", "bar1", 1.2D};

        assertTrue(invocationMatcherWithComplexMatchers.matches(new InvocationOnMock(getMethod()), actualArgs));
        assertFalse(invocationMatcherWithComplexMatchers.matches(new InvocationOnMock(getMethod()), new Object[]{"bar", "foo", 1.5D}));
    }
}