package org.mockito.internal.test. MockHandlerTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.mockito.Answer;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BetaStub;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MockHandler.class})
public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private Invocation invocation;
 private ArgumentMatcher argumentMatcher;
 private StubbedInvocationMatcher stubbedInvocation;
 private VerificationMode verificationMode;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 invocation = PowerMockito.mock(Invocation.class);
 argumentMatcher = ArgumentMatchers.anyInt();
 }

 @Test
 public void testHandle_NoAnswersForStubbing_ReturnDefaultAnswer() throws Throwable {
 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));
 Object result = mockHandler.handle(invocation);

 // Default answer is Answer.RETURN_VALUE_ANY
 verify(invocation.getTarget()).voidMethod();
 assertNull(result);
 }

 @Test
 public void testHandle_HasAnswersForStubbing_ReturnAnswer() throws Throwable {
 List<Answer> answers = Arrays.asList(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer from stubbing";
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));
 Object result = mockHandler.handle(invocation);

 // Stubbing answer is "Answer from stubbing"
 verifyZeroInteractions(invocation.getTarget());
 assertEquals("Answer from stubbing", result);
 }

 @Test
 public void testHandle_HasVerificationMode_VerifyInvocation() throws Throwable {
 verificationMode = VerificationModeFactory.withDefaultVerify();
 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));
 mockingProgress.setVerificationMode(verificationMode);

 mockHandler.handle(invocation);

 // Verify invocation on the target object
 verify(invocation.getTarget()).voidMethod();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_HasAnswersForStubbing_VerificationModeNotMatched() throws Throwable {
 List<Answer> answers = Arrays.asList(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer from stubbing";
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));
 mockingProgress.pushVerificationMode(VerificationMode.never());

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_HasVerificationMode_VerifyWithMatcher() throws Throwable {
 verificationMode = VerificationModeFactory.withMatchedInvocations(argumentMatcher);
 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));
 mockingProgress.setVerificationMode(verificationMode);

 mockHandler.handle(invocation);

 // Verify invocation on the target object with the given argument matcher
 verify(invocation.getTarget()).voidMethod(argumentMatcher.matches(1));
 }

 @Test
 public void testHandle_OngoingStubbing() throws Throwable {
 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));

 OngoingStubbingImpl<Object> ongoingStubbing = mockHandler.handle(invocation);

 // Verify ongoing stubbing was reported
 verify(invocationContainer).registerOngoingStubbing(ongoingStubbing);
 }

 @Test
 public void testHandle_NoVerificationMode_VerifyZeroInteractions() throws Throwable {
 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));

 mockHandler.handle(invocation);

 // Verify zero interactions on the target object
 verifyZeroInteractions(invocation.getTarget());
 }

 @Test
 public void testHandle_HasAnswersForStubbing_VerifyNoInteractions() throws Throwable {
 List<Answer> answers = Arrays.asList(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return null;
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));

 mockHandler.handle(invocation);

 // Verify no interactions on the target object
 verifyZeroInteractions(invocation.getTarget());
 }

 @Test
 public void testHandle_HasStubbedInvocation_ReturnAnswer() throws Throwable {
 stubbedInvocation = new StubbedInvocationMatcher(ArgumentMatchers.any()) {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer from stubbed invocation";
 }
 };
 invocationContainer.setInvocationForPotentialStubbing(stubbedInvocation);

 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));

 Object result = mockHandler.handle(invocation);

 // Stubbed invocation answer is "Answer from stubbed invocation"
 verifyZeroInteractions(invocation.getTarget());
 assertEquals("Answer from stubbed invocation", result);
 }

 @Test
 public void testHandle_HasStubbedInvocation_VerifyZeroInteractions() throws Throwable {
 stubbedInvocation = new StubbedInvocationMatcher(ArgumentMatchers.any()) {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return null;
 }
 };
 invocationContainer.setInvocationForPotentialStubbing(stubbedInvocation);

 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));

 mockHandler.handle(invocation);

 // Verify zero interactions on the target object
 verifyZeroInteractions(invocation.getTarget());
 }

 @Test
 public void testHandle_HasStubbedInvocation_VerifyInvocationWithMatcher() throws Throwable {
 stubbedInvocation = new StubbedInvocationMatcher(argumentMatcher) {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer from stubbed invocation";
 }
 };
 invocationContainer.setInvocationForPotentialStubbing(stubbedInvocation);

 when(invocation.getTarget()).thenReturn(new Object());
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("voidMethod"));

 mockHandler.handle(invocation);

 // Verify invocation on the target object with the given argument matcher
 verify(invocation.getTarget()).voidMethod(argumentMatcher.matches(1));
 }
}