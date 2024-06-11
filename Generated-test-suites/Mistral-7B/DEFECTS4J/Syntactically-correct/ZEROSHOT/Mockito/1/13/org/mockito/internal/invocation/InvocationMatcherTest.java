package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MockitoMethodInvocation;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_WithDefaultMatchers_MatchesInvocation() {
        // given
        Object mock = mock(String.class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String.class, "length"), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Invocation actualInvocation = new MockitoMethodInvocation(mock, new Method(String.class, "length"), new Object[0]);

        // then
        boolean result = invocationMatcher.matches(actualInvocation);
        assertThat(result, is(true));
    }

    @Test
    public void testInvocationMatcher_WithCustomMatchers_MatchesInvocation() {
        // given
        Object mock = mock(String.class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String.class, "length"), new Object[0]);
        List<Matcher> customMatchers = Arrays.asList(is("abc"), isA(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, customMatchers);

        // when
        Invocation actualInvocation = new MockitoMethodInvocation(mock, new Method(String.class, "length"), new Object[0]);

        // then
        boolean result = invocationMatcher.matches(actualInvocation);
        // Assuming String.length() returns "abc" and an integer
        assertThat(result, is(true));
    }

    @Test
    public void testInvocationMatcher_WithCustomMatchers_DoesNotMatchDifferentInvocation() {
        // given
        Object mock = mock(String.class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String.class, "length"), new Object[0]);
        List<Matcher> customMatchers = Arrays.asList(is("abc"), isA(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, customMatchers);

        Object anotherMock = mock(String.class);
        Invocation anotherInvocation = new MockitoMethodInvocation(anotherMock, new Method(String.class, "length"), new Object[0]);

        // when
        boolean result = invocationMatcher.matches(anotherInvocation);

        // then
        boolean expectedResult = false;
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testInvocationMatcher_WithCustomMatchers_DoesNotMatchSameInvocationButDifferentArguments() {
        // given
        Object mock = mock(String.class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String.class, "substring", String.class, int.class, int.class), new Object[]{null, 0, 1});
        List<Matcher> customMatchers = Arrays.asList(is("abc"), isA(Integer.class), isA(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, customMatchers);

        // when
        Invocation actualInvocation = new MockitoMethodInvocation(mock, new Method(String.class, "substring", String.class, int.class, int.class), new Object[]{null, 1, 2});

        // then
        boolean result = invocationMatcher.matches(actualInvocation);
        boolean expectedResult = false;
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testInvocationMatcher_WithCustomMatchers_CapturesArguments() {
        // given
        Object mock = mock(String.class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String.class, "substring", String.class, int.class, int.class), new Object[]{null, 0, 1});
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        List<Matcher> customMatchers = Arrays.asList(is("abc"), isA(String.class), isA(Integer.class), isA(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, customMatchers);

        // when
        Invocation actualInvocation = new MockitoMethodInvocation(mock, new Method(String.class, "substring", String.class, int.class, int.class), new Object[]{null, 1, 2});
        invocationMatcher.captureArgumentsFrom(actualInvocation);

        // then
        String capturedString = stringArgumentCaptor.getValue();
        assertThat(capturedString, is("abc"));
    }

    @Test
    public void testInvocationMatcher_WithVarargs_CapturesArguments() {
        // given
        Object mock = mock(String[].class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String[].class, "addAll", String[].class), new Object[]{null});
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = ArgumentCaptor.forClass(String[].class);
        List<Matcher> customMatchers = Arrays.asList(isA(String[].class), isA(String[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, customMatchers);

        // when
        Invocation actualInvocation = new MockitoMethodInvocation(mock, new Method(String[].class, "addAll", String[].class), new Object[]{new String[]{"a", "b"}});
        invocationMatcher.captureArgumentsFrom(actualInvocation);

        // then
        String[] capturedStrings = stringArrayArgumentCaptor.getValue();
        assertThat(capturedStrings, is(Arrays.asList("a", "b").toArray(new String[0])));
    }

    @Test
    public void testInvocationMatcher_WithVarargs_DoesNotCaptureArgumentsWhenNoMatchers() {
        // given
        Object mock = mock(String[].class);
        Invocation invocation = new MockitoMethodInvocation(mock, new Method(String[].class, "addAll", String[].class), new Object[]{null});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Invocation actualInvocation = new MockitoMethodInvocation(mock, new Method(String[].class, "addAll", String[].class), new Object[]{new String[]{"a", "b"}});
        invocationMatcher.captureArgumentsFrom(actualInvocation);

        // then
        Object[] capturedArguments = invocationMatcher.getInvocation().getArguments();
        assertThat(capturedArguments, is(nullValue()));
    }
}