package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setUp() {
 mock = mock(DummyClass.class);
 mockHandler = new MockHandler<>(mock);
 }

 @Test
 public void testHandle_voidMethodStubbing() {
 // given
 when(mock.voidMethod()).thenCallRealMethod();

 // when
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod", new Object[0]));

 // then
 // no verification or stubbing was performed, as the method is void
 // and no answer was set
 }

 @Test
 public void testHandle_answer() {
 // given
 when(mock.answerMethod()).thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mock, "answerMethod", new Object[0]));

 // then
 verify(mock).answerMethod();
 assertEquals("answer", result);
 }

 @Test
 public void testHandle_verification() {
 // given
 verify(mock).verifyMethodWithArguments(ArgumentMatchers.isA(String.class), ArgumentMatchers.eq("test"));

 // when
 mockHandler.handle(new InvocationOnMock(mock, "verifyMethodWithArguments", new Object[]{"test"}));

 // then
 // verification was performed on the mock
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_doubleVerification() {
 // given
 verify(mock).verifyMethodWithArguments(ArgumentMatchers.isA(String.class), ArgumentMatchers.eq("test"));

 // when
 try {
 mockHandler.handle(new InvocationOnMock(mock, "verifyMethodWithArguments", new Object[]{"test"}));
 } catch (IllegalStateException e) {
 // then
 // verification was already performed on the mock, so an exception is thrown
 }
 }

 @Test
 public void testHandle_stubbingWithAnswer() {
 // given
 List<Answer> answers = new ArrayList<>();
 answers.add(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer1";
 }
 });
 answers.add(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer2";
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 // when
 when(mock.methodWithArguments(ArgumentMatchers.isA(String.class), ArgumentMatchers.eq("argument")))
 .thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer3";
 }
 });

 // then
 Object result = mockHandler.handle(new InvocationOnMock(mock, "methodWithArguments", new Object[]{"argument"}));

 // then
 verify(mock).methodWithArguments(ArgumentMatchers.isA(String.class), ArgumentMatchers.eq("argument"));
 assertEquals("answer3", result);
 }

 @Test
 public void testHandle_stubbingWithStubVoid() {
 // given
 mockHandler.voidMethodStubbable(mock).stubVoid();

 // when
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod", new Object[0]));

 // then
 // no verification or stubbing was performed, as the method is void
 // and no answer was set
 }
}

class DummyClass {
 void voidMethod() {}
 String answerMethod() { return "test"; }
 void verifyMethodWithArguments(String argument) {}
 String methodWithArguments(String argument) { return "result"; }
}