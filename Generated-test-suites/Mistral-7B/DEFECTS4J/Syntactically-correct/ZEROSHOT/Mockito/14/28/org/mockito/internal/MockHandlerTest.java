package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Answer;
import org.mockito.stubbing.AnswerCalledWith;
import org.mockito.stubbing.AnswerReturns;
import org.mockito.stubbing.AnswerThrows;
import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.MockingProgress;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerWith;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.stubbing.VoidMethodStubbableImpl;

import java.util.List;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private Invocation invocation;
 private InvocationContainer invocationContainer;
 private StubbedInvocationMatcher stubbedInvocation;
 private List<Answer> answers;

 @Before
 public void setup() {
 mock = mock(Object.class);
 mockHandler = new MockHandler<>();
 invocationContainer = mockHandler.getInvocationContainer();
 answers = Arrays.asList(
 new AnswerReturns(42),
 new AnswerThrows(new RuntimeException()),
 new AnswerCalledWith(anyInt()),
 new AnswerWith(new Answer<String>() {
 @Override
 public String answer(Invocation invocation) throws Throwable {
 return "answer";
 }
 })
 );
 }

 @Test
 public void testHandle_NoAnswersOrVerificationMode_ReturnsDefaultAnswer() {
 when(mockHandler.handle(invocation))
 .thenReturn("default answer");

 assertEquals("default answer", mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_AnswersForStubbing_ReturnsAnswer() {
 when(mockHandler.handle(invocation))
 .thenReturn(mockHandler.voidMethodStubbable(mock));

 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 when(voidMethodStubbable.answer())
 .thenAnswer(new AnswerWith(new Answer<Object>() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "answer from stubbing";
 }
 }));

 assertEquals("answer from stubbing", mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_VerificationMode_VerifiesInvocation() {
 ArgumentMatcher argumentMatcher = eq("expected argument");
 when(mockHandler.handle(invocation))
 .thenReturn(null);

 ArgumentMatcher invocationMatcher = mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), invocation);
 verify(invocationContainer).setMethodForStubbing(invocationMatcher);
 verify(mockingProgress).pullVerificationMode();
 verify(mockingProgress).validateState();

 verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
 verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);

 ArgumentMatcher capturedArgumentMatcher = stubbedInvocation.getArgumentMatcher();
 assertEquals(argumentMatcher, capturedArgumentMatcher);

 ArgumentMatcher[] expectedArgumentsMatchers = new ArgumentMatcher[1];
 expectedArgumentsMatchers[0] = argumentMatcher;
 Object[] expectedArguments = new Object[1];
 expectedArguments[0] = "expected argument";

 StubbedInvocationMatcher expectedStubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, expectedArgumentsMatchers, Arrays.asList(invocation), null, null);
 verify(invocationContainer).setAnswerFor(invocationMatcher, expectedStubbedInvocation);

 Answer answer = answers.get(0);
 verify(answer).answer(invocation);
 }

 @Test
 public void testHandle_MultipleAnswersForStubbing_ReturnsAnswerWithHighestPriority() {
 when(mockHandler.handle(invocation))
 .thenReturn(mockHandler.voidMethodStubbable(mock));

 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 when(voidMethodStubbable.answer())
 .thenAnswer(new AnswerWith(new Answer<Object>() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "answer 1";
 }
 }));

 when(invocationContainer.findAnswerFor(invocation))
 .thenReturn(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), invocation));

 when(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcher.class), invocation))
 .thenReturn(new StubbedInvocationMatcher(null, null, null, null, answers.subList(1, answers.size())));

 Answer answer = answers.get(0);
 assertEquals("answer 1", mockHandler.handle(invocation));
 assertEquals("answer 2", mockHandler.handle(invocation).answer(invocation));
 }

 @Test
 public void testVoidMethodStubbable_ReturnsStubbableInstance() {
 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 assertNotNull(voidMethodStubbable);
 }

 @Test
 public void testSetAnswersForStubbing_SetsAnswers() {
 List<Answer> expectedAnswers = Arrays.asList(new AnswerReturns(42), new AnswerThrows(new RuntimeException()));
 mockHandler.setAnswersForStubbing(expectedAnswers);
 assertEquals(expectedAnswers, invocationContainer.getAnswersForStubbing());
 }

 @Test
 public void testGetMockSettings_ReturnsMockSettings() {
 MockSettingsImpl mockSettings = mockHandler.getMockSettings();
 assertNotNull(mockSettings);
 }
}