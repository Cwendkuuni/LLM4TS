package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.MockUtil;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.api.VerificationModeFactory;
import org.mockito.internal.invocation.Invocation;
import org.mockito.exceptions.Reporter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private ThreadSafeMockingProgress mockingProgress;
 private List<Invocation> invocations;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 reporter = new Reporter();
 mockUtil = Mockito.spy(new MockUtil());
 mockingProgress = Mockito.spy(new ThreadSafeMockingProgress());
 }

 @Test
 public void testMockCreation() {
 Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
 MockSettings mockSettings = new MockSettingsImpl();
 MockitoCore mockitoCore = MockitoCore.mock(mockitoCoreClass, mockSettings, false);
 assertEquals(mockitoCore, mockitoCore);
 verify(mockUtil).createMock(mockitoCoreClass, mockSettings);
 }

 @Test
 public void testVerify() {
 Object mock = mock(Object.class);
 verify(mock).toString();
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyState() {
 Object mock = mock(Object.class);
 mockingProgress.setState(ThreadSafeMockingProgress.State.DONE);
 verify(mock);
 }

 @Test
 public void testReset() {
 Object mock = mock(Object.class);
 verify(mock).toString();
 reset(mock);
 verify(mock, never()).toString();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNull() {
 verifyNoMoreInteractions();
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testStubbing() {
 Object mock = mock(Object.class);
 Object arg = new Object();
 OngoingStubbing<Object> stubbing = Mockito.when(mock.toString()).thenReturn(arg);
 assertEquals(arg, Mockito.verify(mock).toString());
 verify(stubbing).thenReturn(arg);
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer<String> answer = invocation -> invocation.getMethod().getName();
 Object result = Mockito.doAnswer(answer).when(mock).toString();
 assertEquals("toString", result);
 }

 @Test
 public void testStubVoid() {
 Object mock = mock(Object.class);
 Object arg = new Object();
 Mockito.stubVoid(mock::toString).thenCallRealMethod();
 Mockito.verify(mock).toString();
 assertEquals(arg, mockingProgress.getLastInvocation().getArguments()[0]);
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mock(Object.class);
 Object arg = new Object();
 when(mock.toString()).thenReturn(arg);
 Invocation invocation = mockingProgress.getLastInvocation();
 assertEquals(arg, invocation.getArguments()[0]);
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore mockitoCore = spy(new MockitoCore());
 mockitoCore.validateMockitoUsage();
 Mockito.verify(mockitoCore).validateState();
 }
}