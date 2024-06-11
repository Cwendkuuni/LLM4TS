package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setup() {
 mock = mock(DummyClass.class);
 mockHandler = new MockHandler<DummyClass>(mock);
 }

 @Test
 public void testHandle_noAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenReturn("Return value from mocked handler");

 String result = (String) mockHandler.handle(invocation);

 verify(mock).handle(invocation);
 assertNotNull(result);
 assertEquals("Return value from mocked handler", result);
 }

 @Test
 public void testHandle_answer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenAnswer(Answers.returnValues("Answer 1", "Answer 2"));

 String result1 = (String) mockHandler.handle(invocation);
 String result2 = (String) mockHandler.handle(invocation);

 verify(mock, Mockito.times(2)).handle(invocation);
 assertEquals("Answer 1", result1);
 assertEquals("Answer 2", result2);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_stubbingError() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenThrow(new IllegalStateException("Stubbing error message"));

 try {
 mockHandler.handle(invocation);
 } catch (Throwable e) {
 throw new AssertionError("Unexpected exception", e);
 }

 verify(mock, Mockito.never()).handle(invocation);
 }

 @Test
 public void testHandle_verification() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 verify(mock).handle(invocation);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verificationError() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.handle(invocation)).thenThrow(new RuntimeException("Handle method throws an exception"));

 try {
 mockHandler.handle(invocation);
 } catch (Throwable e) {
 throw new AssertionError("Unexpected exception", e);
 }

 verify(mock).handle(invocation);
 }
}