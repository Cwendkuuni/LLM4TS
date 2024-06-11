package org.mockito.internal.MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Stubber stubber;
 private Answer answer;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 stubber = mockitoCore.stub();
 answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 };
 }

 @Test
 public void testMock() {
 Object mockedObject = mockitoCore.mock(Object.class);
 verify(mockedObject).toString();
 }

 @Test
 public void testMockWithSettings() {
 MockSettings mockSettings = new MockSettings();
 Object mockedObject = mockitoCore.mock(Object.class, mockSettings);
 verify(mockedObject).toString();
 }

 @Test
 public void testStub() {
 stubber.thenReturn("ReturnValue");
 verify(mock).toString();
 verify(mock).toString(); // Should return the same value
 }

 @Test
 public void testWhen() {
 when(mock).thenReturn("ReturnValue");
 verify(mock).toString();
 verify(mock).toString(); // Should return the same value
 }

 @Test
 public void testDeprecatedStub() {
 mockitoCore.stub(mock).thenReturn("DeprecatedReturnValue");
 verify(mock).toString();
 verify(mock).toString(); // Should return the same value
 }

 @Test
 public void testOngoingStubbing() {
 stubber.thenAnswer(answer);
 verify(mock).toString();
 verify(mock).toString(); // Should return the same value
 }

 @Test
 public void testVerify() {
 verify(mock).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock).toString();
 }

 @Test
 public void testReset() {
 stubber.thenReturn("ReturnValue");
 verify(mock).toString();
 reset(mock);
 verify(mock).toString(); // Should return null
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object[] mocks = new Object[]{mock};
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsEmptyArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testDoAnswer() {
 stubber.doAnswer(answer);
 verify(mock).toString();
 verify(mock).toString(); // Should return the same value
 }

 @Test
 public void testStubVoid() {
 mockitoCore.stubVoid(mock).thenReturn("ReturnValue");
 verify(mock).toString();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 verify(mock).toString();
 verify(mock).toString(); // Should return the same value
 assertEquals("toString()", lastInvocation.getMethod().getName());
 assertEquals("mock", lastInvocation.getMock());
 assertEquals(ArgumentMatchers.eq("toString()"), lastInvocation.getArguments()[0]);
 }
}