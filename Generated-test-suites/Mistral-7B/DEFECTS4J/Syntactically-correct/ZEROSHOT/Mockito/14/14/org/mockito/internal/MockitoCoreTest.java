package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerWithInfo;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.mockito.internal.verification.api.VerificationDataImpl.VerificationDataBuilder.withAllActualArgs;
import static org.mockito.internal.verification.api.VerificationDataImpl.VerificationDataBuilder.withMock;
import static org.mockito.internal.verification.api.VerificationDataImpl.VerificationDataBuilder.withType;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mockObject;
 @Mock private Object mockObject2;
 @Mock private Object mockObject3;
 private Stubber stubber;
 private InOrder inOrder;
 private List<Object> mocks;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mocks = Arrays.asList(mockObject, mockObject2, mockObject3);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, null);
 assertThat(mock).isInstanceOf(Object.class);
 }

 @Test
 public void testStubbing() {
 stubber = mockitoCore.stub();
 when(mockObject.someMethod()).thenReturn("result");
 verify(mockObject).someMethod();
 assertEquals("result", mockObject.someMethod());
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 stubber = mockitoCore.stub(mockObject);
 when(mockObject.someMethod()).thenReturn("result");
 verify(mockObject).someMethod();
 assertEquals("result", mockObject.someMethod());
 }

 @Test
 public void testOngoingStubbing() {
 stubber = mockitoCore.when(mockObject.someMethod());
 when(mockObject.someMethod()).thenReturn("result");
 verify(mockObject).someMethod();
 assertEquals("result", mockObject.someMethod());
 }

 @Test
 public void testVerify() {
 verify(mockObject).someMethod();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 verify(new Object());
 }

 @Test
 public void testReset() {
 verify(mockObject).someMethod();
 mockitoCore.reset(mockObject);
 verifyZeroInteractions(mockObject);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 verify(mockObject).someMethod();
 verifyNoMoreInteractions(mockObject);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mockObject.someMethod(argCaptor.capture())).thenReturn(argCaptor.getValue());
 when(mockObject2.someMethod()).thenReturn(42);
 when(mockObject3.someMethod()).thenReturn("hello");

 inOrder(mocks).verify(mockObject).someMethod(ArgumentCaptor.any());
 inOrder(mocks).verify(mockObject2).someMethod();
 inOrder(mocks).verify(mockObject3).someMethod();

 inOrder(mocks).verifyInOrder(Arrays.asList(Arrays.asList(mockObject, mockObject2), Arrays.asList(mockObject, mockObject3))).verify(mockObject).someMethod(1);
 inOrder(mocks).verify(mockObject2).someMethod();
 inOrder(mocks).verify(mockObject3).someMethod();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithInOrderContext() {
 ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mockObject.someMethod(argCaptor.capture())).thenReturn(argCaptor.getValue());
 when(mockObject2.someMethod()).thenReturn(42);
 when(mockObject3.someMethod()).thenReturn("hello");

 InOrder inOrder = inOrder(mocks);
 inOrder.verify(mockObject).someMethod(ArgumentCaptor.any());
 inOrder.verify(mockObject2).someMethod();
 inOrder.verify(mockObject3).someMethod();

 inOrder.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyList() {
 List<Object> emptyList = Arrays.asList();
 mockitoCore.verifyNoMoreInteractionsInOrder(emptyList, inOrder());
 }

 @Test
 public void testVerificationModeFactory() {
 VerifyModeFactory verificationModeFactory = mockitoCore.getVerificationModeFactory();
 VerificationMode verificationMode = verificationModeFactory.atLeastOnce();
 assertThat(verificationMode).isEqualTo(VerificationMode.times(1));
 }

 @Test
 public void testVerifyAnswer() {
 final int result = 42;
 Answer answer = new AnswerWithInfo<Integer>() {
 @Override public Integer answer(InvocationOnMock invocation) throws Throwable { return result; }
 @Override public AnswerWithInfo<Integer> answerWithInfo(InvocationOnMock invocation) throws Throwable { return this; }
 };
 stubber = mockitoCore.doAnswer(answer);
 when(mockObject.someMethod()).thenAnswer(answer);
 verify(mockObject).someMethod();
 assertEquals(result, mockObject.someMethod());
 }

 @Test
 public void testVoidMethodStubbing() {
 Stubber voidStubber = mockitoCore.stubVoid(mockObject);
 mockitoCore.when(mockObject.someMethod()).then(voidStubber.thenCallRealMethod());
 mockObject.someMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = when(mockObject.someMethod()).thenReturn("result").thenCallRealMethod();
 Object lastInvocation = mockitoCore.getLastInvocation();
 assertEquals(invocation, lastInvocation);
 }
}