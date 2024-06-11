package org.mockito.internal.test. MockHandlerTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito. ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private List<Answer> answers;

 @Before
 public void setUp() {
 mock = mock(Object.class);
 mockHandler = new MockHandler<>();
 answers = new ArrayList<>();
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 Invocation invocation = new Invocation(mock, "voidMethod");
 mockingProgress.pushVerificationMode(null);
 mockingProgress.pushArgumentMatcherStorage(new Object[0]);

 // stubbing void method
 mockHandler.voidMethodStubbable(mock).stubVoid();

 // invoke void method
 mockHandler.handle(invocation);

 // verify no answer was returned
 verify(mockHandler).handle(invocation);
 verify(mockHandler).handle(invocation).equals(null);

 // verify verification mode and argument matcher were not affected
 verify(mockingProgress, never()).pushVerificationMode(any(VerificationMode.class));
 verify(mockingProgress, never()).pushArgumentMatcherStorage(any(Object[].class));
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithAnswer() {
 Invocation invocation = new Invocation(mock, "nonVoidMethod", new Object[]{"arg1", "arg2"});
 Answer answer = mock(Answer.class);
 mockingProgress.pushVerificationMode(null);
 mockingProgress.pushArgumentMatcherStorage(new Object[]{argThat(isA(String.class)), argThat(isA(Integer.class))});
 mockingProgress.reportAnswer(answer);

 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer);

 // invoke non-void method
 Object result = mockHandler.handle(invocation);

 // verify answer was returned
 verify(answer).answer(invocation);
 verify(answer).answer(invocation).equals(result);

 // verify verification mode and argument matcher were affected
 ArgumentCaptor<VerificationMode> verificationModeCaptor = ArgumentCaptor.forClass(VerificationMode.class);
 ArgumentCaptor<Object[]> argumentMatcherCaptor = ArgumentCaptor.forClass(Object[].class);
 verify(mockingProgress).pushVerificationMode(verificationModeCaptor.capture());
 verify(mockingProgress).pushArgumentMatcherStorage(argumentMatcherCaptor.capture());

 // verify argument matcher was set correctly
 assertEquals("arg1", argumentMatcherCaptor.getValue()[0]);
 assertEquals("arg2", argumentMatcherCaptor.getValue()[1]);

 // verify answer was reported
 assertEquals(answer, verificationModeCaptor.getValue().getAnswer());
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithNoAnswer() {
 Invocation invocation = new Invocation(mock, "nonVoidMethod", new Object[]{"arg1", "arg2"});

 mockingProgress.pushVerificationMode(null);
 mockingProgress.pushArgumentMatcherStorage(new Object[]{argThat(isA(String.class)), argThat(isA(Integer.class))});

 // invoke non-void method
 Object result = mockHandler.handle(invocation);

 // verify default answer was returned
 verify(mockSettings.getDefaultAnswer()).answer(invocation);
 verify(mockSettings.getDefaultAnswer()).answer(invocation).equals(result);

 // verify verification mode and argument matcher were not affected
 verify(mockingProgress, never()).pushVerificationMode(any(VerificationMode.class));
 verify(mockingProgress, never()).pushArgumentMatcherStorage(any(Object[].class));
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithStubbedInvocation() {
 Invocation invocation = new Invocation(mock, "nonVoidMethod", new Object[]{"arg1", "arg2"});
 ArgumentCaptor<Invocation> invocationCaptor = ArgumentCaptor.forClass(Invocation.class);
 Answer answer = mock(Answer.class);
 Object returnValue = "return value";

 mockingProgress.pushVerificationMode(null);
 mockingProgress.pushArgumentMatcherStorage(new Object[]{argThat(isA(String.class)), argThat(isA(Integer.class))});
 mockingProgress.reportAnswer(answer);
 mockingProgress.reportStubbedInvocation(invocationCaptor.capture());

 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer);

 // stub non-void method
 when(mockHandler).handle(invocation).thenReturn(returnValue);

 // invoke non-void method
 Object result = mockHandler.handle(invocation);

 // verify stubbed invocation was used
 verify(answer).answer(invocationCaptor.getValue());
 verify(answer).answer(invocationCaptor.getValue()).equals(result);

 // verify return value was set correctly
 assertEquals(returnValue, result);

 // verify verification mode and argument matcher were affected
 ArgumentCaptor<VerificationMode> verificationModeCaptor = ArgumentCaptor.forClass(VerificationMode.class);
 ArgumentCaptor<Object[]> argumentMatcherCaptor = ArgumentCaptor.forClass(Object[].class);
 verify(mockingProgress).pushVerificationMode(verificationModeCaptor.capture());
 verify(mockingProgress).pushArgumentMatcherStorage(argumentMatcherCaptor.capture());

 // verify argument matcher was set correctly
 assertEquals("arg1", argumentMatcherCaptor.getValue()[0]);
 assertEquals("arg2", argumentMatcherCaptor.getValue()[1]);

 // verify answer was reported
 assertEquals(answer, verificationModeCaptor.getValue().getAnswer());
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithVerificationMode() {
 Invocation invocation = new Invocation(mock, "nonVoidMethod", new Object[]{"arg1", "arg2"});
 ArgumentCaptor<Invocation> invocationCaptor = ArgumentCaptor.forClass(Invocation.class);
 VerificationMode verificationMode = mock(VerificationMode.class);

 mockingProgress.pushVerificationMode(verificationMode);
 mockingProgress.pushArgumentMatcherStorage(new Object[]{argThat(isA(String.class)), argThat(isA(Integer.class))});

 mockHandler.handle(invocation);

 // verify verification mode was used
 verify(verificationMode).verify(invocationCaptor.capture());
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithVerificationModeAndAnswer() {
 Invocation invocation = new Invocation(mock, "nonVoidMethod", new Object[]{"arg1", "arg2"});
 ArgumentCaptor<Invocation> invocationCaptor = ArgumentCaptor.forClass(Invocation.class);
 Answer answer = mock(Answer.class);
 Object returnValue = "return value";

 mockingProgress.pushVerificationMode(null);
 mockingProgress.pushArgumentMatcherStorage(new Object[]{argThat(isA(String.class)), argThat(isA(Integer.class))});
 mockingProgress.reportAnswer(answer);
 mockingProgress.reportStubbedInvocation(invocationCaptor.capture());

 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer);

 // stub non-void method with answer
 when(mockHandler).handle(invocation).thenAnswer(answer);

 // invoke non-void method
 Object result = mockHandler.handle(invocation);

 // verify answer was used
 verify(answer).answer(invocationCaptor.getValue());
 verify(answer).answer(invocationCaptor.getValue()).equals(result);

 // verify verification mode and argument matcher were not affected
 verify(mockingProgress, never()).pushVerificationMode(any(VerificationMode.class));
 verify(mockingProgress, never()).pushArgumentMatcherStorage(any(Object[].class));
 }
}