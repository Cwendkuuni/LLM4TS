package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.verification.VerificationModeFactory.times;

public class MockHandlerTest {

 private MockHandler<MyMock> mockHandler;
 private MyMock mock;

 @Before
 public void setUp() {
 mock = Mockito.mock(MyMock.class);
 mockHandler = new MockHandler<MyMock>(mock);
 }

 @Test
 public void testHandle_noAnswers_invocationIsPassedToRealObject() {
 final int input = 1;
 final int expectedOutput = 2;

 when(mock.someMethod(input)).thenReturn(expectedOutput);

 int actualOutput = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{input}));

 assertThat(actualOutput).isEqualTo(expectedOutput);
 }

 @Test
 public void testHandle_answersPresent_answerIsReturned() {
 mockHandler.setAnswersForStubbing(Arrays.asList(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 }));

 String actualOutput = (String) mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));

 assertThat(actualOutput).isEqualTo("answer");
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_verificationModePresent_verifyIsCalledOnCorrectMock() {
 mockHandler.setAnswersForStubbing(Arrays.asList(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 }));

 mockHandler.mockingProgress.pushVerificationMode(Mockito.verify(mock, times(1)).someMethod());

 mockHandler.handle(new InvocationOnMock(mock, "someOtherMethod", new Object[0]));
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verificationModePresent_verifyIsCalledOnDifferentMock() {
 MyMock differentMock = Mockito.mock(MyMock.class);
 mockHandler.setAnswersForStubbing(Arrays.asList(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 }));

 mockHandler.mockingProgress.pushVerificationMode(Mockito.verify(differentMock, times(1)).someMethod());

 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[0]));
 }

 @Test
 public void testVoidMethodStubbable() {
 mockHandler.voidMethodStubbable(mock).stubVoid().when(mock).someVoidMethod();

 mockHandler.handle(new InvocationOnMock(mock, "someVoidMethod"));
 }
}

class MyMock {
 void someVoidMethod() {}
 int someMethod(int input) { return input * 2; }
}