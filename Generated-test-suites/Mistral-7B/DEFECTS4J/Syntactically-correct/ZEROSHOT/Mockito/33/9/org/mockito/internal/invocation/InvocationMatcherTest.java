package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;
import org.mockito.verification.VerificationMode;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreation() {
        // Given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testInvocationMatcherCreationWithoutMatchers() {
        // Given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        // Given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testMatches() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Stubber stubber = Mockito.doReturn(true).stub(invocation1.getMock()).method("isSameMock");

        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.matches(invocation2);

        // Then
        verify(invocation1.getMock()).isSameMock(invocation2.getMock());
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        // Given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);

        // When
        boolean result = invocationMatcher1.matches(invocationMatcher1);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithSameInvocationAndMatchers() {
        // Given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers1 = Collections.singletonList(isA(String.class));
        List<Matcher> matchers2 = Collections.singletonList(isInstanceOf(List.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers2);

        // When
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocationsSameMethod() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocationsDifferentMethods() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Given
        Object[] arguments = new Object[0];
        InvocationMatcher invocationMatcher = Mockito.spy(new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.singletonList(isA(String.class))));

        // When
        boolean result = invocationMatcher.safelyArgumentsMatch(arguments);

        // Then
        assertThat(result, is(true));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        // Given
        Object[] arguments = new Object[0];
        InvocationMatcher invocationMatcher = Mockito.spy(new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.singletonList(new Throwable())));

        // When & Then
        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        List<Matcher> matchers1 = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        List<Matcher> matchers2 = Collections.singletonList(isInstanceOf(List.class));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        // When
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethodDifferentMethods() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        List<Matcher> matchers1 = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        List<Matcher> matchers2 = Collections.singletonList(isInstanceOf(List.class));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        // When
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethodWithDifferentInvocations() {
        // Given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        List<Matcher> matchers = Collections.singletonList(isA(String.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(new ArgumentCaptor<String>() {
            @Override
            public void verify(String argument) {
                verify(invocation.getMock()).methodWithStringArgument(argument);
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        Invocation newInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] newArguments = new Object[1];
        newArguments[0] = "test";
        invocationMatcher.captureArgumentsFrom(newInvocation);

        // Then
        verify(invocation.getMock()).methodWithStringArgument("test");
    }

    @Test
    public void testCreateFrom() {
        // Given
        List<Invocation> invocations = Collections.singletonList(Mockito.mock(InvocationOnMock.class));

        // When
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Then
        assertThat(invocationMatchers.size(), is(1));
    }
}