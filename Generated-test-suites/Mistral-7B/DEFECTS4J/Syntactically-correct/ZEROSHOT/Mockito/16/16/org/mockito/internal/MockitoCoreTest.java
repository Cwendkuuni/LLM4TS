package org.mockito.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito. ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stub;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMock() {
 Class<MockTest> mockClass = MockTest.class;
 MockSettings mockSettings = new MockSettingsImpl();
 Object mock = mockitoCore.mock(mockClass, mockSettings);
 assertNotNull(mock);
 verify(mockitoCore).mock(eq(mockClass), eq(mockSettings), anyBoolean());
 }

 @Test
 public void testMockWithSettings() {
 Class<MockTest> mockClass = MockTest.class;
 MockSettings mockSettings = new MockSettingsImpl();
 Object mock = mockitoCore.mock(mockClass, mockSettings);
 assertNotNull(mock);
 verify(mockitoCore).mock(eq(mockClass), eq(mockSettings), anyBoolean());
 }

 @Test
 public void testStub() {
 Object stub = mockitoCore.stub();
 assertTrue(stub instanceof OngoingStubbing);
 verify(mockitoCore).stubbingStarted();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubWithNull() {
 Object stub = mockitoCore.stub();
 mockitoCore.stub(stub);
 }

 @Test
 public void testStubWithObject() {
 Object mock = mock(MockTest.class);
 OngoingStubbing<MockTest> stub = mockitoCore.when(mock);
 stub.thenReturn(new MockTest());
 assertEquals(new MockTest(), stub.get());
 }

 @Test
 public void testStubVoid() {
 Object mock = mock(MockTest.class);
 Stub<MockTest> stub = mockitoCore.stubVoid(mock);
 stub.thenAnswer(new AnswerStub<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "answered";
 }
 });
 assertEquals("answered", stub.call());
 }

 @Test
 public void testVerify() {
 Object mock = mock(MockTest.class);
 verify(mock, VerificationMode.RETURN).someMethod();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNull() {
 verify(null, VerificationMode.RETURN);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock, VerificationMode.RETURN);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(MockTest.class);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNull() {
 verifyNoMoreInteractions();
 }

 @Test
 public void testReset() {
 Object mock = mock(MockTest.class);
 mockitoCore.reset(mock);
 verify(mockitoCore).reset();
 reset(mock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(MockTest.class);
 Object mock2 = mock(MockTest.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderNull() {
 Object mock = mock(MockTest.class);
 InOrder inOrder = mockitoCore.inOrder(mock, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderNotAMock() {
 Object notAMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(notAMock);
 }

 @Test
 public void testAnswer() {
 Object mock = mock(MockTest.class);
 Answer<String> answer = arg -> arg.toString();
 OngoingStubbing<MockTest> stub = mockitoCore.when(mock).someMethod();
 stub.thenAnswer(answer);
 assertEquals("MockTest@", stub.get());
 }

 @Test
 public void testAnswerWithInvocation() {
 Object mock = mock(MockTest.class);
 Answer<String> answer = invocation -> invocation.getArgument(0).toString();
 OngoingStubbing<MockTest> stub = mockitoCore.when(mock).someMethod(ArgumentMatchers.anyString());
 stub.thenAnswer(answer);
 assertEquals("arg", stub.get());
 }

 @Test
 public void testAnswerWithArgumentMatcher() {
 Object mock = mock(MockTest.class);
 ArgumentMatcher<String> argumentMatcher = ArgumentMatchers.eq("arg");
 OngoingStubbing<MockTest> stub = mockitoCore.when(mock).someMethod(argumentMatcher);
 stub.thenReturn(new MockTest());
 assertEquals(new MockTest(), stub.get());
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mock(MockTest.class);
 OngoingStubbing<MockTest> stub = mockitoCore.when(mock).someMethod();
 stub.thenReturn(new MockTest());
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("someMethod(Ljava/lang/String;)Lorg/mockito/MockTest;", lastInvocation.getMethod().toGenericString());
 }
}

class MockTest {
}