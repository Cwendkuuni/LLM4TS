package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.TypeMatcher.argumentThat;
import static org.mockito.internal.matchers.TypeMatcher.isAssignableFrom;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockingDetail;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import org.mockito.stub.Answer3;
import org.mockito.stub.Answer4;
import org.mockito.stub.Answer5;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    private static final String CLASS_UNDER_TEST = "org.mockito.internal.invocation.InvocationMatcher";

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Stubber stubber;
    private MockingDetail mockingDetail;

    @Before
    public void setUp() {
        stubber = Whitebox.createMock(Stubber.class);
        mockingDetail = Whitebox.createMock(MockingDetail.class);

        invocation = new InvocationOnMock(stubber, mockingDetail, new Location(1, 1), new Method(Object.class, String.class));
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testNewInstance() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocationMatcher.getInvocation(), invocation);
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testNewInstanceWithMatchers() {
        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(isAssignableFrom(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocationMatcher.getInvocation(), invocation);
        assertEquals(invocationMatcher.getMatchers(), matchers);
    }

    @Test
    public void testGetMethod() {
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        assertEquals(invocation.toString(), invocationMatcher.toString(new PrintSettings()));
    }

    @Test
    public void testMatches() {
        Invocation invocationToCompare = new InvocationOnMock(stubber, mockingDetail, new Location(1, 1), new Method(Integer.class, int.class));

        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(invocationToCompare));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] argumentsToCompare = new Object[]{1, 2, 3};
        assertTrue(invocationMatcher.safelyArgumentsMatch(argumentsToCompare));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocationToCompare = new InvocationOnMock(stubber, mockingDetail, new Location(1, 1), new Method(Object.class, String.class));

        assertTrue(invocationMatcher.hasSimilarMethod(invocationToCompare));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocationToCompare = new InvocationOnMock(stubber, mockingDetail, new Location(1, 1), new Method(Object.class, String.class));

        assertTrue(invocationMatcher.hasSameMethod(invocationToCompare));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocationToCompare = new InvocationOnMock(stubber, mockingDetail, new Location(1, 1), new Method(Object.class, int.class));

        assertFalse(invocationMatcher.hasSameMethod(invocationToCompare));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(isAssignableFrom(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[]{1, "two"};

        invocationMatcher.captureArgumentsFrom(invocation.getArguments());

        assertTrue(matchers.get(0).matches(invocation.getArguments()[0]));
        assertTrue(matchers.get(1).matches(invocation.getArguments()[1]));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintObjectValues(true);

        assertEquals(invocation.toString(matchers, printSettings), invocationMatcher.toString(printSettings));
    }
}