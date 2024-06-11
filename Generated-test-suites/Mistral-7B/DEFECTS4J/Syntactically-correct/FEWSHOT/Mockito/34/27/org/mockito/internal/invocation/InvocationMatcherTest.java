package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.equalTo;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.matches;
import static org.mockito.internal.matchers.ArgsCaptureMatcher.capture;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.VerificationException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintingInvocation;
import org.mockito.internal.reporting.PrintingType;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setup() throws Exception {
        mock = Whitebox.createMock(Object.class);
        stubber = Whitebox.createNiceMock(Stubber.class);
        invocation = new Invocation(new Method("test", Object[].class), new Object[]{MockCreationSettings.defaultSettings().getMock()}, new Location(1, 1, "TestClass.java"));
        matcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
        assertNull(matcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = matcher.getMethod();
        assertEquals(invocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = matcher.getInvocation();
        assertEquals(this.invocation, invocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expected = invocation.toString();
        String result = matcher.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings(PrintingType.HUMAN_READABLE);
        String expected = invocation.toString(matchers, printSettings);
        String result = matcher.toString(printSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchesSameInvocation() {
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMethod() {
        Invocation otherInvocation = new Invocation(new Method("otherTest", Object[].class), new Object[]{MockCreationSettings.defaultSettings().getMock()}, new Location(1, 1, "TestClass.java"));
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodDifferentMock() {
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{new Object(){}}, new Location(1, 1, "TestClass.java"));
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodSameMockButDifferentArguments() {
        Object otherArgument = new Object(){};
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{otherArgument}, new Location(1, 1, "TestClass.java"));
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodSameMockButSameArguments() {
        Object argument = new Object(){};
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{argument}, new Location(1, 1, "TestClass.java"));
        assertTrue(matcher.matches(otherInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{MockCreationSettings.defaultSettings().getMock()}, new Location(1, 1, "TestClass.java"));
        assertTrue(matcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasDifferentMethod() {
        Invocation otherInvocation = new Invocation(new Method("otherTest", Object[].class), new Object[]{MockCreationSettings.defaultSettings().getMock()}, new Location(1, 1, "TestClass.java"));
        assertFalse(matcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{MockCreationSettings.defaultSettings().getMock()}, new Location(1, 1, "TestClass.java"));
        assertTrue(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasDifferentMethodButSameArguments() {
        Invocation otherInvocation = new Invocation(new Method("otherTest", Object[].class), new Object[]{MockCreationSettings.defaultSettings().getMock()}, new Location(1, 1, "TestClass.java"));
        assertFalse(matcher.hasSameMethod(otherInvocation));
        assertTrue(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasDifferentMethodButDifferentArguments() {
        Invocation otherInvocation = new Invocation(new Method("otherTest", Object[].class), new Object[]{new Object(){}, new Object(){}}, new Location(1, 1, "TestClass.java"));
        assertFalse(matcher.hasSameMethod(otherInvocation));
        assertFalse(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CapturesArguments capture = capture(equalTo(mock));
        matcher.captureArgumentsFrom(invocation);
        assertEquals(mock, capture.getValue());
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithDifferentNumberOfArguments() {
        Invocation invocationWithDifferentNumberOfArguments = new Invocation(invocation.getMethod(), new Object[]{MockCreationSettings.defaultSettings().getMock(), new Object(){}}, new Location(1, 1, "TestClass.java"));
        matcher.captureArgumentsFrom(invocationWithDifferentNumberOfArguments);
        assertNull(capture.getValue());
    }

    @Test(expected = VerificationException.class)
    public void testCaptureArgumentsFromVerifiedInvocation() {
        Invocation verifiedInvocation = new Invocation(invocation.getMethod(), new Object[]{mock}, new Location(1, 1, "TestClass.java"));
        stubber.verify(mock).method(invocation.getMethod());
        matcher.captureArgumentsFrom(verifiedInvocation);
    }
}