package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.capture;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.instanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.args;
import static org.mockito.internal.reporting.PrintSettings.printEmpty;
import static org.mockito.internal.reporting.PrintSettings.printWithFullyQualifiedNames;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher.ArgumentsCaptor;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertNull(matcher.getMatchers());
    }

    @Test
    public void testNewInstanceWithNonEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), isNull());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.method(String.class, "testMethod", new Class[] { String.class, int.class });
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMethod(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testToStringWithEmptyMatcherListAndPrintEmpty() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals("Invocation [method=methodName(Class[],Class[]) methodType=methodType(Class,...,Class), location=Location@0]",
                matcher.toString(printEmpty()));
    }

    @Test
    public void testToStringWithEmptyMatcherListAndPrintWithFullyQualifiedNames() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals("Invocation [method=methodName(java.lang.Class[],java.lang.Class[]) methodType=methodType(java.lang.reflect.Method), location=Location@0]",
                matcher.toString(printWithFullyQualifiedNames()));
    }

    @Test
    public void testToStringWithMatcherListAndPrintEmpty() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), isNull());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals("Invocation [method=methodName(java.lang.Class[],java.lang.Class[]) methodType=methodType(java.lang.reflect.Method), arguments=[argumentsWithValues(java.lang.Integer), null], location=Location@0]",
                matcher.toString(printEmpty()));
    }

    @Test
    public void testToStringWithMatcherListAndPrintWithFullyQualifiedNames() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), isNull());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals("Invocation [method=methodName(java.lang.Class[],java.lang.Class[]) methodType=methodType(java.lang.reflect.Method), arguments=[ArgumentsMatcher$ArgumentsCaptor@1312522622, ArgumentsMatcher$NullMatcher@1525430157], location=Location@0]",
                matcher.toString(printWithFullyQualifiedNames()));
    }

    @Test
    public void testMatches_sameInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), isNull());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatches_differentInvocation() {
        Invocation wantedInvocation = Mockito.mock(Invocation.class);
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), isNull());
        InvocationMatcher wantedMatcher = new InvocationMatcher(wantedInvocation, matchers);

        assertFalse(wantedMatcher.matches(candidateInvocation));
    }

    @Test
    public void testMatches_sameInvocation_but_different_arguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), same(Double.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        candidateInvocation.setArguments(new Object[] { 1, "2.0" });

        assertFalse(matcher.matches(candidateInvocation));
    }

    @Test
    public void testMatches_sameInvocation_but_different_arguments_but_safelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), same(Double.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        candidateInvocation.setArguments(new Object[] { 1, "2.0" });

        assertTrue(matcher.safelyArgumentsMatch(candidateInvocation.getArguments()));
        assertFalse(matcher.matches(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameInvocations() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), same(Double.class));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_differentInvocations() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(same(Integer.class), same(Double.class));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameInvocations_but_different_methods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Method method1 = Mockito.method(String.class, "testMethod1", new Class[] { String.class, int.class });
        method1.setAccessible(true);
        invocation1.setMethod(method1);

        Method method2 = Mockito.method(String.class, "testMethod2", new Class[] { String.class, int.class });
        method2.setAccessible(true);
        invocation2.setMethod(method2);

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameInvocations_but_different_methods_but_same_arguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        Method method1 = Mockito.method(String.class, "testMethod1", new Class[] { String.class, int.class });
        method1.setAccessible(true);
        invocation1.setMethod(method1);

        Method method2 = Mockito.method(String.class, "testMethod2", new Class[] { String.class, int.class });
        method2.setAccessible(true);
        invocation2.setMethod(method2);

        Object[] arguments1 = new Object[] { "foo", 1 };
        Object[] arguments2 = new Object[] { "foo", 1 };
        invocation1.setArguments(arguments1);
        invocation2.setArguments(arguments2);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        Method method = Mockito.method(String.class, "testMethod", new Class[] { String.class, int.class });
        method.setAccessible(true);
        invocation1.setMethod(method);
        invocation2.setMethod(method);

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testGetLocation() {
        Location location = Mockito.mock(Location.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setLocation(location);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals(location, matcher.getLocation());
    }

    @Test
    public void testToStringWithNullMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, null);

        assertEquals("Invocation [method=methodName(Class[],Class[]) methodType=methodType(Method), location=null]",
                matcher.toString(printEmpty()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(capture(ArgumentsCaptor.forClass(Integer.class)), capture(ArgumentsCaptor.forClass(String.class)));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[] { 1, "foo" };
        invocationMock.setArguments(arguments);

        matcher.captureArgumentsFrom(invocationMock);

        ArgumentCaptor<Integer> integerCaptor = matchers.get(0);
        ArgumentCaptor<String> stringCaptor = matchers.get(1);

        assertEquals(1, integerCaptor.getValue());
        assertEquals("foo", stringCaptor.getValue());
    }

    @Test
    public void testCaptureArgumentsFrom_nullInvocation() {
        List<Matcher> matchers = Arrays.asList(capture(ArgumentsCaptor.forClass(Integer.class)), capture(ArgumentsCaptor.forClass(String.class)));
        InvocationMatcher matcher = new InvocationMatcher(null, matchers);

        assertNull(matcher.getInvocation());
    }

    @Test
    public void testCaptureArgumentsFrom_emptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[] { 1, "foo" };
        invocationMock.setArguments(arguments);

        matcher.captureArgumentsFrom(invocationMock);

        assertNull(matcher.getMatchers());
    }

    @Test
    public void testCaptureArgumentsFrom_multipleMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(capture(ArgumentsCaptor.forClass(Integer.class)), capture(ArgumentsCaptor.forClass(String.class)), capture(isNull()));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[] { 1, "foo", null };
        invocationMock.setArguments(arguments);

        matcher.captureArgumentsFrom(invocationMock);

        ArgumentCaptor<Integer> integerCaptor = matchers.get(0);
        ArgumentCaptor<String> stringCaptor = matchers.get(1);
        ArgumentCaptor<Object> nullCaptor = matchers.get(2);

        assertEquals(1, integerCaptor.getValue());
        assertEquals("foo", stringCaptor.getValue());
        assertNull(nullCaptor.getValue());
    }
}