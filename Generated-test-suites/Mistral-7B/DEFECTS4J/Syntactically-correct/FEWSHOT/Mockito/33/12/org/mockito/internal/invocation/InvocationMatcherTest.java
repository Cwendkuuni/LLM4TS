package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.argThat;
import static org.mockito.internal.matchers.Equals.equalTo;
import static org.mockito.internal.matchers.TypeSafeMatcher.instanceOf;
import static org.mockito.internal.reporting.PrintSettings.printEmpty;
import static org.mockito.internal.reporting.PrintSettings.printError;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotVerifiedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.MockitoStub;

public class InvocationMatcherTest {

    @Test
    public void testCreateFrom() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher1 = new InvocationMatcher(new InvocationOnMock(mock).withMethod(Object.class.getMethod("toString")), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(new InvocationOnMock(mock).withMethod(Object.class.getMethod("toString")), Collections.<Matcher>emptyList());

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(matcher1, matcher2));
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0) == matcher1);
        assertTrue(matchers.get(1) == matcher2);
    }

    @Test
    public void testHasSameMethod() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher1 = new InvocationMatcher(new InvocationOnMock(mock).withMethod(Object.class.getMethod("toString")), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(new InvocationOnMock(mock).withMethod(Object.class.getMethod("hashCode")), Collections.<Matcher>emptyList());

        assertTrue(matcher1.hasSameMethod(matcher1));
        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testMatches() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)), Collections.<Matcher>emptyList());

        InvocationOnMock invocation1 = new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)).withArguments(new Object[]{"foo", "bar"});
        InvocationOnMock invocation2 = new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)).withArguments(new Object[]{"bar", "foo"});

        assertTrue(matcher.matches(invocation1));
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesWithArguments() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)), Arrays.asList(argThat(instanceOf(String.class)), equalTo("foo")));

        InvocationOnMock invocation = new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)).withArguments(new Object[]{"foo", "bar"});
        assertTrue(matcher.matches(invocation));
    }

    @Test(expected = InvocationNotVerifiedException.class)
    public void testMatchesWithUnverifiedInvocation() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(instanceOf(String.class)));
        mock.reset();
        mock.verifyNoMoreInteractions();
        matcher.matches(new InvocationOnMock(mock).withMethod(String.class.getMethod("hashCode")));
    }

    @Test
    public void testToString() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)), Collections.<Matcher>emptyList());

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintEmpty(printEmpty);
        printSettings.setPrintError(printError);

        assertEquals("Invocation on mock [MockitoStub@1b4e65c]", matcher.toString());
        assertEquals("Invocation on mock [MockitoStub@1b4e65c]\n" +
                "\tmethod: concat(java.lang.String)\n" +
                "\targuments: []", matcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        MockitoStub mock = new MockitoStub();
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)), Collections.<Matcher>emptyList());
        Object arg = new Object();

        matcher.captureArgumentsFrom(new InvocationOnMock(mock).withMethod(String.class.getMethod("concat", String.class)).withArguments(new Object[]{"foo", arg}));

        assertEquals(arg, matcher.matchers.get(0).getRealValue());
    }
}