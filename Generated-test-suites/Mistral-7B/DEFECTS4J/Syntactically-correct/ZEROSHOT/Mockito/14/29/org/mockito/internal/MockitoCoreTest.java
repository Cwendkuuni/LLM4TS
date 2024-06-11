package org.mockito.internal.MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.AnswerStub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertNull;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private MockSettingsImpl mockSettings;
 private IOngoingStubbing ongoingStubbing;
 private InOrder inOrder;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testMock() {
 mock = mockitoCore.mock(Mockito.mock(String.class), mockSettings);
 assertNull(mockingProgress.getLastInvocation());
 verify(mock).toString();
 }

 @Test
 public void testStub() {
 ongoingStubbing = mockitoCore.stub();
 when(mock).thenReturn("Test");
 verify(mockitoCore.mock(String.class, mockSettings)).equals("Test");
 }

 @Test
 public void testStubVoid() {
 mock = mockitoCore.mock(String.class, mockSettings);
 mockitoCore.stubVoid(mock).thenCallRealMethod();
 verify(mock).equals("Test");
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 mock = mockitoCore.mock(String.class, mockSettings);
 ongoingStubbing = mockitoCore.stub(mock);
 when(mock).thenReturn("Test");
 verify(mockitoCore.mock(String.class, mockSettings)).equals("Test");
 }

 @Test
 public void testOngoingStubbing() {
 mock = mockitoCore.mock(String.class, mockSettings);
 ongoingStubbing = mockitoCore.when(mock);
 when(mock).thenReturn("Test");
 verify(mockitoCore.mock(String.class, mockSettings)).equals("Test");
 }

 @Test
 public void testVerify() {
 mock = mockitoCore.mock(String.class, mockSettings);
 verify(mockitoCore.mock(String.class, mockSettings)).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 mockitoCore.verify(null, VerificationModeFactory.instance().nullVerificationMode());
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationModeFactory.instance().nullVerificationMode());
 }

 @Test
 public void testVerifyNoInteractions() {
 mock = mockitoCore.mock(String.class, mockSettings);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoInteractionsInOrder() {
 mock = mockitoCore.mock(String[].class, mockSettings);
 mockitoCore.inOrder(mock).verify(mock).toString();
 mockitoCore.inOrder(mock).verify(mock).toString();
 }

 @Test
 public void testVerifyInteractions() {
 mock = mockitoCore.mock(String.class, mockSettings);
 when(mock.toString()).thenReturn("Test");
 mockitoCore.verify(mock).toString();
 }

 @Test
 public void testVerifyInteractionsInOrder() {
 mock = mockitoCore.mock(String[].class, mockSettings);
 when(mock[0].toString()).thenReturn("First");
 when(mock[1].toString()).thenReturn("Second");
 InOrder inOrder = mockitoCore.inOrder(mock);
 inOrder.verify(mock[0]).toString();
 inOrder.verify(mock[1]).toString();
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = new AnswerStub<>("Answer");
 mock = mockitoCore.mock(String.class, mockSettings);
 mockitoCore.doAnswer(answer).when(mock).toString();
 verify(mock).toString();
 assertNull(mockingProgress.getLastInvocation());
 assertNull(answer.getValue());
 }

 @Test
 public void testReset() {
 mock = mockitoCore.mock(String.class, mockSettings);
 when(mock.toString()).thenReturn("Test");
 verify(mockitoCore.mock(String.class, mockSettings)).toString();
 mockitoCore.reset(mock);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNull() {
 mock = mockitoCore.mock(String.class, mockSettings);
 verifyNoMoreInteractions((Object) null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyList() {
 mock = mockitoCore.mock(String.class, mockSettings);
 verifyNoMoreInteractions(new Object[0]);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNoMocks() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithOneMock() {
 mock = mockitoCore.mock(String.class, mockSettings);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithMultipleMocks() {
 mock = mockitoCore.mock(String.class, mockSettings);
 Object mock2 = mockitoCore.mock(Integer.class, mockSettings);
 Object[] mocks = {mock, mock2};
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithInOrder() {
 mock = mockitoCore.mock(String.class, mockSettings);
 mockitoCore.inOrder(mock).verify(mock).toString();
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithInOrderAndMultipleMocks() {
 mock = mockitoCore.mock(String.class, mockSettings);
 Object mock2 = mockitoCore.mock(Integer.class, mockSettings);
 Object[] mocks = {mock, mock2};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 inOrder.verify(mock).toString();
 inOrder.verify(mock2).toString();
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 mock = mockitoCore.mock(String.class, mockSettings);
 when(mock.toString()).thenReturn("Test");
 verify(mockitoCore.mock(String.class, mockSettings)).toString();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("Test", lastInvocation.getArguments()[0]);
 assertEquals("toString", lastInvocation.getMethod().getName());
 }

}