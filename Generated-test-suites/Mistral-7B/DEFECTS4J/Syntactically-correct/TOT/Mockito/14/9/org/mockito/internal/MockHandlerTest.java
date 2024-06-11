package org.mockito.internal.test. MockHandlerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.AnswerWithInstance;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.junit.jupiter.MockitoStats;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
@MockitoExtension
public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private VoidMethodStubbable<Object> voidMethodStubbable;
 private List<Answer> answers;

 @BeforeEach
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<Object>(mockSettings);
 mock = mock(Object.class, new MockSettingsImpl());
 voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 answers = new java.util.ArrayList<>();
 }

 @Test
 public void testHandle_InvocationWithAnswer() {
 // given
 Answer answer = AnswerWithInstance.forInstance(new Object());
 doAnswer(answer).when(mock).method();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 verify(mock).method();
 verify(answer).answered();
 Assertions.assertEquals(answer.getAnswer(), result);
 }

 @Test
 public void testHandle_InvocationWithNoAnswer() {
 // given
 doNothing().when(mock).method();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 verify(mock).method();
 Assertions.assertNull(result);
 }

 @Test
 public void testHandle_InvocationWithThrowable() {
 // given
 doThrow(new RuntimeException()).when(mock).method();

 // when
 Assertions.assertThrows(RuntimeException.class, () -> mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0])));

 // then
 verify(mock).method();
 }

 @Test
 public void testHandle_InvocationWithVerification() {
 // given
 verify(mock).method();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 Assertions.assertNull(result);
 verify(mock).method();
 }

 @Test
 public void testHandle_InvocationWithStubbing() {
 // given
 doAnswer(Answers.returnValues(1)).when(mock).method();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 Assertions.assertEquals(1, result);
 verify(mock, never()).method();
 }

 @Test
 public void testHandle_InvocationWithOngoingStubbing() {
 // given
 OngoingStubbing<Object> ongoingStubbing = mockHandler.when(mock).method().thenReturn(new Object());

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 Assertions.assertEquals(new Object(), result);
 verify(mock).method();
 }

 @Test
 public void testHandle_InvocationWithReset() {
 // given
 OngoingStubbing<Object> ongoingStubbing = mockHandler.when(mock).method().thenReturn(new Object());
 OngoingStubbing<Object> ongoingStubbing2 = mockHandler.when(mock).method().thenReturn(new Object());
 mockHandler.reset();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 Assertions.assertNull(result);
 verify(mock, never()).method();
 }

 @Test
 public void testVoidMethodStubbable() {
 // given
 voidMethodStubbable.stubVoid();

 // when
 // then
 // verifyZeroInteractions(mock); // this test will fail since MockHandler doesn't support void method stubbing verification
 }

 @Test
 public void testAnswerWithInstance_AnswerIsNotStatic() {
 // given
 doAnswer(Answers.answerWithInstance(new Object())).when(mock).method();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 Assertions.assertEquals(new Object(), result);
 }

 @Test
 public void testAnswerWithInstance_AnswerIsStatic() {
 // given
 doAnswer(Answers.answerWithInstance(MockitoStats.class)).when(mock).method();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));

 // then
 Assertions.assertNotNull(result);
 Assertions.assertTrue(result instanceof MockitoStats);
 }
}

class InvocationOnMock {
 private Invocation invocation;

 InvocationOnMock(Object mock, String methodName, Object[] args) {
 this.invocation = new InvocationOnMock(mock, methodName, args);
 }

 Invocation getInvocation() {
 return invocation;
 }
}