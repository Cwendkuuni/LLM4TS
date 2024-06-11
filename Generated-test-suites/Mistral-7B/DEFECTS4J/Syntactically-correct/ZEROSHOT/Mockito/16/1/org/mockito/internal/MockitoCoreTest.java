package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockSettings mockSettings;
 private MockitoCore mockitoCore;
 private Object mockObject;
 private Object spyObject;
 private OngoingStubbing stubbing;
 private Invocation lastInvocation;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockSettings.verbose(true);
 mockSettings.initMocks(true);
 mockitoCore = new MockitoCore();
 mockObject = mock(Object.class, mockSettings);
 spyObject = spy(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 assertNotNull(mock);
 assertEquals(mock.getClass(), Object.class);
 }

 @Test
 public void testStubbing() {
 stubbing = mockitoCore.stub();
 assertNotNull(stubbing);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStubbingWithNoArgument() {
 mockitoCore.stub();
 }

 @Test
 public void testStubbingWithObjectArgument() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 Object stub = mockitoCore.stub(mock);
 assertNotNull(stub);
 assertSame(mock, stub.getMock());
 }

 @Test
 public void testStubbingWithAnswer() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 Object stub = mockitoCore.stub(mock);
 when(mock).thenReturn("Test");
 assertEquals("Test", stub.thenAnswer(i -> "Test").get());
 }

 @Test
 public void testVerify() {
 verify(mockObject);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullObject() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object() {};
 Mockito.verify(notAMock);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 when(mock).thenReturn("Test");
 Object resetMock = mockitoCore.reset(mock);
 assertEquals(mock, resetMock);
 assertNull(stubbing);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullObject() {
 verifyNoMoreInteractions();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithEmptyArray() {
 verifyNoMoreInteractions();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object() {};
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, mockSettings);
 Object mock2 = mockitoCore.mock(Object.class, mockSettings);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).method1();
 inOrder.verify(mock1).method2();
 inOrder.verify(mock2).method1();
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 Object answer = new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return "Test";
 }
 };
 Object stub = mockitoCore.stub(mock);
 stub.thenAnswer(answer);
 assertEquals("Test", stub.get());
 }

 @Test
 public void testDoAnswer() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 Object answer = new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return "Test";
 }
 };
 Object stub = mockitoCore.doAnswer(answer);
 stub = mockitoCore.mock(Object.class, mockSettings);
 when(stub).thenReturn(mock);
 assertEquals("Test", stub.get());
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 Object stub = mockitoCore.stubVoid(mock);
 mockitoCore.when(mock).thenCallRealMethod();
 stub.callRealMethod();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 when(mock).thenReturn("Test");
 stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn("ReturnValue");
 lastInvocation = mockitoCore.getLastInvocation();
 assertEquals(lastInvocation.getMethod(), mockitoCore.mock(Method.class, mockSettings).getMethod());
 assertEquals(lastInvocation.getArguments(), new Object[]{mockitoCore.mock(Object.class, mockSettings)});
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}