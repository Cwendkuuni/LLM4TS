package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.argThat;
import static org.mockito.internal.matchers.ArgumentCaptor.forClass;
import static org.mockito.internal.matchers.Equals.equalTo;
import static org.mockito.internal.matchers.TypeSafety.notNull;
import static org.mockito.internal.reporting.PrintSettings.printSettings;
import static org.mockito.internal.reporting.PrintSettings.suppress;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoBaseException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocation.PrintingFriendly;
import org.mockito.internal.verification.Invocations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Location location;
    private List<Matcher> matchers;
    private Stubber stubber;
    private Invocation invocation;
    private Invocations invocations;

    @Before
    public void setup() {
        stubber = org.mockito.Mockito.mock(Stubber.class);
        invocation = mockInvocation();
        invocations = new Invocations("");
        Matcher mockMatcher = argThat(notNull(InvocationMatcher.class));
        matchers = new LinkedList<>();
        matchers.add(mockMatcher);
        matcher = new InvocationMatcher(invocation, matchers);
        location = new Location("Test.java", 1, 1);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        String actual = "";
        String expected = "";
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StringWithQuotes() {
        String actual = "\"foo\"";
        String expected = "foo";
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StringWithLeadingAndTrailingQuotes() {
        String actual = "\"foo\"";
        String expected = "foo";
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StringWithQuotesInside() {
        String actual = "\"foo bar\"";
        String expected = "foo bar";
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyArray() {
        Object[] actual = new Object[0];
        Object[] expected = new Object[0];
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_ArrayWithQuotes() {
        Object[] actual = new Object[] {"\"foo\"", "\"bar\""};
        Object[] expected = new Object[] {"foo", "bar"};
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_ArrayWithQuotesInside() {
        Object[] actual = new Object[] {"\"foo bar\"", "\"baz quux\""};
        Object[] expected = new Object[] {"foo bar", "baz quux"};
        assertEquals(expected, matcher.stripLeadingAndTrailingQuotes(actual));
    }

    @Test
    public void testStripLeadingHyphens() {
        String actual = "--foo";
        String expected = "foo";
        assertEquals(expected, matcher.stripLeadingHyphens(actual));
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphen() {
        String actual = "--foo--";
        String expected = "foo";
        assertEquals(expected, matcher.stripLeadingHyphens(actual));
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String actual = "-foo";
        String expected = "foo";
        assertEquals(expected, matcher.stripLeadingHyphens(actual));
    }

    @Test
    public void testStripLeadingHyphens_Null() {
        assertNull(matcher.stripLeadingHyphens(null));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation);
        assertTrue(matcher.hasSameMethod(otherMatcher));
    }

    @Test
    public void testHasSameMethod_DifferentInvocations() {
        Invocation otherInvocation = mockInvocation();
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertFalse(matcher.hasSameMethod(otherMatcher));
    }

    @Test
    public void testHasSameMethod_DifferentMethods() {
        Invocation otherInvocation = mockInvocationWithDifferentMethod();
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertFalse(matcher.hasSameMethod(otherMatcher));
    }

    @Test
    public void testMatches_SameInvocation() {
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_DifferentInvocations() {
        Invocation otherInvocation = mockInvocation();
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertFalse(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_InvocationsWithDifferentArguments() {
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation otherInvocation = new InvocationOnMock(getMock(), getMethod(), arguments, location);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertFalse(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_InvocationsWithSameArguments() {
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation otherInvocation = new InvocationOnMock(getMock(), getMethod(), arguments, location);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertTrue(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_InvocationsWithSameMethodButDifferentArguments() {
        Method otherMethod = getOtherMethod();
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation otherInvocation = new InvocationOnMock(getMock(), otherMethod, arguments, location);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertFalse(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_InvocationsWithSameMethodAndArguments() {
        Method otherMethod = getOtherMethod();
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation otherInvocation = new InvocationOnMock(getMock(), otherMethod, arguments, location);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        assertTrue(matcher.matches(otherMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation otherInvocation = mockInvocation();
        Object[] otherArguments = otherInvocation.getArguments();
        assertTrue(matcher.safelyArgumentsMatch(otherArguments));
    }

    @Test(expected = MockitoBaseException.class)
    public void testSafelyArgumentsMatch_ThrowsException() {
        Invocation otherInvocation = mockInvocationWithThrowableArgument();
        Object[] otherArguments = otherInvocation.getArguments();
        matcher.safelyArgumentsMatch(otherArguments);
    }

    @Test
    public void testToString() {
        String expected = "Invocation [method=public java.lang.String org.apache.commons.cli.HelpFormatter.formatHelp(java.lang.String, org.apache.commons.cli.HelpFormatter$DefaultFormatter,java.lang.PrintStream,java.lang.String, java.lang.String[],java.lang.String[]) throws java.io.IOException,java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException, java.lang.SecurityException, java.lang.IllegalAccessException]\n"
                + "Arguments: []\n"
                + "Location: Test.java:1:1";
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testToString_WithPrintSettings() {
        PrintSettings printSettings = printSettings();
        printSettings.suppress(PrintingFriendly.ARGUMENTS);
        String expected = "Invocation [method=public java.lang.String org.apache.commons.cli.HelpFormatter.formatHelp(java.lang.String, org.apache.commons.cli.HelpFormatter$DefaultFormatter,java.lang.PrintStream,java.lang.String, java.lang.String[],java.lang.String[]) throws java.io.IOException,java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException, java.lang.SecurityException, java.lang.IllegalAccessException]\n"
                + "Location: Test.java:1:1";
        assertEquals(expected, matcher.toString(printSettings));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertTrue(invocationMatchers.size() > 0);
    }

    private Invocation mockInvocation() {
        return new InvocationOnMock(getMock(), getMethod(), new Object[0], location);
    }

    private Invocation mockInvocationWithDifferentMethod() {
        Method otherMethod = getOtherMethod();
        return new InvocationOnMock(getMock(), otherMethod, new Object[0], location);
    }

    private Invocation mockInvocationWithThrowableArgument() {
        Method method = getMethod();
        Object[] arguments = new Object[1];
        arguments[0] = new RuntimeException("Test exception");
        return new InvocationOnMock(getMock(), method, arguments, location);
    }

    private Method getMethod() {
        try {
            return Class.forName("org.apache.commons.cli.HelpFormatter").getMethod("formatHelp", String.class, Class.forName("org.apache.commons.cli.HelpFormatter$DefaultFormatter").asSubclass(Object.class), PrintStream.class, String[].class, String[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method getOtherMethod() {
        try {
            return Class.forName("org.apache.commons.cli.HelpFormatter").getMethod("printHelp", PrintStream.class, String[].class, String[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getMock() {
        return stubber.mock(Object.class);
    }

    private void captureArgumentsFrom(Invocation i) {
        matcher.captureArgumentsFrom(i);
        for (int i1 = 0; i1 < matchers.size(); i1++) {
            Matcher matcher1 = matchers.get(i1);
            if (matcher1 instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher1;
                Object capturedArgument = capturedArguments.getCapturedValue();
                assertEquals(i.getArguments()[i1], capturedArgument);
            }
        }
    }
}