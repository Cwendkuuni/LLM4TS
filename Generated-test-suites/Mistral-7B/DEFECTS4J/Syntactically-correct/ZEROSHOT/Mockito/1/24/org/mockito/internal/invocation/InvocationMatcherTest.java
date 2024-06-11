package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_matchInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(actualInvocation.getMethod()).thenReturn(method);

        // then
        MatcherAssert.assertThat(invocationMatcher, Matchers.is(new InvocationMatcher(actualInvocation)));
        MatcherAssert.assertThat(invocationMatcher, Matchers.hasMethodName("testMethod"));
        MatcherAssert.assertThat(invocationMatcher, Matchers.hasMatchers(matchers.toArray(new Matcher[0])));
        MatcherAssert.assertThat(invocationMatcher, Matchers.hasSameInvocation(actualInvocation));
    }

    @Test
    public void testInvocationMatcher_matchesInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Stubber stubber = Mockito.doReturn(true).stub(actualInvocation, "matchesInvocation");

        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(actualInvocation.getMethod()).thenReturn(method);

        // when
        boolean result = invocationMatcher.matches(actualInvocation);

        // then
        verify(actualInvocation).matchesInvocation();
        MatcherAssert.assertThat(result, Matchers.is(true));
    }

    @Test
    public void testInvocationMatcher_doesNotMatchInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Stubber stubber = Mockito.doReturn(false).stub(actualInvocation, "matchesInvocation");

        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("AnotherTestMethod");
        when(actualInvocation.getMethod()).thenReturn(method);

        // when
        boolean result = invocationMatcher.matches(actualInvocation);

        // then
        verify(actualInvocation).matchesInvocation();
        MatcherAssert.assertThat(result, Matchers.is(false));
    }

    @Test
    public void testInvocationMatcher_invocationsWithSameMethodNameButDifferentParameterTypes() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers2 = Arrays.asList(Matchers.eq(1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");

        // when
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // then
        MatcherAssert.assertThat(result, Matchers.is(false));
    }

    @Test
    public void testInvocationMatcher_invocationsWithSameMethodNameAndSameParameterTypes() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers2 = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");

        // when
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // then
        MatcherAssert.assertThat(result, Matchers.is(true));
    }

    @Test
    public void testInvocationMatcher_invocationsWithDifferentMethods() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(Matchers.eq("Hello"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers2 = Arrays.asList(Matchers.eq("World"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("testMethod1");

        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("testMethod2");

        // when
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // then
        MatcherAssert.assertThat(result, Matchers.is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgThat.argThat(new ArgumentCaptor<String>() {
            @Override
            public boolean matches(Object argument) {
                MatcherAssert.assertThat(argument, Matchers.is("Hello"));
                return true;
            }
        }));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{"Hello"};

        // when
        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                invocation.getArgumentAt(0, String.class);
                return null;
            }
        }).invoke(invocation, arguments));

        // then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(invocation).getArgumentAt(0, String.class);
        verify(invocationMatcher).getMatchers().get(0).matches("Hello");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromInvocationWithVarargs() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgThat.argThat(new ArgumentCaptor<String[]>() {
            @Override
            public boolean matches(Object argument) {
                String[] strings = (String[]) argument;
                MatcherAssert.assertThat(strings, Matchers.arrayContaining(Arrays.asList("Hello", "World")));
                return true;
            }
        }));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{"Hello", "World"};

        // when
        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object[] rawArguments = invocation.getRawArguments();
                String[] strings = (String[]) rawArguments[invocation.getArgIndex()];
                return null;
            }
        }).invoke(invocation, arguments));

        // then
        throw new AssertionError("Expected UnsupportedOperationException but got none.");
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        MatcherAssert.assertThat(invocationMatchers, Matchers.hasSize(2));
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            MatcherAssert.assertThat(invocationMatcher, Matchers.instanceOf(InvocationMatcher.class));
        }
    }
}