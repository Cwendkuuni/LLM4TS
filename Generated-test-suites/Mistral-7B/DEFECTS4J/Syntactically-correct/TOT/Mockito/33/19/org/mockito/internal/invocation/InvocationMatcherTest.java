package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.invocation.ProxyInvocationHandler;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testMatchesInvocationWithMatchers() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(InvocationMatcher.class).getMethod("hasSimilarMethod", Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, method), Arrays.asList(argThat(isA(InvocationMatcher.class))));

        // When & Then
        InvocationMatcher target = new InvocationMatcher(new InvocationOnMock(mock, method));
        assertEquals(invocationMatcher, target);
    }

    @Test
    public void testMatchesInvocationsWithSameMethod() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(InvocationMatcher.class).getMethod("hasSameMethod", Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(mock, method), Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method), Collections.emptyList());

        // When
        MethodInterceptor interceptor = Mockito.mock(MethodInterceptor.class);
        ProxyInvocationHandler handler = Mockito.mock(ProxyInvocationHandler.class);
        handler.setSuperclass(InvocationMatcher.class);
        handler.setInterfaces(new Class[0]);
        handler.setTarget(invocationMatcher1);
        handler.setInterceptors(new Object[]{interceptor});

        // When invoking the method
        handler.invokeMethod(method, new Object[0]);

        // Then, the interceptor should be called only once
        verify(interceptor).intercept(any(InvocationOnMock.class));

        // When & Then
        assertEquals(invocationMatcher1, invocationMatcher2);
    }

    @Test
    public void testMatchesInvocationsWithDifferentMethods() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Method method1 = Mockito.spy(InvocationMatcher.class).getMethod("hasSameMethod", Invocation.class);
        Method method2 = Mockito.spy(InvocationMatcher.class).getMethod("hasSimilarMethod", Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(mock, method1), Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method2), Collections.emptyList());

        // When
        MethodInterceptor interceptor = Mockito.mock(MethodInterceptor.class);
        ProxyInvocationHandler handler = Mockito.mock(ProxyInvocationHandler.class);
        handler.setSuperclass(InvocationMatcher.class);
        handler.setInterfaces(new Class[0]);
        handler.setTarget(invocationMatcher1);
        handler.setInterceptors(new Object[]{interceptor});

        // When invoking the method
        handler.invokeMethod(method1, new Object[0]);

        // Then, the interceptor should be called only once
        verify(interceptor).intercept(any(InvocationOnMock.class));

        // When
        handler.setTarget(invocationMatcher2);

        // When invoking the method
        handler.invokeMethod(method1, new Object[0]);

        // Then, the interceptor should be called twice
        verify(interceptor, times(2)).intercept(any(InvocationOnMock.class));

        // When & Then
        assertFalse(invocationMatcher1.equals(invocationMatcher2));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesInvocationsWithDifferentArguments() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(InvocationMatcher.class).getMethod("matches", Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(mock, method), Arrays.asList(argThat(isA(Integer.class))));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method), Arrays.asList(argThat(isA(String.class))));

        // When & Then
        Invocation invocation1 = new Invocation(mock, method, new Object[]{1}, new Location(0, 0));
        Invocation invocation2 = new Invocation(mock, method, new Object[]{"test"}, new Location(0, 0));

        assertEquals(invocationMatcher1, invocationMatcher1);
        assertFalse(invocationMatcher1.matches(invocation2));

        // When
        when(invocationMatcher1.matches(any(Invocation.class))).thenReturn(true);

        // Then
        assertEquals(invocationMatcher1, invocationMatcher1);
        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher1.matches(invocation2));
    }

    @Captor
    private ArgumentCaptor<InvocationMatcher> invocationMatcherCaptor;

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(InvocationMatcher.class).getMethod("captureArgumentsFrom", Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, method), Collections.emptyList());

        // When
        InvocationMatcher target = Mockito.spy(InvocationMatcher.class);
        invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock, method));

        // Then
        verify(target).captureArgumentsFrom(argThat(isA(InvocationMatcher.class)));

        // When
        verify(target, times(1)).captureArgumentsFrom(invocationMatcherCaptor.capture());

        // Then
        InvocationMatcher capturedInvocationMatcher = invocationMatcherCaptor.getValue();
        assertEquals(invocationMatcher, capturedInvocationMatcher);
    }
}