package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withEmptyMatchers_shouldReturnMatchedInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation matchedInvocation = invocationMatcher.getInvocation();
        assertThat(matchedInvocation, is(invocation));
    }

    @Test
    public void testInvocationMatcher_withCustomMatchers_shouldReturnMatchedInvocation() {
        Object[] arguments = new Object[]{1, "two", 3.14};
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                ArgumentMatchers.argThat(arg -> arg.equals("two")),
                ArgumentMatchers.argThat(arg -> arg instanceof Number)
        ));
        Invocation matchedInvocation = invocationMatcher.getInvocation();
        assertThat(matchedInvocation, is(invocation));
    }

    @Test
    public void testInvocationMatcher_withCustomMatchers_shouldReturnFalseForDifferentInvocations() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getArguments()).thenReturn(new Object[]{1, "two", 3.14});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, "three", 3.14});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testInvocationMatcher_withSameMethod_shouldReturnTrue() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(Mockito.spy(new Method(() -> {})));
        when(invocation2.getMethod()).thenReturn(Mockito.spy(new Method(() -> {})));
        when(invocation1.getMethod().getName()).thenReturn("someMethod");
        when(invocation2.getMethod().getName()).thenReturn("someMethod");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testInvocationMatcher_withDifferentMethods_shouldReturnFalse() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(new Method(() -> {}, String.class, int.class));
        when(invocation2.getMethod()).thenReturn(new Method(() -> {}, int.class, String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testInvocationMatcher_withSameInvocation_shouldReturnTrue() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_withDifferentInvocations_shouldReturnFalse() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_withSameInvocationAndCustomMatchers_shouldReturnTrue() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                ArgumentMatchers.argThat(arg -> arg.equals(1))
        );
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_withDifferentInvocationsAndCustomMatchers_shouldReturnFalse() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers1 = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                ArgumentMatchers.argThat(arg -> arg.equals(1))
        );
        List<Matcher> matchers2 = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                ArgumentMatchers.argThat(arg -> arg.equals(2))
        );
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_withSameInvocationAndDifferentMatchers_shouldReturnFalse() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers1 = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                ArgumentMatchers.argThat(arg -> arg.equals(1))
        );
        List<Matcher> matchers2 = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                ArgumentMatchers.argThat(arg -> arg.equals(2))
        );
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "two", 3.14};
        when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                new CapturesArguments()
        );
        invocationMatcher.captureArgumentsFrom(invocation);
        Matcher capturedMatcher = matchers.get(1);
        Object capturedArgument = capturedMatcher.getCaptured();
        assertThat(capturedArgument, is(arguments[0]));
    }

    @Test
    public void testCaptureArgumentsFromInvocation_withVarArgs() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "two", 3.14, new Object[]{4, 5}};
        when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(arg -> arg instanceof Integer),
                new CapturesArguments(),
                new CapturesArguments()
        );
        invocationMatcher.captureArgumentsFrom(invocation);
        Matcher capturedMatcher1 = matchers.get(1);
        Object capturedArgument1 = capturedMatcher1.getCaptured();
        assertThat(capturedArgument1, is(arguments[0]));
        Matcher capturedMatcher2 = matchers.get(2);
        Object[] capturedArgument2 = (Object[]) capturedMatcher2.getCaptured();
        assertThat(capturedArgument2, is(arguments[arguments.length - 1]));
    }

    @Test
    public void testCreateFrom_withEmptyListOfInvocations_shouldReturnEmptyListOfInvocationMatchers() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertTrue(invocationMatchers.isEmpty());
    }

    @Test
    public void testCreateFrom_withNonEmptyListOfInvocations_shouldReturnListOfInvocationMatchers() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertThat(invocationMatchers.size(), is(2));
    }

    private Answer<Object> mockMethodWithReturnValue(Object returnValue) {
        return invocation -> returnValue;
    }

    private void when(Method method, Object... arguments) {
        when(mock.method(method, ArgumentMatchers.any(), ArgumentMatchers.anyArray())).thenAnswer(mockMethodWithReturnValue(arguments[0]));
    }
}