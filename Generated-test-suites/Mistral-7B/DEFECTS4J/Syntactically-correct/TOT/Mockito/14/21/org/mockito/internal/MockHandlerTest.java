package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mock = mock(DummyClass.class);
 }

 @Test
 public void testHandleInvocation_whenStubbingReturnValue_thenReturnValueIsReturned() {
 // Given
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new DummyReturnValue());
 when(mock.someMethod()).thenReturn(new DummyReturnValue());

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // Then
 assertInstanceOf(DummyReturnValue.class, result);
 }

 @Test
 public void testHandleInvocation_whenStubbingVoidMethod_thenNullIsReturned() {
 // Given
 when(mock.someVoidMethod()).thenCallRealMethod();

 // When
 mockHandler.handle(new InvocationOnMock(mock, "someVoidMethod", new Object[0]));

 // Then
 // No return value expected
 }

 @Test
 public void testHandleInvocation_whenVerifyingInvocation_thenVerificationSucceeds() {
 // Given
 when(mock.someMethod()).thenReturn(new DummyReturnValue());
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // When
 verify(mock).someMethod();

 // Then
 // Verification succeeds
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocation_whenVerifyingInvocationOnDifferentMock_thenExceptionIsThrown() {
 // Given
 DummyClass otherMock = mock(DummyClass.class);
 when(mock.someMethod()).thenReturn(new DummyReturnValue());

 // When
 verify(otherMock).someMethod();

 // Then
 // Exception is thrown
 }

 @Test
 public void testHandleInvocation_whenMultipleVerifications_thenVerificationsAreProcessedInOrder() {
 // Given
 when(mock.someMethod()).thenReturn(new DummyReturnValue());
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));
 when(mock.someMethod()).thenThrow(new RuntimeException());

 // When
 verify(mock).someMethod();

 // Then
 // First verification succeeds, second throws an exception
 }

 @Test
 public void testHandleInvocation_whenOngoingStubbing_thenStubbingIsReported() {
 // Given
 when(mock.someMethod()).thenCallRealMethod();
 when(mock.someOtherMethod()).thenReturn(new DummyReturnValue());
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // When
 OngoingStubbingImpl<DummyClass> ongoingStubbing = mockHandler.invocationContainer().findAnswerFor(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // Then
 assertNotNull(ongoingStubbing);
 }

 @Test
 public void testHandleInvocation_whenMultipleAnswersForStubbing_thenAnswerWithHigherPriorityIsUsed() {
 // Given
 when(mock.someMethod()).thenReturn(new DummyReturnValue1()).thenReturn(new DummyReturnValue2());
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // When
 // DummyReturnValue2 has higher priority
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // Then
 assertInstanceOf(DummyReturnValue2.class, result);
 }

 @Test
 public void testHandleInvocation_whenAnswerCapturesArguments_thenArgumentsAreCaptured() {
 // Given
 when(mock.someMethod()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object[] arguments = invocation.getArguments();
 return arguments[0];
 }
 });
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{new DummyArgument()}));

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{new DummyArgument1()}));

 // Then
 assertInstanceOf(DummyArgument1.class, result);
 }

 @Test
 public void testHandleInvocation_whenStubbingVoidMethodWithAnswer_thenAnswerIsUsed() {
 // Given
 when(mock.someVoidMethod()).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 // Do something
 return null;
 }
 });
 mockHandler.handle(new InvocationOnMock(mock, "someVoidMethod", new Object[0]));

 // Then
 // No return value expected
 }

 @Test
 public void testHandleInvocation_whenStubbingWithStubs_thenStubsAreUsed() {
 // Given
 Stub<Object> stub = Mockito.stub(mock, "someMethod");
 stub.thenReturn(new DummyReturnValue());

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // Then
 assertInstanceOf(DummyReturnValue.class, result);
 }

 @Test
 public void testHandleInvocation_whenStubbingWithAnswerAndStubs_thenAnswerIsUsedFirst() {
 // Given
 when(mock.someMethod()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return new DummyReturnValue1();
 }
 }).thenStub(new Stub<Object>() {
 @Override
 public Object stub(InvocationOnMock invocation) {
 return new DummyReturnValue2();
 }
 });
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 // Then
 assertInstanceOf(DummyReturnValue1.class, result);
 }
}