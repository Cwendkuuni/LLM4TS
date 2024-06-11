package org.mockito.internal.test;

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

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockHandler<MockitoInvocationHandler> mockHandlerSpy;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private Invocation invocation;
 private List<Answer> answers;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 invocation = Mockito.mock(Invocation.class);
 answers = new ArrayList<>();
 }

 @Test
 public void testHandle_stubbingVoid() {
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class))).thenAnswer(invocation -> {
 // stubbing voids with stubVoid() or doAnswer() style
 doAnswer(answers.get(0)).when(mockHandler).handle(ArgumentMatchers.any(Invocation.class));
 return null;
 });

 invocationContainer.setAnswersForStubbing(answers);
 Answer answer = mock(Answer.class);
 answers.add(answer);

 mockHandler.handle(invocation);

 verify(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test
 public void testHandle_verification() {
 // We need to check if verification was started on the correct mock
 // - see VerifyingWithAnExtraCallToADifferentMockTest
 VerificationMode verificationMode = Mockito.verify(mockHandler, Mockito.times(1)).handle(ArgumentMatchers.any(Invocation.class));

 // We need to check if verification was started on the correct mock
 // - see VerifyingWithAnExtraCallToADifferentMockTest
 verify(mockHandler).handle(ArgumentMatchers.eq(invocation));
 }

 @Test
 public void testHandle_verification_noInteractions() {
 mockHandler.handle(invocation);
 verifyNoInteractions(mockHandler);
 }

 @Test
 public void testHandle_verification_zeroInteractions() {
 mockHandler.handle(invocation);
 verifyZeroInteractions(mockHandler);
 }

 @Test
 public void testHandle_verification_multipleInteractions() {
 Answer answer1 = mock(Answer.class);
 Answer answer2 = mock(Answer.class);
 answers.add(answer1);
 answers.add(answer2);

 doAnswer(answer1).when(mockHandler).handle(ArgumentMatchers.any(Invocation.class));
 doAnswer(answer2).when(mockHandler).handle(ArgumentMatchers.any(Invocation.class));

 mockHandler.handle(invocation);

 verify(answer1).answer(ArgumentMatchers.any(InvocationOnMock.class));
 verify(answer2).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test
 public void testHandle_verification_noMoreInteractions() {
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class))).thenAnswer(invocation -> {
 // We need to check if verification was started on the correct mock
 // - see VerifyingWithAnExtraCallToADifferentMockTest
 verifyNoMoreInteractions(mockHandler);
 return null;
 });

 mockHandler.handle(invocation);
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_verification_error() {
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class))).thenAnswer(invocation -> {
 // We need to check if verification was started on the correct mock
 // - see VerifyingWithAnExtraCallToADifferentMockTest
 throw new RuntimeException();
 });

 try {
 mockHandler.handle(invocation);
 } catch (RuntimeException e) {
 // expected
 }

 verify(mockHandler).handle(ArgumentMatchers.eq(invocation));
 }

 @Test
 public void testHandle_verification_noInteractions_spy() {
 mockHandlerSpy = spy(mockHandler);

 mockHandlerSpy.handle(invocation);
 verifyNoInteractions(mockHandlerSpy);
 }

 @Test
 public void testHandle_verification_zeroInteractions_spy() {
 mockHandlerSpy = spy(mockHandler);

 mockHandlerSpy.handle(invocation);
 verifyZeroInteractions(mockHandlerSpy);
 }
}