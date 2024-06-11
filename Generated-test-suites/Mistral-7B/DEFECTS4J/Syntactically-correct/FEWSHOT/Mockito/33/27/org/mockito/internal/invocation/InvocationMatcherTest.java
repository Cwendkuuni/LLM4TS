package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatchers.eq;
import static org.mockito.internal.matchers.ArgumentsMatchers.isA;
import static org.mockito.internal.reporting.PrintSettings.defaultPrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.CaptureArguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocationWithArguments;
import org.mockito.internal.reporting.PrintingFriendlyInvocationWithLocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocationWithTarget;
import org.mockito.internal.verification.VerifiedInvocation;
import org.mockito.internal.verification.VerifiedInvocationWithTarget;
import org.mockito.internal.verification.VerificationUtils;

public class InvocationMatcherTest {

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new VerifiedInvocationWithTarget(mock(Object.class), new MethodTestObject(), "method1", new Object[0]));
        invocations.add(new InvocationOnMock(mock(Object.class), new MethodTestObject(), "method2", new Object[0], new Location(1, 1, "TestClass.java")));

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher matcher : matchers) {
            assertEquals(matcher.getMethod().getName(), "method1");
            assertEquals(matcher.getMatchers().size(), 0);
            assertNull(matcher.getLocation());
        }

        InvocationMatcher matcherWithMatchers = matchers.get(1);
        assertEquals(matcherWithMatchers.getMethod().getName(), "method2");
        assertEquals(matcherWithMatchers.getMatchers().size(), 0);
        assertNotNull(matcherWithMatchers.getLocation());
    }

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Method method1 = new MethodTestObject().getMethod("method1", Object[].class);
        Method method2 = new MethodTestObject().getMethod("method1", Integer.class);

        Invocation invocation1 = new InvocationOnMock(mock, method1, new Object[0], new Location(1, 1, "TestClass.java"));
        Invocation invocation2 = new InvocationOnMock(mock, method2, new Integer[0], new Location(2, 2, "TestClass.java"));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(Object.class);
        Method method1 = new MethodTestObject().getMethod("method1", Object[].class);
        Method method2 = new MethodTestObject().getMethod("method1", Integer.class);

        Invocation invocation1 = new InvocationOnMock(mock, method1, new Object[0], new Location(1, 1, "TestClass.java"));
        Invocation invocation2 = new InvocationOnMock(mock, method2, new Integer[0], new Location(2, 2, "TestClass.java"));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        InvocationMatcher matcher3 = new InvocationMatcher(invocation1);

        assertTrue(matcher1.hasSimilarMethod(matcher1));
        assertTrue(matcher1.hasSimilarMethod(matcher3));
        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertFalse(matcher1.hasSimilarMethod(new Object()));
        assertFalse(matcher1.hasSimilarMethod(null));
        assertFalse(matcher1.hasSimilarMethod(new VerifiedInvocationWithTarget(mock, method1, new Object[0], new Location(3, 3, "TestClass.java"))));
    }

    @Test
    public void testMatches() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Invocation invocation = new InvocationOnMock(mock, method, new Object[0], new Location(1, 1, "TestClass.java"));

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[2];
        arguments[0] = "foo";
        arguments[1] = 1;

        Invocation invocationToMatch = new InvocationOnMock(mock, method, arguments, new Location(2, 2, "TestClass.java"));

        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Invocation invocation = new InvocationOnMock(mock, method, new Object[0], new Location(1, 1, "TestClass.java"));

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettingsString = "PrintSettings{printInline = false, printStacktrace = false, printRefs = false, printStacktraceOptions = [class java.lang.reflect.Method, class org.mockito.internal.debugging.Location], printCapturedValues = false}";
        String expected = String.format("Invocation [%s.%s(%s)%n%s]", mock.getClass().getName(), method.getName(),
                ArgumentsComparator.toString(matchers.toArray(new Matcher[0])), printSettingsString);

        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Invocation invocation = new InvocationOnMock(mock, method, new Object[0], new Location(1, 1, "TestClass.java"));

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[2];
        arguments[0] = "foo";
        arguments[1] = 1;

        Invocation invocationToMatch = new InvocationOnMock(mock, method, arguments, new Location(2, 2, "TestClass.java"));

        CaptureArguments captureArguments = mock(CaptureArguments.class);
        matcher.captureArgumentsFrom(invocationToMatch);

        assertEquals("foo", captureArguments.getValue(0));
        assertEquals(1, captureArguments.getValue(1).intValue());
    }

    @Test
    public void testToStringWithPrintingFriendlyInvocation() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Invocation invocation = new PrintingFriendlyInvocationWithTarget(new PrintingFriendlyInvocationWithArguments(mock, method, new Object[0]), new Location(1, 1, "TestClass.java"));

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettingsString = "PrintSettings{printInline = false, printStacktrace = false, printRefs = false, printStacktraceOptions = [class java.lang.reflect.Method, class org.mockito.internal.debugging.Location], printCapturedValues = false}";

        String expected = String.format("Invocation [%s.%s(%s)%n%s]", "PrintingFriendlyInvocationWithTarget", method.getName(),
                ArgumentsComparator.toString(matchers.toArray(new Matcher[0])), printSettingsString);

        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testToStringWithPrintingFriendlyInvocationWithArguments() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Object[] arguments = new Object[2];
        arguments[0] = "foo";
        arguments[1] = 1;

        Invocation invocation = new PrintingFriendlyInvocationWithArguments(mock, method, arguments);

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettingsString = "PrintSettings{printInline = false, printStacktrace = false, printRefs = false, printStacktraceOptions = [class java.lang.reflect.Method, class org.mockito.internal.debugging.Location], printCapturedValues = false}";

        String expected = String.format("Invocation [%s.%s(%s, %s, %s)%n%s]", "PrintingFriendlyInvocationWithArguments", method.getName(),
                "arg0", "arg1",
                Arrays.toString(new Object[]{arguments[0], arguments[1]}), printSettingsString);

        assertEquals(expected, matcher.toString(defaultPrintSettings()));
    }

    @Test
    public void testToStringWithPrintingFriendlyInvocationWithLocation() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocation = new PrintingFriendlyInvocationWithLocation(new PrintingFriendlyInvocationWithArguments(mock, method, new Object[0]), location);

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettingsString = "PrintSettings{printInline = false, printStacktrace = false, printRefs = false, printStacktraceOptions = [class java.lang.reflect.Method, class org.mockito.internal.debugging.Location], printCapturedValues = false}";

        String expected = String.format("Invocation [%s.%s(%s, %s)%n%s@%s]", "PrintingFriendlyInvocationWithLocation", method.getName(),
                "arg0",
                Arrays.toString(new Object[]{VerificationUtils.getActualArgs(invocation)}), printSettingsString, location.getLineNumber());

        assertEquals(expected, matcher.toString(defaultPrintSettings()));
    }

    @Test
    public void testToStringWithVerifiedInvocation() {
        Object mock = mock(Object.class);
        Method method = new MethodTestObject().getMethod("method1", Object[].class);

        Object[] arguments = new Object[2];
        arguments[0] = "foo";
        arguments[1] = 1;

        VerifiedInvocation invocation = new VerifiedInvocationWithTarget(mock, method, arguments, new Location(1, 1, "TestClass.java"));

        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentMatcher<Object[]> arg) -> arg.length == 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettingsString = "PrintSettings{printInline = false, printStacktrace = false, printRefs = false, printStacktraceOptions = [class java.lang.reflect.Method, class org.mockito.internal.debugging.Location], printCapturedValues = false}";

        String expected = String.format("Invocation [%s.%s(%s, %s)%n%s@%s]", "VerifiedInvocationWithTarget", method.getName(),
                "arg0",
                Arrays.toString(new Object[]{VerificationUtils.getActualArgs(invocation)}), printSettingsString,
                VerificationUtils.getCallSite(invocation).getMethodName());

        assertEquals(expected, matcher.toString(defaultPrintSettings()));
    }

    public static class MethodTestObject {
        public Method method1(Object[] args) {
            return null;
        }
    }
}