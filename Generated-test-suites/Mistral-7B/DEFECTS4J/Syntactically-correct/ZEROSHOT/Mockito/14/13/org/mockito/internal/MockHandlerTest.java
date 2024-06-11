package org.mockito.internal.test.MockHandlerTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Answer;
import org.mockito.verification.VerificationMode;

import org.mockito.internal.Invocation;
import org.mockito.internal.InvocationMatcher;
import org.mockito.internal.MatchersBinder;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockitoInvocationHandler;
import org.mockito.internal.Progress;
import org.mockito.internal.Progress.MockingProgress;
import org.mockito.internal.Stubbing;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.Stub;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.mockito.Mockito.verifyZeroInteractions;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private MockSettingsImpl mockSettings;
 private Object mock;
 private Object realObject;
 private ArgumentMatcher argMatcher;
 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private VerificationMode verificationMode;
 private List<Answer> answers;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<Object>(mockSettings);
 mock = mock(Object.class);
 realObject = new Object() {};
 argMatcher = argThat(isA(String.class));
 invocation = new Invocation(realObject, "testMethod", new Object[]{"testString"}, new Class[]{String.class});
 invocationMatcher = mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation);
 verificationMode = VerificationModeFactory.verifyNoMoreInteractions();
 answers = Arrays.asList(new AnswerImpl() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer";
 }
 }, new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 throw new RuntimeException("Exception");
 }
 });
 }

 @Test
 public void testHandle_noStubbingOrVerification() throws Throwable {
 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_stubbingVoidMethod() throws Throwable {
 mockHandler.voidMethodStubbable(mock).stubVoid();
 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_stubbingWithAnswer() throws Throwable {
 mockHandler.voidMethodStubbable(mock).thenAnswer(new AnswerImpl() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer";
 }
 });
 Object result = mockHandler.handle(invocation);
 assertEquals("Answer", result);
 }

 @Test
 public void testHandle_stubbingWithAnswerWithException() throws Throwable {
 mockHandler.voidMethodStubbable(mock).thenAnswer(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 throw new RuntimeException("Exception");
 }
 });
 Object result = mockHandler.handle(invocation);
 throw new AssertionError("Expected Exception but got: " + result);
 }

 @Test
 public void testHandle_verification() throws Throwable {
 mockHandler.mockingProgress.addVerificationMode(verificationMode);
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 verify(mock).testMethod(argMatcher.match(invocation.getArguments()[0]));
 }

 @Test
 public void testHandle_verificationWithArgumentMatcher() throws Throwable {
 mockHandler.mockingProgress.addVerificationMode(verificationMode);
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 verify(mock).testMethod(argMatcher);
 }

 @Test
 public void testHandle_verificationWithArgumentMatcherAndOrdering() throws Throwable {
 mockHandler.mockingProgress.addVerificationMode(verificationMode);
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 verify(mock).testMethod(argMatcher.match(invocation.getArguments()[0]))
 .inOrder();
 }

 @Test
 public void testHandle_verificationWithDifferentMocks() throws Throwable {
 Object otherMock = mock(Object.class);
 mockHandler.mockingProgress.addVerificationMode(verificationMode);
 mockHandler.handle(new Invocation(mock, "testMethod", new Object[]{"testString"}, new Class[]{String.class}));
 mockHandler.handle(new Invocation(otherMock, "testMethod", new Object[]{"testString"}, new Class[]{String.class}));
 verifyZeroInteractions(otherMock);
 verify(mock).testMethod(argMatcher.match(invocation.getArguments()[0]));
 }

 @Test
 public void testHandle_answersList() throws Throwable {
 mockHandler.voidMethodStubbable(mock).thenAnswer(answers.get(0));
 mockHandler.voidMethodStubbable(mock).thenAnswer(answers.get(1));
 Object result = mockHandler.handle(invocation);
 assertEquals("Answer", result);
 mockHandler.handle(invocation);
 throw new AssertionError("Expected Exception but got: " + result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_tooManyAnswers() throws Throwable {
 mockHandler.voidMethodStubbable(mock).thenAnswer(answers.subList(0, 3).toArray(new Answer[3]));
 Object result = mockHandler.handle(invocation);
 throw new AssertionError("Expected IllegalStateException but got: " + result);
 }
}