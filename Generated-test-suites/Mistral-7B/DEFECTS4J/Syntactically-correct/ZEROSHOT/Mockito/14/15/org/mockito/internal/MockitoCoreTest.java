package org.mockito.internal. MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InvocationOnMockException;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mockObject;
 @Mock private Object mockObject2;
 @Mock private MockSettings mockSettings;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 verify(mockitoCore).mockingStarted(mock, Object.class, mockSettings);
 }

 @Test
 public void testStubbingStarted() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 verify(mockitoCore).stubbingStarted();
 }

 @Test
 public void testStubbingWhen() {
 Object mock = mock(Object.class);
 when(mock).thenReturn("test");
 String result = (String) mockitoCore.verify(mock, VerificationMode.RETURNED(1));
 verify(mock).someMethod();
 verify(mockitoCore).verificationStarted(VerificationMode.RETURNED(1));
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyOnNullMock() {
 mockitoCore.verify(null, VerificationMode.RETURNED(1));
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyOnNonMock() {
 Object nonMock = new Object();
 mockitoCore.verify(nonMock, VerificationMode.RETURNED(1));
 }

 @Test
 public void testReset() {
 Object mock = mock(Object.class);
 mockitoCore.verify(mock, VerificationMode.RETURNED(1));
 mockitoCore.reset(mock);
 verifyNoInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 mockitoCore.verify(mock, VerificationMode.RETURNED(1));
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNoMoreInteractionsOnNonMock() {
 Object nonMock = new Object();
 mockitoCore.verifyNoMoreInteractions(nonMock);
 }

 @Test
 public void testVerifyInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNullMock() {
 InOrder inOrder = mockitoCore.inOrder(null);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockObject), inOrder);
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNonMock() {
 Object nonMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(nonMock);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockObject), inOrder);
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer answer = mock(Answer.class);
 when(mock).thenAnswer(answer);
 Object result = mockitoCore.verify(mock, VerificationMode.RETURNED(1));
 verify(answer).answer(any(InvocationOnMock.class));
 }

 @Test
 public void testStubVoid() {
 Object mock = mock(Object.class);
 mockitoCore.stubVoid(mock).someMethod();
 verify(mockitoCore).mockingProgress().voidMethodStubbable(mock);
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mock(Object.class);
 when(mock.someMethod()).thenReturn("test");
 Object result = mockitoCore.verify(mock, VerificationMode.RETURNED(1)).someMethod();
 verify(mockitoCore).getLastInvocation().getMethod().getName().equals("someMethod");
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}