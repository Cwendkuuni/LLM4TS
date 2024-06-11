package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoCore;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testMatcherForSingleArgumentMethod() {
        Object mock = mock(String.class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new MethodTestDouble(), new Object[]{"test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Arrays.asList(ArgumentMatchers.eq("test"));
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, expectedMatchers);

        assertThat(invocationMatcher, is(actualMatcher));
        assertThat(invocationMatcher.getMatchers(), is(expectedMatchers));
        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @Test
    public void testMatcherForMultipleArgumentMethod() {
        Object mock = mock(Double.class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new MethodTestDoubleWithTwoArguments(), new Object[]{1.0, 2.0});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Arrays.asList(ArgumentMatchers.eq(1.0), ArgumentMatchers.eq(2.0));
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, expectedMatchers);

        assertThat(invocationMatcher, is(actualMatcher));
        assertThat(invocationMatcher.getMatchers(), is(expectedMatchers));
        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @Test
    public void testMatcherForVarargsMethod() {
        Object mock = mock(Number[].class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new MethodTestDoubleWithVarargs(), new Object[]{new Double[]{1.0, 2.0}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Arrays.asList(ArgumentMatchers.arrayContaining(ArgumentMatchers.arrayContaining(ArgumentMatchers.instanceOf(Double.class))));
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, expectedMatchers);

        assertThat(invocationMatcher, is(actualMatcher));
        assertThat(invocationMatcher.getMatchers(), is(expectedMatchers));
        verify(mock).someMethod(ArgumentMatchers.arrayContaining(ArgumentMatchers.instanceOf(Double.class)));
    }

    @Test
    public void testMatcherForVarargsMethodWithCustomMatcher() {
        Object mock = mock(Number[].class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new MethodTestDoubleWithVarargs(), new Object[]{new Double[]{1.0, 2.0}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new MyCustomMatcher()));

        verify(mock).someMethod(new Double[]{1.0, 2.0});
    }

    @Test
    public void testMatcherForMethodWithNoArguments() {
        Object mock = mock(String.class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new MethodTestNoArguments(), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Collections.emptyList();
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, expectedMatchers);

        assertThat(invocationMatcher, is(actualMatcher));
        assertThat(invocationMatcher.getMatchers(), is(expectedMatchers));
        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMatcherForMethodWithVarargsAndCustomMatcher() {
        Object mock = mock(Number[].class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new MethodTestDoubleWithVarargs(), new Object[]{new Double[]{1.0, 2.0}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new MyCustomMatcher()));

        // This test should fail with UnsupportedOperationException since the test class does not support capturing varargs
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Stubber stubber = given(mock).stub();

        Invocation invocation = new InvocationOnMock(mock, new SomeMethodWithArguments(), new Object[]{"test", 1, 2.0});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedToString = "InvocationMatcher [invocation=Invocation [method=SomeMethodWithArguments(java.lang.String, java.lang.String, int, double), arguments=[test, 1, 2.0]], matchers=[ArgumentsMatcher [matcher=ArgumentsMatcher [matcher=ArgThat [matcher=Matcher [matcher=org.hamcrest.CoreMatchers.is(java.lang.String.class)], description=is (String)], description=is], ArgumentsMatcher [matcher=ArgumentsMatcher [matcher=ArgumentsMatcher [matcher=Matcher [matcher=org.hamcrest.CoreMatchers.is(int.class)], description=is], description=is], ArgumentsMatcher [matcher=ArgumentsMatcher [matcher=Matcher [matcher=org.hamcrest.CoreMatchers.is(double.class)], description=is]]]";
        String actualToString = invocationMatcher.toString();

        assertThat(invocationMatcher.toString(), is(expectedToString));
    }

    private static class MethodTestDouble {
        public void someMethod(String arg) {}
    }

    private static class MethodTestDoubleWithTwoArguments {
        public void someMethod(double arg1, double arg2) {}
    }

    private static class MethodTestDoubleWithVarargs {
        public void someMethod(Double... varargs) {}
    }

    private static class MethodTestNoArguments {
        public void someMethod() {}
    }

    private static class SomeMethodWithArguments {
        public void someMethod(String arg1, int arg2, double arg3) {}
    }

    private static class MyCustomMatcher implements Matcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("custom matcher");
        }
    }
}