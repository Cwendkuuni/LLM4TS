package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgThat.argThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintableInvocation;
import org.mockito.internal.reporting.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    @Test
    public void test_Constructor_With_Invocation_And_EmptyList_Of_Matchers() {
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), new Method(Object.class, new Class[]{}), new Object[0], new Location(1, 1, "testMethodName"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void test_Constructor_With_Invocation_And_List_Of_Matchers() {
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), new Method(Object.class, new Class[]{Integer.TYPE}), new Object[]{1}, new Location(1, 1, "testMethodName"), null);
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(Integer.class)), argThat(Matchers.equalTo(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void test_getMethod() {
        Method method = new Method(Object.class, new Class[]{}, null);
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[0], new Location(1, 1, "testMethodName"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void test_getInvocation() {
        Method method = new Method(Object.class, new Class[]{}, null);
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[0], new Location(1, 1, "testMethodName"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void test_getMatchers() {
        Method method = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[]{1}, new Location(1, 1, "testMethodName"), null);
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(Integer.class)), argThat(Matchers.equalTo(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void test_toString() {
        Method method = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[]{1}, new Location(1, 1, "testMethodName"), null);
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(Integer.class)), argThat(Matchers.equalTo(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("Invoked %s%n%s", invocation.toString(printSettings), invocationMatcher.toString(printSettings));

        assertEquals(expected, invocationMatcher.toString());
        assertEquals(expected, invocationMatcher.toString(new PrintSettings()));
    }

    @Test
    public void test_matches() {
        Method method = new Method(Object.class, new Class[]{}, null);
        Invocation invocation1 = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[0], new Location(1, 1, "testMethodName1"), null);
        Invocation invocation2 = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[0], new Location(1, 1, "testMethodName2"), null);
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(Object.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void test_safelyArgumentsMatch() {
        Method method = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Object[] arguments = new Object[]{1};
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, arguments, new Location(1, 1, "testMethodName"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void test_hasSimilarMethod() {
        Method method1 = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Method method2 = new Method(Object.class, new Class[]{Double.TYPE}, null);
        Invocation invocation1 = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method1, new Object[]{1}, new Location(1, 1, "testMethodName1"), null);
        Invocation invocation2 = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method2, new Object[]{1.0}, new Location(1, 1, "testMethodName2"), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void test_hasSameMethod() {
        Method method1 = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Method method2 = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Invocation invocation1 = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method1, new Object[]{1}, new Location(1, 1, "testMethodName1"), null);
        Invocation invocation2 = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method2, new Object[]{1}, new Location(1, 1, "testMethodName2"), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void test_getLocation() {
        Method method = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[]{1}, new Location(1, 1, "testMethodName"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test
    public void test_toString_With_PrintSettings() {
        Method method = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, new Object[]{1}, new Location(1, 1, "testMethodName"), null);
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(Integer.class)), argThat(Matchers.equalTo(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("Invoked %s%n%s", invocation.toString(printSettings), invocationMatcher.toString(printSettings));

        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void test_captureArgumentsFrom() {
        Method method = new Method(Object.class, new Class[]{Integer.TYPE}, null);
        Object[] arguments = new Object[]{1};
        Invocation invocation = new InvocationOnMock(new Stubber(), MockitoTestUtils.createMock(Object.class), method, arguments, new Location(1, 1, "testMethodName"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object capture = new Object();
        Matcher mockMatcher = argThat(arg -> arg.equals(capture));
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom(anyObject())).thenAnswer(invocation -> invocation.getArguments()[0]);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom(arguments[0]);
    }
}