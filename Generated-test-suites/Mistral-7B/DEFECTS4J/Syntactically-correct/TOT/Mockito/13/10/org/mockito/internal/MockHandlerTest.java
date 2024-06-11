package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import static org.mockito.Mockito.*; // for imports of various stubbing and verification methods

public class MockHandlerTest {

 private MockHandler<DummyMock> mockHandler;
 private DummyMock mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mock = Mockito.mock(DummyMock.class);
 }

 @Test
 public void testHandle_whenInvokingMethodOnStub_thenReturnDefaultAnswer() {
 given(mock.someMethod(ArgumentMatchers.anyInt())).willReturn(42);

 int result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Object[0]));

 assertEquals(42, result);
 }

 @Test
 public void testHandle_whenInvokingMethodOnStubWithStubbedAnswer_thenReturnAnswer() {
 given(mock.someMethod(ArgumentMatchers.anyInt())).willReturn(42);
 given(mockHandler.invocationContainerImpl.findAnswerFor(eq(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Object[0]))))
 .willReturn(answers(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return 7;
 }
 }));

 int result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Object[0]));

 assertEquals(7, result);
 }

 @Test(expected = InvocationOnMockException.class)
 public void testHandle_whenInvokingMethodOnStubWithNoMatchingStub_thenThrowException() {
 given(mock.someMethod(ArgumentMatchers.anyInt())).willReturn(42);

 try {
 mockHandler.handle(new InvocationOnMock(new Object(), "someMethod", new Object[]{1}, new Object[0]));
 } catch (InvocationOnMockException e) {
 throw new AssertionError("Exception should be thrown with different mock instance", e);
 }
 }

 @Test
 public void testHandle_whenInvokingMethodOnStubWithVerificationMode_thenVerifyMethodCall() {
 given(mock.someMethod(ArgumentMatchers.anyInt())).willReturn(42);

 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Object[0]));

 verify(mock).someMethod(ArgumentMatchers.eq(1));
 }

 @Test
 public void testHandle_whenInvokingMethodOnStubWithStubbingAndVerification_thenStubAndVerify() {
 given(mock.someMethod(ArgumentMatchers.anyInt())).willReturn(42);
 given(mockHandler.invocationContainerImpl.findAnswerFor(eq(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Object[0]))))
 .willReturn(answers(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("expected exception");
 }
 }));

 verify(mock).someMethod(ArgumentMatchers.eq(1));

 try {
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}, new Object[0]));
 } catch (RuntimeException e) {
 // expected exception
 }
 }
}

class DummyMock {
 int someMethod(int i);
}