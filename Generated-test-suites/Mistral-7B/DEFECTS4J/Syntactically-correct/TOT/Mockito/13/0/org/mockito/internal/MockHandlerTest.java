package org.mockito.internal.tests;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.mockito.Answer;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.MockingProgress;
import org.mockito.internal.invocation.ThreadSafeMockingProgress;
import org.mockito.internal.progress.MockingProgressListener;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.AnswerWithThrowable;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.StubbingInfo;
import org.mockito.verification.VerificationModeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private InvocationOnMock invocation;
 private Invocation invocationToStub;
 private StubbingInfo stubbingInfo;
 private List<Answer> answers;
 private ArgumentMatcher argumentMatcher;
 private VerificationMode verificationMode;
 private VerificationModeFactory verificationModeFactory;
 private MockingProgress mockingProgress;
 private ThreadSafeMockingProgress threadSafeMockingProgress;
 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mock = mock(Object.class);
 mockHandler = new MockHandler<Object>(new MockSettingsImpl());
 invocation = mock(InvocationOnMock.class);
 invocationToStub = mock(Invocation.class);
 stubbingInfo = mock(StubbingInfo.class);
 answers = Arrays.asList(new AnswerImpl(), new AnswerWithThrowable(new RuntimeException()));
 argumentMatcher = ArgumentMatchers.anyInt();
 mockingProgress = mock(MockingProgress.class);
 threadSafeMockingProgress = new ThreadSafeMockingProgress();
 mockingProgress = threadSafeMockingProgress;
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testHandle_whenInvocationIsStubbed_thenNullIsReturned() {
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(mockingProgress.pullVerificationMode()).thenReturn(null);
 when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(argumentMatcher);
 when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

 when(mockHandler.handle(invocationToStub)).thenReturn(null);

 mockStatic(Mockito.class);
 reset(mockingProgress);

 mockHandler.handle(invocation);

 verify(mockHandler).handle(invocationToStub);
 }

 @Test
 public void testHandle_whenVerificationModeIsPulled_thenVerifyIsCalledWithCorrectArguments() {
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode = mock(VerificationMode.class));
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation());
 when(stubbedInvocation.answer(invocation)).thenReturn(null);

 mockStatic(Mockito.class);
 reset(mockingProgress);

 mockHandler.handle(invocation);

 verify(verificationMode).verify(eq(invocationContainerImpl), argumentMatcher);
 }

 @Test
 public void testHandle_whenVerificationModeIsNotPulled_thenNullIsReturned() {
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerImpl());

 mockStatic(Mockito.class);
 reset(mockingProgress);

 mockHandler.handle(invocation);

 verifyZeroInteractions(mockingProgress);
 }

 @Test
 public void testHandle_whenInvocationIsNotStubbedOrVerified_thenDefaultAnswerIsReturned() {
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerImpl());

 mockStatic(Mockito.class);
 reset(mockingProgress);

 Object result = mockHandler.handle(invocation);

 verifyZeroInteractions(mockingProgress);
 assertSame(result, new AnswerImpl().answer(invocation));
 }

 @Test
 public void testVoidMethodStubbable_whenVoidMethodIsStubbed_thenStubIsCreated() {
 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 verify(mockingProgress).registerMock(mock);
 }

 @Test
 public void testGetInvocationContainer_whenCalled_thenInvocationContainerIsReturned() {
 InvocationContainer invocationContainer = mock(InvocationContainer.class);
 mockHandler = new MockHandler<Object>(mockHandler) {
 private InvocationContainer invocationContainer;

 @Override
 public InvocationContainer getInvocationContainer() {
 return invocationContainer = this.invocationContainer;
 }
 };

 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);

 InvocationContainer returnedInvocationContainer = mockHandler.getInvocationContainer();

 assertSame(invocationContainer, returnedInvocationContainer);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_whenInvocationContainerHasNoAnswerOrVerificationMode_thenIllegalStateExceptionIsThrown() {
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(mockingProgress.pullVerificationMode()).thenReturn(null);
 when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

 mockStatic(Mockito.class);
 reset(mockingProgress);

 try {
 mockHandler.handle(invocation);
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_whenInvocationIsStubbedWithDifferentInvocationMatcher_thenIllegalStateExceptionIsThrown() {
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(mockingProgress.pullVerificationMode()).thenReturn(null);
 when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
 when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(argumentMatcher1 = mock(ArgumentMatcher.class));
 when(invocationContainerImpl.setMethodForStubbing(argumentMatcher1)).thenReturn(true);
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(argumentMatcher2 = mock(ArgumentMatcher.class));

 mockStatic(Mockito.class);
 reset(mockingProgress);

 mockHandler.handle(invocation);

 verify(mockHandler).handle(invocation);
 try {
 mockHandler.handle(invocation);
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testSetAnswersForStubbing_whenListOfAnswersIsSet_thenAnswersAreStored() {
 mockHandler = new MockHandler<Object>(mockHandler) {
 private List<Answer> answers;

 @Override
 public void setAnswersForStubbing(List<Answer> answers) {
 this.answers = answers;
 }
 };

 mockHandler.setAnswersForStubbing(answers);

 List<Answer> storedAnswers = mockHandler.invocationContainerImpl.getAnswersForStubbing();

 assertSame(answers, storedAnswers);
 }

 @Test
 public void testGetMockSettings_whenCalled_thenMockSettingsAreReturned() {
 MockSettingsImpl mockSettings = mockHandler.getMockSettings();

 assertSame(mockSettings, mockHandler.mockSettings);
 }

 @Test
 public void testHandle_whenInvocationIsStubbedWithAnswer_thenAnswerIsReturned() {
 StubbingInfo stubbingInfo = mock(StubbingInfo.class);
 when(mockHandler.getMockingProgress()).thenReturn(mockingProgress);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mockingProgress);
 when(mockingProgress.pullVerificationMode()).thenReturn(null);
 when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
 when(invocationContainerImpl.setMethodForStubbing(eq(argumentMatcher))).thenReturn(true);
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(argumentMatcher);
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerImpl());
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerImpl());
 when(answers.get(0)).thenReturn(answer = new AnswerImpl());
 when(answers.get(0).answer(invocation)).thenReturn("answer");
 when(stubbingInfo.getAnswer()).thenReturn(answer);
 when(stubbingInfo.getAnswer()).thenReturn(answer);
 when(invocationContainerImpl.setAnswerFor(invocation, argumentMatcher, stubbingInfo)).thenReturn(true);

 mockStatic(Mockito.class);
 reset(mockingProgress);

 mockHandler.handle(invocation);

 assertEquals("answer", mockHandler.handle(invocation));
 }
}