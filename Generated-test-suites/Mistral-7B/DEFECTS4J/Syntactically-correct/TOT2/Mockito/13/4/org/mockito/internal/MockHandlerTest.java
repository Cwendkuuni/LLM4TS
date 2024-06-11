package org.mockito.internal.test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    MockHandler<MyMockType> mockHandler;

    @Before
    public void setup() {
        mockHandler = Mockito.spy(new MockHandler<MyMockType>());
    }

    // Constructor test case
    @Test
    public void testConstructor() {
        MockHandler<MyMockType> mockHandler = new MockHandler<MyMockType>(new MockSettingsImpl());
        Mockito.assertNotNull(mockHandler);
    }

    // Handle method test cases
    @Test
    public void testHandleMethod_whenInvokedWithMatchingArguments_thenReturnNullAndInvokeVerificationOrAnswer() {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Invocation invocation = new Invocation(invocationMock.getMock(), new Object[]{}, new Class[0], "methodName", new Object[0]);
        Answer answer = Answers.returnValues(new Object());
        doAnswer(answer).when(mockHandler).handle(eq(invocation));

        mockHandler.handle(invocation);
        verify(invocationMock).answer(answer);
    }

    @Test
    public void testHandleMethod_whenInvokedWithVoidReturnType_thenReturnNull() {
        Invocation invocation = new Invocation(null, new Object[]{}, new Class[0], "methodName", new Object[0]);
        mockHandler.handle(invocation);
        verifyNoMoreInteractions(mockHandler);
    }

    @Test
    public void testHandleMethod_whenInvokedWithMultipleArguments_thenReturnNullAndInvokeVerificationOrAnswer() {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{new Object(), new Object()};
        Invocation invocation = new Invocation(invocationMock.getMock(), arguments, new Class[0], "methodName", new Object[0]);
        Answer answer = Answers.returnValues(new Object());
        doAnswer(answer).when(mockHandler).handle(eq(invocation));

        mockHandler.handle(invocation);
        verify(invocationMock).answer(answer);
    }

    @Test(expected = RuntimeException.class)
    public void testHandleMethod_whenInvokedWithException_thenThrowException() {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Invocation invocation = new Invocation(invocationMock.getMock(), new Object[]{}, new Class[0], "methodName", new Object[]{"ExceptionClass"});
        doThrow(new Exception("Exception message")).when(mockHandler).handle(eq(invocation));

        mockHandler.handle(invocation);
    }

    // VoidMethodStubbable test cases
    @Test
    public void testVoidMethodStubbable_whenStubbed_thenInvokeAnswer() {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Answer answer = Answers.returnValues(new Object());
        VoidMethodStubbable<MyMockType> voidMethodStubbable = mockHandler.voidMethodStubbable(invocationMock.getMock());
        voidMethodStubbable.doAnswer(answer).when(mockHandler).handle(isA(InvocationOnMock.class));

        invocationMock.answer(answer);
        mockHandler.handle(new Invocation(invocationMock.getMock(), new Object[0], new Class[0], "methodName", new Object[0]));
        verify(invocationMock).answer(answer);
    }

    @Test
    public void testVoidMethodStubbable_whenInvokedWithNullMock_thenReturnDefaultVoidMethodStubbable() {
        VoidMethodStubbable<MyMockType> voidMethodStubbable = mockHandler.voidMethodStubbable(null);
        Mockito.assertNotNull(voidMethodStubbable);
    }
}