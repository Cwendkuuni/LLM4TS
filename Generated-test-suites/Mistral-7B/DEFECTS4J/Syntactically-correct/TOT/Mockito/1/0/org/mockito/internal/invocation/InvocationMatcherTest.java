package org.mockito.internal.invocation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_sameMethodWithSameArguments() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithSameNameAndArguments", int.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method, 1, 2);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMock(mock, method, 1, 2);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assertions.assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcher_sameMethodWithDifferentArguments() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithSameName", int.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method, 1, 2);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMock(mock, method, 3, 4);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assertions.assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcher_differentMethods() {
        Object mock = Mockito.mock(Object.class);
        Method method1 = Mockito.method(Object.class, "method1", int.class);
        Method method2 = Mockito.method(Object.class, "method2", int.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock, method1, 1);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMock(mock, method2, 1, 2);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assertions.assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcher_differentMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithSameNameAndArguments", int.class, int.class);

        Invocation invocation1 = new InvocationOnMock(mock1, method, 1, 2);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMock(mock2, method, 1, 2);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assertions.assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcher_unverifiedInvocation() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithSameNameAndArguments", int.class, int.class);
        Invocation invocation = new InvocationOnMock(mock, method, 1, 2);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Mockito.unverify(mock).methodWithSameNameAndArguments(1, 2);

        Invocation unverifiedInvocation = new InvocationOnMock(mock, method, 1, 2);
        InvocationMatcher unverifiedMatcher = new InvocationMatcher(unverifiedInvocation);

        Assertions.assertTrue(matcher.matches(unverifiedMatcher));
    }

    @Test
    public void testInvocationMatcher_vararg_sameMatcher() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithVararg", int[].class);

        Invocation invocation = new InvocationOnMock(mock, method, new Object[]{new int[]{1, 2}});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        InvocationMatcher varargMatcher = new InvocationMatcher(matcher.getMatchers().get(0));

        Mockito.verify(mock, Mockito.times(1)).methodWithVararg((Object) new int[]{1, 2});
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInvocationMatcher_vararg_differentMatcher() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithVararg", int[].class);

        Invocation invocation = new InvocationOnMock(mock, method, new Object[]{new int[]{1, 2}});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        InvocationMatcher varargDifferentMatcher = new InvocationMatcher(matcher.getMatchers().get(1));

        Mockito.verifyNoInteractions(mock);
    }

    @Test
    public void testInvocationMatcher_vararg_sameInvocation() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithVararg", int[].class);

        Invocation invocation = new InvocationOnMock(mock, method, new Object[]{new int[]{1, 2}});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);

        Invocation invocation2 = new InvocationOnMock(mock, method, new Object[]{new int[]{1, 2}});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Mockito.verify(mock, Mockito.times(2)).methodWithVararg((Object) new int[]{1, 2});

        Answer<Void> answer = Mockito.answer(inv -> {});
        MethodInterceptor interceptor = Mockito.mock(MethodInterceptor.class);
        Mockito.doAnswer(answer).when(interceptor).invoke(Mockito.any(InvocationOnMock.class), Mockito.eq(method));

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(matcher1, matcher2));
        Mockito.doCallRealMethod().when(mock).methodWithVararg((Object) anyArray());
        Mockito.doAnswer(inv -> {
            matchers.get(0).captureArgumentsFrom(inv.getArgumentAt(0, InvocationOnMock.class));
            matchers.get(1).captureArgumentsFrom(inv.getArgumentAt(1, InvocationOnMock.class));
            return null;
        }).when(interceptor).intercept(Mockito.argThat(m -> m.getClass().isAssignableFrom(InvocationOnMock.class)), Mockito.eq(method));

        Invocation invocationInterceptor = new InvocationOnMock(mock, method, new Object[]{new int[]{1, 2}});
        invocationInterceptor.interceptors().add(interceptor);

        matcher1.captureArgumentsFrom(invocationInterceptor);
        matcher2.captureArgumentsFrom(invocationInterceptor);

        Assertions.assertArrayEquals(new int[]{1, 2}, matcher1.getMatchers().get(0).getArguments().get(0));
        Assertions.assertArrayEquals(new int[]{1, 2}, matcher2.getMatchers().get(0).getArguments().get(0));
    }

    private Object anyArray() {
        return Mockito.anyArray();
    }
}