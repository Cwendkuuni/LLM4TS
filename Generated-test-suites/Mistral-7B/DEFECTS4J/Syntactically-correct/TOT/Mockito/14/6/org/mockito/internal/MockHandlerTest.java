package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class MockHandlerTest {

 private MockHandler<MyMock> mockHandler;
 private MyMock mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mock = mock(MyMock.class);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithMatchingStub_thenReturnStubbedValue() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 doAnswer(Arguments.arguments(42)).when(mock).myMethod(ArgumentMatchers.anyInt());

 Object result = mockHandler.handle(invocation);

 assertEquals(42, result);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithNoMatchingStub_thenReturnDefaultValue() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 doNothing().when(mockHandler).handle(invocation);
 Object result = mockHandler.handle(invocation);

 assertNull(result);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithVerificationMode_thenVerifyMethodCall() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.verify(mock).myMethod(ArgumentMatchers.anyInt());
 doNothing().when(mockHandler).handle(invocation);
 mockHandler.handle(invocation);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_whenInvokingAMethodWithVerificationModeOnDifferentMock_thenThrowAssertionError() {
 MyMock otherMock = mock(MyMock.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 doThrow(new AssertionError("expected verification error")).when(mock).myMethod(ArgumentMatchers.anyInt());
 doNothing().when(mockHandler).handle(invocation);
 mockHandler.handle(invocation);
 verify(otherMock).myMethod(ArgumentMatchers.anyInt());
 }

 @Test
 public void testHandle_whenInvokingAMethodWithVoidAnswer_thenReturnNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Answer<Object> answer = Answer.returningNull();
 doAnswer(answer).when(mock).myMethod(ArgumentMatchers.anyInt());
 Object result = mockHandler.handle(invocation);

 assertNull(result);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithVoidMethodStubbable_thenReturnNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 VoidMethodStubbable<MyMock> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 voidMethodStubbable.when(mock).myMethod();
 Object result = mockHandler.handle(invocation);

 assertNull(result);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithListAnswer_thenReturnAnswerList() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Answer<List<Integer>> answer = Answer.returning(Arrays.asList(1, 2, 3));
 doAnswer(answer).when(mock).myMethod(ArgumentMatchers.anyInt());
 List<Integer> result = (List<Integer>) mockHandler.handle(invocation);

 assertEquals(Arrays.asList(1, 2, 3), result);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithMultipleAnswers_thenReturnFirstMatchingAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 List<Answer<Object>> answers = Arrays.asList(
         Answer.returning(1),
         Answer.returning("two"),
         Answer.returning(Collections.singletonMap("key", "value"))
 );
 doAnswer(answers.get(0)).when(mock).myMethod(anyInt());
 Object result = mockHandler.handle(invocation);

 assertEquals(1, result);
 }

 @Test
 public void testHandle_whenInvokingAMethodWithVerificationAndAnswer_thenVerifyAndReturnAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 doAnswer(Arguments.arguments(42)).when(mock).myMethod(ArgumentMatchers.anyInt());
 doThrow(new RuntimeException("expected exception")).when(mock).myMethod(anyInt());
 doAnswer(Arguments.arguments(7)).when(mock).myMethod(anyInt());
 Object result = mockHandler.handle(invocation);

 assertEquals(7, result);
 verify(mock).myMethod(ArgumentMatchers.anyInt());
 }

 @Test
 public void testHandle_whenInvokingAMethodWithVerificationAndNoAnswer_thenVerifyButReturnDefaultValue() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 doThrow(new RuntimeException("expected exception")).when(mock).myMethod(anyInt());
 doNothing().when(mockHandler).handle(invocation);
 Object result = mockHandler.handle(invocation);

 assertNull(result);
 verify(mock).myMethod(anyInt());
 }
}

class MyMock {
 public int myMethod(int arg) {
 return arg;
 }
}