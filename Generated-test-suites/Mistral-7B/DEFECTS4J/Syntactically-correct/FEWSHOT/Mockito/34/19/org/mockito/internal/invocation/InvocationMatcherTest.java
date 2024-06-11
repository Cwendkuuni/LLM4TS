package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.not;
import static org.mockito.internal.matchers.ArgThat.nullValue;
import static org.mockito.internal.matchers.ArgThat.same;
import static org.mockito.internal.matchers.ArgThat.sameInstance;
import static org.mockito.internal.matchers.Arguments.argument;
import static org.mockito.internal.matchers.Arguments.arrayContaining;
import static org.mockito.internal.matchers.Arguments.args;
import static org.mockito.internal.matchers.Arguments.instanceOf;
import static org.mockito.internal.reporting.PrintSettings.DEFAULT_PRINT_SETTINGS;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.InvocationNotVerifiedException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMock;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithNoMatchers() {
        Method targetMethod = SomeClassWithAMethod.class.getMethod("noArgsMethod", new Class[0]);
        Invocation invocation = new InvocationMock(targetMethod, "lineNumber", "fileName", null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        InvocationMatcher noArgumentsMatcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(noArgumentsMatcher));
        assertTrue(noArgumentsMatcher.matches(matcher));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Method targetMethod = SomeClassWithAMethod.class.getMethod("argsMethod", String.class, int.class);

        Object[] arguments = new Object[] {"foo", 1};
        Invocation invocation = new InvocationMock(targetMethod, "lineNumber", "fileName", arguments);

        InvocationMatcher matcherWithArgStringMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(same("foo"))));
        InvocationMatcher matcherWithArgIntMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(same(1))));
        InvocationMatcher matcherWithBothMatchers = new InvocationMatcher(invocation, Arrays.asList(argThat(same("foo")), argThat(same(1))));

        assertTrue(matcherWithArgStringMatcher.matches(matcherWithBothMatchers));
        assertTrue(matcherWithArgIntMatcher.matches(matcherWithBothMatchers));
        assertTrue(matcherWithArgStringMatcher.matches(matcherWithArgIntMatcher));
        assertTrue(matcherWithArgIntMatcher.matches(matcherWithArgStringMatcher));
        assertTrue(matcherWithBothMatchers.matches(matcherWithArgStringMatcher));
        assertTrue(matcherWithBothMatchers.matches(matcherWithArgIntMatcher));

        InvocationMatcher noArgStringMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertFalse(matcherWithArgStringMatcher.matches(noArgStringMatcher));
        assertFalse(noArgStringMatcher.matches(matcherWithArgStringMatcher));
    }

    @Test
    public void testInvocationMatcherWithArgumentCapture() {
        Method targetMethod = SomeClassWithAMethod.class.getMethod("argsMethod", String[].class);
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation invocation = new InvocationMock(targetMethod, "lineNumber", "fileName", arguments);

        InvocationMatcher matcherWithArgCapture = new InvocationMatcher(invocation, Arrays.asList(argThat(new CaptureArgumentMatcher("arg1"))));

        String capturedArgument1 = null;
        matcherWithArgCapture.captureArgumentsFrom(new InvocationMock(targetMethod, "lineNumber", "fileName", new String[] {"baz", "qux"}));
        assertTrue(matcherWithArgCapture.getMatchers().get(0).matches(Arrays.asList(args(capturedArgument1))));
        assertEquals("bar", capturedArgument1);
    }

    @Test(expected = InvocationNotVerifiedException.class)
    public void testInvocationMatcherWithUnverifiedInvocation() {
        Method targetMethod = SomeClassWithAMethod.class.getMethod("argsMethod", String[].class);
        Object[] arguments = new Object[] {"foo", "bar"};
        Invocation invocation = new InvocationMock(targetMethod, "lineNumber", "fileName", arguments);

        InvocationMatcher matcher = new InvocationMatcher(new InvocationMock(targetMethod, "lineNumber", "fileName"));
        matcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSimilarMethod() {
        Method targetMethod1 = SomeClassWithAMethod.class.getMethod("methodWithOneArg", String.class);
        Method targetMethod2 = SomeClassWithAMethod.class.getMethod("methodWithOneArg", Integer.TYPE);

        Invocation invocation1 = new InvocationMock(targetMethod1, "lineNumber1", "fileName1", "foo");
        Invocation invocation2 = new InvocationMock(targetMethod2, "lineNumber2", "fileName2", 1);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.hasSimilarMethod(invocation2));
        assertFalse(invocation2.hasSimilarMethod(matcher));

        Method targetMethod3 = SomeClassWithAMethod.class.getMethod("methodWithOneArg", String.class);
        Invocation invocation3 = new InvocationMock(targetMethod3, "lineNumber3", "fileName3", "foo");
        assertTrue(matcher.hasSimilarMethod(invocation3));
        assertTrue(invocation3.hasSimilarMethod(matcher));
    }

    @Test
    public void testHasSameMethod() {
        Method targetMethod1 = SomeClassWithAMethod.class.getMethod("methodWithOneArg", String.class);
        Method targetMethod2 = SomeClassWithAMethod.class.getMethod("methodWithOneArg", String.class);

        Invocation invocation1 = new InvocationMock(targetMethod1, "lineNumber1", "fileName1", "foo");
        Invocation invocation2 = new InvocationMock(targetMethod2, "lineNumber2", "fileName2", "foo");

        assertTrue(matcher.hasSameMethod(invocation2));
        assertTrue(invocation2.hasSameMethod(matcher));
    }

    private static class SomeClassWithAMethod {
        public void noArgsMethod() {}

        public void argsMethod(String arg1, int arg2) {}
    }

    private static Matcher<Object> argThat(Matcher<?> matcher) {
        return new ArgThat() {
            @Override
            public boolean matches(Object item) {
                return matcher.matches(item);
            }
        };
    }

    private static Matcher<Object> argThat(CaptureArgumentMatcher captureArgumentMatcher) {
        return new ArgThat() {
            @Override
            public boolean matches(Object item) {
                captureArgumentMatcher.captureArgument((Object[]) item);
                return true;
            }
        };
    }

    private static Matcher<Object> args(Object... args) {
        return arrayContaining(Arrays.asList(args));
    }

    private static Matcher<Object> captureArgumentMatcher(final String argumentName) {
        return new ArgThat() {
            private Object capturedArgument;

            @Override
            public boolean matches(Object item) {
                this.capturedArgument = item;
                return true;
            }

            public Object getCapturedArgument() {
                return capturedArgument;
            }
        };
    }
}

class CaptureArgumentMatcher implements Matcher<Object> {
    private String argumentName;

    public CaptureArgumentMatcher(String argumentName) {
        this.argumentName = argumentName;
    }

    @Override
    public boolean matches(Object item) {
        return true;
    }

    public void captureArgument(Object[] invocationArguments) {
        int index = Arrays.asList(invocationArguments).indexOf(argument(argumentName));
        if (index != -1) {
            this.argument = invocationArguments[index];
        }
    }

    private Object argument;

    public Object getCapturedArgument() {
        return argument;
    }
}