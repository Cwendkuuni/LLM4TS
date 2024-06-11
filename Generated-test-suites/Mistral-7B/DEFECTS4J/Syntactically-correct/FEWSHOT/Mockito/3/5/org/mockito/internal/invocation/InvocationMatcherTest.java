package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.hasItem;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.invocation.InvocationTester.invocation;
import static org.mockito.stubbing.AnswerCalled.times;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.verification.VerificationMode;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testEmptyMatcher() {
        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, new Object[0]), Collections.<Matcher>emptyList());

        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation(mock, new Object[0]));

        assertTrue(matcher.matches(emptyMatcher));
    }

    @Test
    public void testToString() {
        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, "foo", 1), Arrays.asList(argThat(isNull()), argThat(isInstanceOf(Integer.class))));

        assertEquals("Invocation on [mock]\n" +
                     "    method: foo(java.lang.String, int)\n" +
                     "    arguments:\n" +
                     "        [<null>]\n" +
                     "        [java.lang.Integer]\n", matcher.toString());
    }

    @Test
    public void testMatchesWithSameMethod() {
        Method method = Mockito.methodThat(m -> m.getName().equals("foo"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock, method, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock, method, new Object[0]), Collections.<Matcher>emptyList());

        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        Method method1 = Mockito.methodThat(m -> m.getName().equals("foo"));
        Method method2 = Mockito.methodThat(m -> m.getName().equals("bar"));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock, method1, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock, method2, new Object[0]), Collections.<Matcher>emptyList());

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testMatchesWithDifferentMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock1, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock2, new Object[0]), Collections.<Matcher>emptyList());

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testMatchesWithNullInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(null, Collections.<Matcher>emptyList());

        assertFalse(matcher.matches(new InvocationMatcher(invocation(mock, new Object[0]), Collections.<Matcher>emptyList())));
    }

    @Test
    public void testMatchesWithNullMatcher() {
        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, new Object[0]), null);

        assertFalse(matcher.matches(new InvocationMatcher(invocation(mock, new Object[0]), Collections.<Matcher>emptyList())));
    }

    @Test
    public void testMatchesWithArguments() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();

        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, "foo", arg1, arg2, arg3),
                Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object.class)), argThat(isInstanceOf(Object.class))));

        InvocationMatcher candidate1 = new InvocationMatcher(invocation(mock, "foo", arg1, arg2), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object.class))));
        InvocationMatcher candidate2 = new InvocationMatcher(invocation(mock, "foo", arg1, new Object()), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object.class))));
        InvocationMatcher candidate3 = new InvocationMatcher(invocation(mock, "foo", arg1, arg2, new Object()), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object.class)), argThat(isInstanceOf(Object.class))));

        assertTrue(matcher.matches(candidate1));
        assertTrue(matcher.matches(candidate2));
        assertTrue(matcher.matches(candidate3));

        assertFalse(matcher.matches(new InvocationMatcher(invocation(mock, "bar", arg1, arg2, arg3), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object.class)), argThat(isInstanceOf(Object.class))))));
    }

    @Test
    public void testMatchesWithVarargs() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object[] args = new Object[] { arg1, arg2 };

        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, "foo", args), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object[].class))));

        InvocationMatcher candidate1 = new InvocationMatcher(invocation(mock, "foo", arg1, arg2), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object.class))));
        InvocationMatcher candidate2 = new InvocationMatcher(invocation(mock, "foo", new Object[] { arg1, arg2 }), Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isInstanceOf(Object[].class))));

        assertTrue(matcher.matches(candidate1));
        assertTrue(matcher.matches(candidate2));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Mockito.methodThat(m -> m.getName().equals("foo"));
        Method method2 = Mockito.methodThat(m -> m.getName().equals("foo"));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock, method1, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock, method2, new Object[0]), Collections.<Matcher>emptyList());

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasDifferentMethods() {
        Method method1 = Mockito.methodThat(m -> m.getName().equals("foo"));
        Method method2 = Mockito.methodThat(m -> m.getName().equals("bar"));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock, method1, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock, method2, new Object[0]), Collections.<Matcher>emptyList());

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethodWithVarargs() {
        Method method1 = Mockito.methodThat(m -> m.getName().equals("foo"));
        Method method2 = Mockito.methodThat(m -> m.getName().equals("foo") && m.isVarArgs());

        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock, method1, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock, method2, new Object[0]), Collections.<Matcher>emptyList());

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasDifferentMethodsWithVarargs() {
        Method method1 = Mockito.methodThat(m -> m.getName().equals("foo"));
        Method method2 = Mockito.methodThat(m -> m.getName().equals("bar"));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation(mock, method1, new Object[0]), Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation(mock, method2, new Object[0]), Collections.<Matcher>emptyList());

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testCaptureArguments() {
        Object arg = new Object();
        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, "foo", arg), Arrays.asList(argThat(argThat(isInstanceOf(Object.class)))));

        Invocation invocation = Mockito.mock(Invocation.class);
        matcher.captureArgumentsFrom(invocation);

        assertEquals(arg, invocation.getArgumentAt(0, Object.class));
    }

    @Test
    public void testCaptureArgumentsWithVarargs() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object[] args = new Object[] { arg1, arg2 };

        InvocationMatcher matcher = new InvocationMatcher(invocation(mock, "foo", args), Arrays.asList(argThat(isInstanceOf(Object[].class))));

        Invocation invocation = Mockito.mock(Invocation.class);
        matcher.captureArgumentsFrom(invocation);

        assertEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation(mock, "foo", new Object[] { "arg1", new Object() }), invocation(mock, "bar"));

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0).matches(matchers.get(0)));
        assertFalse(matchers.get(0).matches(matchers.get(1)));
        assertFalse(matchers.get(1).matches(matchers.get(0)));
        assertFalse(matchers.get(1).matches(matchers.get(1)));
    }
}