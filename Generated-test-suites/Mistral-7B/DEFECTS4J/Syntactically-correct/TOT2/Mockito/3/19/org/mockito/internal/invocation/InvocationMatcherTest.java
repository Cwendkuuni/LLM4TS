package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(Collections.emptyList(), matcher.getMatchers());
    }

    @Test
    public void testMatchesWithSameInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expectedValue1"), eq("expectedValue2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate, matchers);

        assertTrue(matcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesWithDifferentInvocationsAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expectedValue1"), eq("expectedValue2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(new Method("differentMethod", new Class[0]));

        assertFalse(matcher.matches(new InvocationMatcher(candidate, matchers)));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentReturnTypes() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(int[].class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidateMethod.getReturnType()).thenReturn(double[].class);

        assertFalse(matcher.hasSameMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);

        assertTrue(matcher.hasSameMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("differentMethod");
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(matcher.hasSameMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expectedValue1"), eq("expectedValue2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher:\n" +
                "\tinvocation = Invocation:\n" +
                "\t\tlocation = Location:\n" +
                "\t\t\tline = 0\n" +
                "\t\t\tmethod = Method:\n" +
                "\t\t\t\tname = getMethodName\n" +
                "\t\t\t\treturnType = void\n" +
                "\t\t\t\tparameters = []\n" +
                "\tmatchers = [\n" +
                "\t\tMatcherDecorator: ArgumentMatcher: expected: \"expectedValue1\"\n" +
                "\t\tMatcherDecorator: ArgumentMatcher: expected: \"expectedValue2\"\n" +
                "]";

        String actual = matcher.toString();

        assertEquals(expected, actual);
    }
}

//