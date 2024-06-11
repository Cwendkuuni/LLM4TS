package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<SomeMockType> mockHandler;
 private SomeMockType mockObject;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockObject = mock(SomeMockType.class);
 }

 @Test
 public void testHandle_invocationWithStubbing() {
 // given
 when(mockObject.someMethod()).thenReturn("stubbed result");

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));

 // then
 verify(mockObject).someMethod();
 assertEquals("stubbed result", result);
 }

 @Test
 public void testHandle_invocationWithoutStubbing() {
 // given
 // no stubbing is set

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));

 // then
 assertNull(result);
 }

 @Test
 public void testHandle_invocationWithVerification() {
 // given
 // no stubbing is set

 // when
 mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));

 // then
 verify(mockObject).someMethod();
 }

 @Test
 public void testHandle_invocationWithMultipleVerifications() {
 // given
 // no stubbing is set

 // when
 mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));
 mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));

 // then
 verify(mockObject, times(2)).someMethod();
 }

 @Test
 public void testHandle_invocationWithMatchers() {
 // given
 when(mockObject.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[]{1}));

 // then
 verify(mockObject).someMethod(ArgumentMatchers.eq(1));
 assertEquals(42, result);
 }

 @Test
 public void testHandle_invocationWithException() {
 // given
 when(mockObject.someMethod()).thenThrow(new RuntimeException("expected exception"));

 // when
 try {
 Object result = mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));
 } catch (RuntimeException e) {
 // then
 assertEquals("expected exception", e.getMessage());
 }
 }

 @Test
 public void testHandle_invocationWithVoidMethodStubbing() {
 // given
 VoidMethodStubbable<SomeMockType> voidMethodStubbable = mockHandler.voidMethodStubbable(mockObject);
 voidMethodStubbable.doNothing();

 // when
 mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));

 // then
 verify(mockObject).someMethod();
 }

 @Test
 public void testHandle_invocationWithListAnswer() {
 // given
 List<String> listAnswer = Arrays.asList("answer1", "answer2");
 when(mockObject.someMethod()).thenAnswer(invocation -> listAnswer);

 // when
 List<String> result = (List<String>) mockHandler.handle(new InvocationOnMock(mockObject, "someMethod", new Object[0]));

 // then
 assertEquals(listAnswer, result);
 }
}