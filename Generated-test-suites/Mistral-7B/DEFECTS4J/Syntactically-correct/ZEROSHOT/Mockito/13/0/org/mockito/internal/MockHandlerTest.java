package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerString;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private InvocationOnMock invocation;
 private OngoingStubbing<MockitoInvocationHandler> ongoingStubbing;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 }

 @Test
 public void testHandle_noVerificationOrStubbing() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 invocation = Mockito.mock(InvocationOnMock.class);

 Object result = mockHandler.handle(invocation);

 assertNull(result);
 }

 @Test
 public void testHandle_verificationMode() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 VerificationMode verificationMode = Mockito.verify(mock);
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 mockHandler.handle(invocation);

 verify(mock).handle(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verificationMode_wrongMock() throws Throwable {
 MockitoInvocationHandler mock1 = mock(MockitoInvocationHandler.class);
 MockitoInvocationHandler mock2 = mock(MockitoInvocationHandler.class);
 VerificationMode verificationMode = Mockito.verify(mock1);
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock2;

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Answer<Integer> answer = AnswerString.with("Hello");
 List<Answer<?>> answers = new ArrayList<>();
 answers.add(answer);

 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 when(mockHandler.getMockSettings()).thenReturn(mockSettings);
 when(invocationContainer.findAnswerFor(ArgumentMatchers.eq(mock))).thenReturn(answer);
 when(mockSettings.getDefaultAnswer()).thenReturn(AnswerString.with("World"));

 when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
 when(invocationContainer.getInvocationForPotentialStubbing()).thenReturn(ArgumentMatchers.any());

 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 Object result = mockHandler.handle(invocation);

 assertEquals("Hello", result);
 }

 @Test
 public void testHandle_stubbing_reset() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Answer<Integer> answer = AnswerString.with("Hello");
 List<Answer<?>> answers = new ArrayList<>();
 answers.add(answer);

 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 when(mockHandler.getMockSettings()).thenReturn(mockSettings);
 when(invocationContainer.findAnswerFor(ArgumentMatchers.eq(mock))).thenReturn(answer);
 when(mockSettings.getDefaultAnswer()).thenReturn(AnswerString.with("World"));

 when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
 when(invocationContainer.getInvocationForPotentialStubbing()).thenReturn(ArgumentMatchers.any());

 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 Object result = mockHandler.handle(invocation);

 assertEquals("World", result);

 ongoingStubbing = mockHandler.getInvocationContainer().setAnswer(invocation, ArgumentMatchers.anyInt(), answer);
 result = mockHandler.handle(invocation);

 assertEquals("Hello", result);

 verify(ongoingStubbing).thenReturn(answer);
 }

 @Test
 public void testHandle_stubbing_voidMethod() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 VoidMethodStubbable<MockitoInvocationHandler> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 voidMethodStubbable.doReturn(null).when(mock).voidMethod();

 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 mockHandler.handle(invocation);

 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testHandle_stubbing_voidMethod_reset() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 VoidMethodStubbable<MockitoInvocationHandler> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 voidMethodStubbable.doNothing().when(mock).voidMethod();
 voidMethodStubbable.doReturn(null).when(mock).voidMethod();

 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 mockHandler.handle(invocation);

 verifyZeroInteractions(mock);
 }

 @Test
 public void testHandle_stubbing_answers() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 List<Answer<?>> answers = new ArrayList<>();
 answers.add(AnswerString.with("Hello"));
 answers.add(AnswerString.with("World"));

 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 when(mockHandler.getMockSettings()).thenReturn(mockSettings);
 when(invocationContainer.findAnswersForStubbing()).thenReturn(answers);
 when(mockSettings.getDefaultAnswer()).thenReturn(Answer.CALLS_REAL_METHOD);

 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 Object result = mockHandler.handle(invocation);

 assertEquals("World", result);

 ongoingStubbing = mockHandler.getInvocationContainer().setAnswer(invocation, ArgumentMatchers.anyInt(), answers.get(0));
 result = mockHandler.handle(invocation);

 assertEquals("Hello", result);
 }

 @Test
 public void testHandle_stubbing_answers_reset() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 List<Answer<?>> answers = new ArrayList<>();
 answers.add(AnswerString.with("Hello"));
 answers.add(AnswerString.with("World"));

 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 when(mockHandler.getMockSettings()).thenReturn(mockSettings);
 when(invocationContainer.findAnswersForStubbing()).thenReturn(answers);
 when(mockSettings.getDefaultAnswer()).thenReturn(Answer.CALLS_REAL_METHOD);

 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock() = mock;

 Object result = mockHandler.handle(invocation);

 assertEquals(null, result);

 ongoingStubbing = mockHandler.getInvocationContainer().setAnswersForStubbing(answers);
 result = mockHandler.handle(invocation);

 assertEquals("Hello", result);
 }
}