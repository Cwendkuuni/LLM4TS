package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private static final String MOCKED_METHOD_NAME = "mockedMethod";
    private static final String ANOTHER_MOCKED_METHOD_NAME = "anotherMockedMethod";
    private static final String DIFFERENT_METHOD_NAME = "differentMethodName";
    private static final Object[] ARGS = new Object[]{1, "hello", 3.14};
    private static final Object[] ARGS_VARARG = new Object[]{"one", "two", "three"};
    private static final Object[] ARGS_VARARG_MISMATCH = new Object[]{"one", "one", "three"};

    private InvocationMatcher matcher;
    private Invocation mockedInvocation;
    private Invocation anotherMockedInvocation;
    private Invocation differentInvocation;

    @Before
    public void setUp() {
        Mockito.initMocks(this);

        mockedInvocation = Mockito.mock(Invocation.class);
        when(mockedInvocation.getMethod()).thenReturn(new Method("mockedMethod", new Class[0]));

        anotherMockedInvocation = Mockito.mock(Invocation.class);
        when(anotherMockedInvocation.getMethod()).thenReturn(new Method(ANOTHER_MOCKED_METHOD_NAME, new Class[0]));

        differentInvocation = Mockito.mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(new Method(DIFFERENT_METHOD_NAME, new Class[0]));

        matcher = new InvocationMatcher(mockedInvocation);
    }

    @Test
    public void shouldReturnTrueWhenInvocationMatches() {
        // given
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method(MOCKED_METHOD_NAME, new Class[0]));

        // when
        boolean result = matcher.matches(actualInvocation);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWhenInvocationDoesntMatch() {
        // given
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method("differentMethod", new Class[0]));

        // when
        boolean result = matcher.matches(actualInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnFalseWhenUnverifiedInvocationDoesntMatch() {
        // given
        Invocation actualInvocation = anotherMockedInvocation;

        // when
        boolean result = matcher.matches(actualInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnFalseWhenInvocationsHaveDifferentMethods() {
        // given
        Invocation actualInvocation = differentInvocation;

        // when
        boolean result = matcher.matches(actualInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnFalseWhenInvocationsHaveDifferentMocks() {
        // given
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method("mockedMethod", new Class[0]));

        // when
        boolean result = matcher.matches(anotherMockedInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnFalseWhenInvocationHasVarArgsButDifferentSize() {
        // given
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method(MOCKED_METHOD_NAME, new Class[]{Integer.TYPE, String.class, Double.TYPE}));

        // when
        boolean result = matcher.matches(actualInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasVarArgsWithSameSize() {
        // given
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method(MOCKED_METHOD_NAME, new Class[]{Integer.TYPE, String.class, Double.TYPE}));

        // when
        boolean result = matcher.matches(mockedInvocation);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWhenInvocationHasVarArgsButDifferentArguments() {
        // given
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method(MOCKED_METHOD_NAME, new Class[]{Integer.TYPE, String.class, Double.TYPE}));

        // when
        boolean result = matcher.matches(mockedInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldCaptureArgumentsFromInvocation() {
        // given
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method("testMethod", new Class[]{Integer.TYPE, String.class}));
        when(invocation.getArguments()).thenReturn(ARGS_VARARG);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Matcher capturedArgMatcher = ArgumentMatchers.anyInt();

        // when
        matcher.captureArgumentsFrom(invocation);

        // then
        Object capturedArg = capturedArgMatcher.getValue();
        assertThat(capturedArg, is(equalTo(1)));

        capturedArgMatcher = ArgumentMatchers.argThat(new StringMatcher("hello"));
        matcher.captureArgumentsFrom(invocation);

        capturedArg = capturedArgMatcher.getValue();
        assertThat(capturedArg, is(equalTo("hello")));
    }

    @Test(expected = Throwable.class)
    public void shouldThrowExceptionWhenArgumentsDontMatch() {
        // given
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method("testMethod", new Class[]{Integer.TYPE, String.class}));
        when(invocation.getArguments()).thenReturn(ARGS_VARARG_MISMATCH);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Matcher capturedArgMatcher = ArgumentMatchers.anyInt();

        // when
        matcher.captureArgumentsFrom(invocation);

        // then
        // exception is thrown here
    }
}