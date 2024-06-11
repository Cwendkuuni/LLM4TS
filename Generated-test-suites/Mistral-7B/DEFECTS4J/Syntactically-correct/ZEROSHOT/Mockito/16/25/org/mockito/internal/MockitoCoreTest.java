package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerCalledWith;
import org.mockito.stubbing.AnswerReturns;
import org.mockito.stubbing.AnswerStub;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertNotNull;

public class MockitoCoreTest {

 @Mock
 private MockitoCore mockitoCore;

 @InjectMocks
 private MockitoCore testee;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testMockCreation() {
 Object mocked = testee.mock(String.class);
 assertNotNull(mocked);
 }

 @Test
 public void testStubbing() {
 Object mocked = testee.mock(String.class);
 when(mocked).thenReturn("test");
 String result = (String) mocked.getClass().getMethod("getValue").invoke(mocked);
 assertNotNull(result);
 assertNotNull(testee.getLastInvocation());
 }

 @Test
 public void testStubbingWithAnswer() {
 Object mocked = testee.mock(String.class);
 when(mocked).thenAnswer(new AnswerStub() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "test";
 }
 });
 String result = (String) mocked.getClass().getMethod("getValue").invoke(mocked);
 assertNotNull(result);
 assertNotNull(testee.getLastInvocation());
 }

 @Test
 public void testStubbingWithAnswerCalledWith() {
 Object mocked = testee.mock(String.class);
 when(mocked).thenAnswer(new AnswerCalledWith("test") {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "test";
 }
 });
 String result = (String) mocked.getClass().getMethod("getValue").invoke(mocked);
 assertNotNull(result);
 assertNotNull(testee.getLastInvocation());
 }

 @Test
 public void testStubbingWithAnswerReturns() {
 Object mocked = testee.mock(String.class);
 when(mocked).thenAnswer(new AnswerReturns("test"));
 String result = (String) mocked.getClass().getMethod("getValue").invoke(mocked);
 assertNotNull(result);
 assertNotNull(testee.getLastInvocation());
 }

 @Test
 public void testVerifying() {
 Object mocked = testee.mock(String.class);
 testee.verify(mocked, VerificationMode.RETURNED_ONCE);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyingWithNullPassed() {
 testee.verify(null, VerificationMode.RETURNED_ONCE);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyingWithNotAMockPassed() {
 Object notAMock = new Object();
 testee.verify(notAMock, VerificationMode.RETURNED_ONCE);
 }

 @Test
 public void testVerifyingNoMoreInteractions() {
 Object mocked = testee.mock(String.class);
 testee.verifyNoMoreInteractions(mocked);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyingNoMoreInteractionsWithNullPassed() {
 testee.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyingNoMoreInteractionsWithNotAMockPassed() {
 Object notAMock = new Object();
 testee.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testReset() {
 Object mocked = testee.mock(String.class);
 testee.stub(mocked).thenReturn("test");
 String result = (String) mocked.getClass().getMethod("getValue").invoke(mocked);
 assertNotNull(result);
 testee.reset();
 String resetResult = (String) mocked.getClass().getMethod("getValue").invoke(mocked);
 assertNull(resetResult);
 }

 @Test
 public void testInOrder() {
 Object mocked1 = testee.mock(String.class);
 Object mocked2 = testee.mock(String.class);
 InOrder inOrder = testee.inOrder(mocked1, mocked2);
 testee.when(mocked1).thenReturn("test1");
 testee.when(mocked2).thenReturn("test2");
 inOrder.verify(mocked1).method1();
 inOrder.verify(mocked2).method2();
 }
}