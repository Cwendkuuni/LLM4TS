package org.mockito.internal.tests;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalVerificationMode;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotation;
import org.mockito.stubbing.AnswerCallBack;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.AnswerWrapper;
import org.mockito.verification.VerificationModeFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.mockito.internal.MockitoCore.getLastInvocation;
import static org.mockito.internal.MockitoCore.validateMockitoUsage;

public class MockitoCoreTest {

 private MockitoCore mockitoCore = new MockitoCore();

 @Test
 public void testMock() {
 MockitoAnnotations.initMocks(this);
 Object mock = mockitoCore.mock(String.class);
 Assert.assertNotNull(mock);
 }

 @Test
 public void testMockWithSettings() {
 MockitoAnnotations.initMocks(this);
 MockSettings settings = new MockSettings();
 Object mock = mockitoCore.mock(String.class, settings, true);
 Assert.assertNotNull(mock);
 }

 @Test
 public void testStub() {
 MockitoAnnotations.initMocks(this);
 Object mock = mockitoCore.mock(String.class);
 Object stub = mockitoCore.stub();
 when(mock.toString()).thenReturn("MockitoCoreTest");
 Assert.assertEquals("MockitoCoreTest", mock.toString());
 }

 @Test
 public void testDeprecatedStub() {
 MockitoAnnotations.initMocks(this);
 Object mock = mockitoCore.mock(String.class);
 Object stub = mockitoCore.stub(mock);
 when(mock.toString()).thenReturn("MockitoCoreTest");
 Assert.assertEquals("MockitoCoreTest", mock.toString());
 }

 @Test
 public void testWhen() {
 MockitoAnnotations.initMocks(this);
 Object mock = mockitoCore.mock(String.class);
 Object stub = mockitoCore.when(mock);
 when(mock.toString()).thenReturn("MockitoCoreTest");
 Assert.assertEquals("MockitoCoreTest", mock.toString());
 }

 @Test
 public void testVerify() {
 MockitoAnnotations.initMocks(this);
 Object mock = mockitoCore.mock(String.class);
 verify(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object object = new Object();
 mockitoCore.verify(object);
 }

 @Test
 public void testReset() {
 MockitoAnnotations.initMocks(this);
 Object mock1 = mockitoCore.mock(String.class);
 Object mock2 = mockitoCore.mock(String.class);
 mockitoCore.reset(mock1, mock2);
 verify(mock1).toString();
 verify(mock2).toString();
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 MockitoAnnotations.initMocks(this);
 Object mock = mockitoCore.mock(String.class);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object object = new Object();
 mockitoCore.verifyNoMoreInteractions(object);
 }

 @Test
 public void testInOrder() {
 MockitoAnnotations.initMocks(this);
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testDoAnswer() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 Answer answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0).toString();
 }
 };
 when(mock.toString()).thenAnswer(answer);
 Assert.assertEquals("MockitoCoreTest", mock.toString("MockitoCoreTest"));
 }

 @Test
 public void testStubVoid() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 mockitoCore.stubVoid(mock).print();
 mock.print();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");
 Object lastInvocation = getLastInvocation();
 Method method = lastInvocation.getMethod();
 Assert.assertEquals("toString", method.getName());
 }

 @Test
 public void testAnswer() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 AnswerStub answerStub = Mockito.stub(mock, "toString").toReturn("MockitoCoreTest");
 String result = mock.toString();
 Assert.assertEquals("MockitoCoreTest", result);
 }

 @Test
 public void testAnswerWrapper() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 AnswerWrapper answerWrapper = Mockito.stub(mock, "toString").to("MockitoCoreTest");
 String result = mock.toString();
 Assert.assertEquals("MockitoCoreTest", result);
 }

 @Test
 public void testAnswerCallBack() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 AnswerCallBack answerCallBack = new AnswerCallBack<String>() {
 @Override
 public String call(InvocationOnMock invocation) {
 String argument = invocation.getArgumentAt(0);
 return argument.toString();
 }
 };
 when(mock.toString()).thenAnswer(answerCallBack);
 String result = mock.toString("MockitoCoreTest");
 Assert.assertEquals("MockitoCoreTest", result);
 }

 @Test
 public void testVerificationModeFactory() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 InternalVerificationMode internalVerificationMode = VerificationModeFactory.withDefaultTimeout(1000);
 verify(mock, internalVerificationMode).toString();
 }

 @Test
 public void testVerificationMode() {
 MockitoAnnotations.initMocks(this);
 String mock = mockitoCore.mock(String.class);
 InternalVerificationMode internalVerificationMode = VerificationMode.withDefaultTimeout(1000);
 verify(mock, internalVerificationMode).toString();
 }
}