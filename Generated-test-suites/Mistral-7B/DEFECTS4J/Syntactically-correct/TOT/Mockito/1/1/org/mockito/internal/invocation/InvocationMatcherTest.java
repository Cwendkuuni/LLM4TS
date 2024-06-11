package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
    }

    @Test
    public void testBasicMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, 2, 3};

        invocationMatcher = new InvocationMatcher(invocation, ArgumentsProcessor.argumentsToMatchers(arguments));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2), ArgumentMatchers.eq(3)));

        assertEquals(expectedMatcher, invocationMatcher);
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(arguments, invocationMatcher.getMatchers().toArray());
    }

    @Test
    public void testEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);

        invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals(expectedMatcher, invocationMatcher);
        assertEmpty(invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, 2, 3};

        invocationMatcher = new InvocationMatcher(invocation, ArgumentsProcessor.argumentsToMatchers(arguments));

        String expected = "InvocationMatcher(mockito-invocation: Invocation [method=Method [name=any, returnType=class java.lang.Object]], arguments=[1, 2, 3])";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, 2, 3};

        invocationMatcher = new InvocationMatcher(invocation, ArgumentsProcessor.argumentsToMatchers(arguments));
        InvocationMatcher otherMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2), ArgumentMatchers.eq(3)));

        assertTrue(invocationMatcher.matches(otherMatcher));
        assertTrue(otherMatcher.matches(invocationMatcher));
    }

    @Test
    public void testDoesNotMatchDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, 2, 3};

        invocationMatcher = new InvocationMatcher(invocation1, ArgumentsProcessor.argumentsToMatchers(arguments));

        assertFalse(invocationMatcher.matches(new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(4), ArgumentMatchers.eq(3)))));
    }

    @Test
    public void testDoesNotMatchDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, 2, 3};

        invocationMatcher = new InvocationMatcher(invocation1, ArgumentsProcessor.argumentsToMatchers(arguments));

        assertFalse(invocationMatcher.matches(new InvocationMatcher(invocation2, ArgumentsProcessor.argumentsToMatchers(arguments))));
    }

    @Test
    public void testDoesNotMatchUnverifiedInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, 2, 3};

        invocationMatcher = new InvocationMatcher(invocation, ArgumentsProcessor.argumentsToMatchers(arguments));

        Invocation otherInvocation = Mockito.mock(Invocation.class);
        assertFalse(invocationMatcher.matches(new InvocationMatcher(otherInvocation, Collections.emptyList())));
    }

    @Test
    public void testCaptureArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object arg1 = Mockito.mock(Object.class);
        Object arg2 = Mockito.mock(Object.class);
        Object arg3 = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{arg1, arg2, arg3};

        invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.argThat(new ArgumentCaptureMatcher())));
        invocationMatcher.captureArgumentsFrom(new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arg1), ArgumentMatchers.eq(arg2), ArgumentMatchers.eq(arg3))));

        verify(arg1).doSomething();
        verify(arg2).doSomethingElse();
        verify(arg3).doSomethingElse();
    }

    @Test
    public void testCaptureArgumentsVararg() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, new Object[]{2, 3, 4}};

        invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.argThat(new ArgumentCaptureMatcher()), ArgumentMatchers.argThat(new VarargCaptureMatcher())));
        invocationMatcher.captureArgumentsFrom(new InvocationMatcher(invocation, ArgumentsProcessor.argumentsToMatchers(arguments)));

        verify(arguments[1][0]).doSomething();
        verify(arguments[1][1]).doSomethingElse();
        verify(arguments[1][2]).doSomethingElse();
    }

    @Test
    public void testDoesNotCaptureArgumentsFromDifferentInvocations() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object arg = Mockito.mock(Object.class);

        invocationMatcher = new InvocationMatcher(invocation1, Collections.singletonList(ArgumentMatchers.argThat(new ArgumentCaptureMatcher())));
        invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(ArgumentMatchers.argThat(new ArgumentCaptureMatcher())));

        invocationMatcher.captureArgumentsFrom(new InvocationMatcher(invocation1, Collections.singletonList(ArgumentMatchers.eq(arg))));
        verifyZeroInteractions(arg);

        invocationMatcher2.captureArgumentsFrom(new InvocationMatcher(invocation2, Collections.singletonList(ArgumentMatchers.eq(arg))));
        verifyZeroInteractions(arg);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertTrue(invocationMatcher2.hasSameMethod(invocationMatcher1));
    }

    @Test
    public void testDoesNotHaveSameMethodWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(method1.getName()).thenReturn("testMethod1");
        when(method2.getName()).thenReturn("testMethod2");

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher2.hasSameMethod(invocationMatcher1));
    }

    @Test
    public void testDoesNotHaveSameMethodWithDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMock()).thenReturn(mock);
        when(invocation2.getMock()).thenReturn(mock);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher2.hasSameMethod(invocationMatcher1));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Invocation invocation3 = Mockito.mock(Invocation.class);

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2, invocation3);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(3, invocationMatchers.size());
    }
}