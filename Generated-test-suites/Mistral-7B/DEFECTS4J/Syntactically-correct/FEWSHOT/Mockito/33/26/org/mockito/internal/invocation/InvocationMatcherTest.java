package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.stub.MockitoStubInvocation;
import org.mockito.internal.util.reflection.Whitebox;

public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithMatchers() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        List<Matcher> matchers = Collections.singletonList(argThat(is(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new MockitoStubInvocation(method, new Object[]{1}), matchers);
        assertTrue(invocationMatcher.matches(new MockitoStubInvocation(method, new Object[]{1})));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new MockitoStubInvocation(method, new Object[]{1}));
        assertTrue(invocationMatcher.matches(new MockitoStubInvocation(method, new Object[]{1})));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        Method method2 = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new MockitoStubInvocation(method1, new Object[]{1}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new MockitoStubInvocation(method2, new Object[]{1}));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testDifferentMethods() {
        Method method1 = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod1", Integer.TYPE, int.class);
        Method method2 = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod2", String.class, int.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new MockitoStubInvocation(method1, new Object[]{1}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new MockitoStubInvocation(method2, new Object[]{"1"}));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testDifferentMocks() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        Object mock1 = new Object();
        Object mock2 = new Object();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new MockitoStubInvocation(method, new Object[]{1}, mock1), Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new MockitoStubInvocation(method, new Object[]{1}, mock2), Collections.emptyList());
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithArguments() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Collections.singletonList(argThat(is(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new MockitoStubInvocation(method, arguments), matchers);
        assertTrue(invocationMatcher.matches(new MockitoStubInvocation(method, arguments)));
    }

    @Test
    public void testMismatchedArguments() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Collections.singletonList(argThat(is(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new MockitoStubInvocation(method, arguments), matchers);
        assertFalse(invocationMatcher.matches(new MockitoStubInvocation(method, arguments)));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Collections.singletonList(argThat(new CapturesArguments() {
            int capturedArgument;

            @Override
            public void captureFrom(Object argument) {
                this.capturedArgument = ((Integer) argument).intValue();
            }

            @Override
            public String description() {
                return "an integer equal to [1]";
            }
        }));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new MockitoStubInvocation(method, arguments), matchers);
        assertTrue(invocationMatcher.matches(new MockitoStubInvocation(method, arguments)));
        assertEquals(1, invocationMatcher.getMatchers().get(0).match(arguments[0]));
    }

    @Test
    public void testCreateFrom() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new MockitoStubInvocation(method, new Object[]{1}));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher invocationMatcher = invocationMatchers.get(0);
        assertTrue(invocationMatcher.matches(new MockitoStubInvocation(method, new Object[]{1})));
    }

    @Test
    public void testCreateFromWithMatchers() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new MockitoStubInvocation(method, new Object[]{1}));
        List<Matcher> matchers = Collections.singletonList(argThat(is(1)));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations, matchers);
        InvocationMatcher invocationMatcher = invocationMatchers.get(0);
        assertTrue(invocationMatcher.matches(new MockitoStubInvocation(method, new Object[]{1})));
    }

    @Test
    public void testToString() {
        Method method = Whitebox.getMethod(MockitoStubInvocation.class, "someMethod", Integer.TYPE, int.class);
        Invocation invocation = new MockitoStubInvocation(method, new Object[]{1});
        List<Matcher> matchers = Collections.singletonList(argThat(is(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = String.format("InvocationMatcher{%s, %s}", invocation.toString(), matchers.toString());
        assertEquals(expected, invocationMatcher.toString());
    }

    private static Matcher<Object> is(Object expected) {
        return argThat(new Matcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return equalTo(expected).matches(argument);
            }

            @Override
            public String description() {
                return String.format("an %s", expected.getClass().getSimpleName());
            }
        });
    }
}