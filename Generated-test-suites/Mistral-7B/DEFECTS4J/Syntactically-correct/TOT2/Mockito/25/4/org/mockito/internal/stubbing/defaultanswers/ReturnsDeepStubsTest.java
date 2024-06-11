package org.example.junit.test.ReturnsDeepStubs;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.MethodStub;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testReturnsDeepStubs() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs);
    }

    @Test
    public void testAnswerWithNullInvocation() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNull(returnsDeepStubs.answer(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnswerWithInvalidReturnType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = new MethodStub(); // An invalid method
        invocation.setMethod(method);
        try {
            returnsDeepStubs.answer(invocation);
        } catch (Throwable e) {} // swallow the exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerWithNonMockableReturnType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(List.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = new MethodStub();
        invocation.setMethod(method);
        try {
            returnsDeepStubs.answer(invocation);
        } catch (Throwable e) {}
    }

    @Test
    public void testAnswerWithAlreadyStubbedInvocation() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        container.addAnswer(stubbedInvocationMatcher, false);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);
        assertSame(mock, returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testAnswerWithGenericReturnType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Map.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(Map.Entry.class);
        when(invocation.getMock()).thenReturn(mock);
        assertSame(mock, returnsDeepStubs.answer(invocation));
    }
}