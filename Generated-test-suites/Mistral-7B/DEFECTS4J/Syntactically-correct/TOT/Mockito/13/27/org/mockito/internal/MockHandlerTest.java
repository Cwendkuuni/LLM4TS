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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mockDummyClass;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockDummyClass = mock(DummyClass.class);
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 // given
 when(mockDummyClass.doSomething()).thenCallRealMethod();

 // when
 mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));

 // then
 // no verification or stubbing happened, as the method was not stubbed
 verifyNoMoreInteractions(mockDummyClass);
 }

 @Test
 public void testHandleMethodInvocationWithAnswer() {
 // given
 when(mockDummyClass.doSomething()).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));

 // then
 // verification and stubbing did not happen, as the method was stubbed
 // with an Answer
 assertEquals(42, result);
 verifyNoMoreInteractions(mockDummyClass);
 }

 @Test
 public void testHandleMethodInvocationWithStub() {
 // given
 when(mockDummyClass.doSomething()).thenReturn(42);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));

 // then
 // verification and stubbing happened
 verify(mockDummyClass).doSomething();
 assertEquals(42, result);
 }

 @Test
 public void testHandleMethodInvocationWithException() {
 // given
 when(mockDummyClass.doSomething()).thenThrow(new RuntimeException("Test Exception"));

 // when
 try {
 mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));
 } catch (Throwable e) {
 // then
 // verification and stubbing did not happen
 assertEquals("Test Exception", e.getMessage());
 }

 // then
 verifyNoMoreInteractions(mockDummyClass);
 }

 @Test
 public void testHandleMethodInvocationWithMultipleAnswers() {
 // given
 List<Answer> answers = Arrays.asList(
 new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 },
 new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 2;
 }
 },
 new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 3;
 }
 }
 );
 mockHandler.setAnswersForStubbing(answers);
 when(mockDummyClass.doSomething()).thenAnswer(Answer.returning(1));

 // when
 int result1 = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));
 int result2 = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));
 int result3 = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));

 // then
 // verification and stubbing did not happen
 assertEquals(1, result1);
 assertEquals(2, result2);
 assertEquals(3, result3);
 verifyNoMoreInteractions(mockDummyClass);
 }

 @Test
 public void testHandleMethodInvocationWithVerification() {
 // given
 when(mockDummyClass.doSomething()).thenReturn(42);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));

 // then
 // verification and stubbing happened
 verify(mockDummyClass).doSomething();
 assertEquals(42, result);
 }

 @Test
 public void testHandleMethodInvocationWithVerifyNoMoreInteractions() {
 // given
 when(mockDummyClass.doSomething()).thenReturn(42);
 verify(mockDummyClass).doSomething();

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[0]));

 // then
 // verification and stubbing happened
 // and no more interactions should be allowed
 verifyNoMoreInteractions(mockDummyClass);
 assertEquals(42, result);
 }

 @Test
 public void testHandleMethodInvocationWithArgumentMatcher() {
 // given
 when(mockDummyClass.doSomething(ArgumentMatchers.eq("test"))).thenReturn(42);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mockDummyClass, "doSomething", new Object[]{"test"}));

 // then
 // verification and stubbing happened
 // and the argument was passed to the verification
 verify(mockDummyClass).doSomething(ArgumentMatchers.eq("test"));
 assertEquals(42, result);
 }
}

class DummyClass {
 public void doSomething() {
 // empty method
 }

 public void doSomething(String arg) {
 // empty method
 }
}