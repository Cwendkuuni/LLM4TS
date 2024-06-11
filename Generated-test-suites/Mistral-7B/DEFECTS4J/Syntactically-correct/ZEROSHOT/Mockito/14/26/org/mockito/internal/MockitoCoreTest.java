package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalVerificationMode;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.<Object>mock(Object.class);
 verify(mockitoCore).mock(ArgumentMatchers.eq(Object.class), ArgumentMatchers.any(MockSettings.class));
 }

 @Test
 public void testStubbing() {
 Object result = mockitoCore.<Object>stub(mock1).returnValue(new Object());
 verify(mockitoCore).stubbingStarted();
 verify(mock1).returnValue(new Object());
 }

 @Test
 public void testDeprecatedStubbing() {
 Object result = mockitoCore.<Object>stub(mock1);
 verify(mockitoCore).stubbingStarted();
 verify(mockitoCore).stub(mock1);
 }

 @Test
 public void testWhen() {
 Object result = mockitoCore.<Object>when(mock1).thenReturn(new Object());
 verify(mockitoCore).stubbingStarted();
 verify(mock1).thenReturn(new Object());
 }

 @Test
 public void testVerify() {
 verify(mock1, ArgumentMatchers.eq(VerificationMode.times(1)));
 }

 @Test
 public void testVerifyNull() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 verify(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsMultipleMocks() {
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 verifyOrder(inOrder);
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock1, mock2);
 mockitoCore.reset();
 Mockito.reset(mock1, mock2);
 }

 @Test
 public void testVerifyOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);
 when(mock1.doSomething()).thenCallRealMethod();
 when(mock2.doSomething()).thenCallRealMethod();
 InvocationOnMock invocation1 = mockitoCore.getLastInvocation();
 mockitoCore.verify(mock1).doSomething();
 mockitoCore.verify(mock2).doSomething();
 verifyOrder(inOrder);
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 };
 Object result = mockitoCore.doAnswer(answer).when(mock).doSomething();
 verify(mock).doSomething();
 verify(mock).doSomething(); // Verify that the answer was called twice
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mock(Object.class);
 Object result = mockitoCore.stubVoid(mock).doSomething();
 verify(mock).doSomething();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}