package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.verification.api.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.answers;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mockObject;
 private Object mockAnotherObject;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 mockObject = mockitoCore.mock(String.class);
 assert(mockObject instanceof String);
 }

 @Test
 public void testMockCreationWithSettings() {
 mockObject = mockitoCore.mock(String.class, new MockSettingsImpl());
 assert(mockObject instanceof String);
 }

 @Test
 public void testStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 when(mockObject).thenReturn("mockValue");
 assert(mockitoCore.getLastInvocation().getMethod().getName()).equals("thenReturn");
 }

 @Test
 public void testStubbingWithAnswer() {
 doAnswer(invocation -> "answerValue").when(mockObject).someMethod();
 verify(mockObject).someMethod();
 assert(mockObject.someMethod()).equals("answerValue");
 }

 @Test
 public void testVerification() {
 mockObject = mock(String.class);
 verify(mockObject).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNullMock() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNotAMock() {
 Object notAMock = new Object();
 verify(notAMock).toString();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockObject = mock(String.class);
 verifyNoMoreInteractions(mockObject);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 verifyNoMoreInteractions();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 mockObject = mock(Object.class);
 mockAnotherObject = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mockObject, mockAnotherObject);
 mockitoCore.when(mockObject.someMethod()).thenReturn("returnValue1");
 mockitoCore.when(mockAnotherObject.someMethod()).thenReturn("returnValue2");
 inOrder.verify(mockObject).someMethod();
 inOrder.verify(mockAnotherObject).someMethod();
 }

 @Test
 public void testReset() {
 mockObject = mock(String.class);
 when(mockObject).thenReturn("mockValue");
 verify(mockObject).toString();
 reset(mockObject);
 assert(mockitoCore.getLastInvocation() == null);
 }

 @Test
 public void testValidation() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = AssertionError.class)
 public void testValidationNullState() {
 mockitoCore.mockingProgress.setMockingState(null);
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = AssertionError.class)
 public void testValidationStubbingInProgress() {
 mockitoCore.stubbingStarted();
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 mockObject = mock(String.class);
 when(mockObject.toString()).thenReturn("mockValue");
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.then(answers.returnValues(Arrays.asList("answer1", "answer2")));
 Invocation invocation = mockitoCore.getLastInvocation();
 assert(invocation.getTarget().equals(mockObject));
 assert(invocation.getMethod().getName().equals("then"));
 }
}