package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.api.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 }

 @Test
 public void testMockCreation() {
 mock = mockitoCore.mock(String.class);
 assertNotNull(mock);
 }

 @Test
 public void testMockCreationWithSettings() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 mockingProgress.reset();
 OngoingStubbing stubbing = mockitoCore.stub();
 when(mock).thenReturn("test");
 verify(mock).toString();
 assertNotNull(stubbing.getLastInvocation().getMethod());
 }

 @Test
 public void testStubbingDeprecatedMethod() {
 mockingProgress.reset();
 DeprecatedOngoingStubbing<String> stubbing = mockitoCore.stub((String) Arguments.any());
 when(mock).thenReturn("test");
 verify(mock).toString();
 assertNotNull(stubbing.getLastInvocation().getMethod());
 }

 @Test
 public void testStubbingWithWhen() {
 mockingProgress.reset();
 OngoingStubbing<String> stubbing = mockitoCore.when(mock);
 when(mock).thenReturn("test");
 verify(mock).toString();
 assertNotNull(stubbing.getLastInvocation().getMethod());
 }

 @Test
 public void testVerify() {
 mock = mockitoCore.mock(String.class);
 verify(mock).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, VerificationMode.TIMES(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyWithNotAMock() {
 String mock = new String("test");
 mockitoCore.verify(mock, VerificationMode.TIMES(1));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = mockitoCore.mock(String.class);
 stub(mock).returns("test");
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 String mock = new String("test");
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testReset() {
 mock = mockitoCore.mock(String.class);
 stub(mock).returns("test");
 reset(mock);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(String.class);
 Object mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(String.class);
 Answer<String> answer = invocation -> "test";
 stub(mock).thenAnswer(answer);
 verify(mock).toString();
 assertNotNull(mockitoCore.getLastInvocation().getArguments()[0]);
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mockitoCore.mock(String.class);
 stub(mock).voidMethod(ArgumentMatchers.eq("test"));
 verify(mock).voidMethod("test");
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}