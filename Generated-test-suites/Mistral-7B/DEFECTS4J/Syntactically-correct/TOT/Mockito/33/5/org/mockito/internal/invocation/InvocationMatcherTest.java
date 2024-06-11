package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        // given
        Object mock = mock(Object.class);
        Method method = getClass().getDeclaredMethod("testMethod", String.class, int.class);
        Invocation invocation = invocation(mock, method, "test string", 10);

        List<Matcher> matchers = Arrays.asList(
                is("test string"),
                is(10)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        InvocationMatcher expected = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher, is(sameInstance(expected)));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.getMethod(), is(sameInstance(method)));
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
    }

    @Test
    public void testMatches() {
        // given
        Object mock = mock(Object.class);
        Method method = getClass().getDeclaredMethod("testMethod", String.class, int.class);
        Object[] arguments = new Object[]{"test string", 10};
        Invocation invocation = invocation(mock, method, arguments);

        List<Matcher> matchers = Arrays.asList(
                is("test string"),
                is(10)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        InvocationMatcher actual = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.matches(actual), is(true));
    }

    @Test
    public void testDoesNotMatch() {
        // given
        Object mock = mock(Object.class);
        Method method = getClass().getDeclaredMethod("testMethod", String.class, int.class);
        Object[] arguments = new Object[]{"wrong string", 20};
        Invocation invocation = invocation(mock, method, arguments);

        List<Matcher> matchers = Arrays.asList(
                is("test string"),
                is(10)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        InvocationMatcher actual = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.matches(actual), is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Object mock = mock(Object.class);
        Method method1 = getClass().getDeclaredMethod("testMethod1", String.class, int.class);
        Method method2 = getClass().getDeclaredMethod("testMethod2", String.class, int.class);

        Invocation invocation1 = invocation(mock, method1, "test string", 10);
        Invocation invocation2 = invocation(mock, method2, "test string", 10);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when

        // then
        assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(true));
    }

    @Test
    public void testHasSimilarMethodButDifferentMethods() {
        // given
        Object mock = mock(Object.class);
        Method method1 = getClass().getDeclaredMethod("testMethod1", String.class, int.class);
        Method method2 = getClass().getDeclaredMethod("testMethod2", int.class, String.class);

        Invocation invocation1 = invocation(mock, method1, "test string", 10);
        Invocation invocation2 = invocation(mock, method2, 10, "test string");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when

        // then
        assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Object mock = mock(Object.class);
        Method method = getClass().getDeclaredMethod("testMethod", String.class, int.class);
        Invocation invocation = invocation(mock, method, "test string", 10);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        String capturedString = stringArgumentCaptor.getValue();
        Integer capturedInteger = integerArgumentCaptor.getValue();

        assertThat(capturedString, is("test string"));
        assertThat(capturedInteger, is(10));
    }

    @Test(expected = AssertionError.class)
    public void testInvalidArguments() {
        // given
        Object mock = mock(Object.class);
        Method method = getClass().getDeclaredMethod("testMethod", String.class, int.class);
        Object[] arguments = new Object[]{null, 10};
        Invocation invocation = invocation(mock, method, arguments);

        List<Matcher> matchers = Arrays.asList(
                is("test string"),
                is(10)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when

        // then
        // should throw an AssertionError as the arguments do not match
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = Arrays.asList(
                invocation(mock(Object.class), getClass().getDeclaredMethod("testMethod1", String.class, int.class), "test string1", 10),
                invocation(mock(Object.class), getClass().getDeclaredMethod("testMethod1", String.class, int.class), "test string2", 20),
                invocation(mock(Object.class), getClass().getDeclaredMethod("testMethod2", int.class, String.class), 30, "test string")
        );

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        InvocationMatcher invocationMatcher3 = invocationMatchers.get(2);

        assertThat(invocationMatcher1.getMethod().getName(), is("testMethod1"));
        assertThat(invocationMatcher1.getMatchers().size(), is(2));

        InvocationMatcher expectedMatcher1 = new InvocationMatcher(invocationMatcher1.getInvocation());
        InvocationMatcher expectedMatcher2 = new InvocationMatcher(invocationMatcher2.getInvocation());
        InvocationMatcher expectedMatcher3 = new InvocationMatcher(invocationMatcher3.getInvocation());

        assertThat(invocationMatcher1, is(sameInstance(expectedMatcher1)));
        assertThat(invocationMatcher2, is(sameInstance(expectedMatcher2)));
        assertThat(invocationMatcher3, is(sameInstance(expectedMatcher3)));
    }
}