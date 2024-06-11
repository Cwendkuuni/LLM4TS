package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mockObject;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockObject = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl().mockBehavior(MockBehavior.LENIENT));
 verify(mockedObject).toString();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 when(mockObject).thenReturn("Hello World");
 String result = (String) mockitoCore.verify(mockObject);
 verify(mockObject).toString();
 verify(mockObject).hashCode();
 verify(mockObject).equals(mockObject);

 assertEquals("Hello World", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingWithNullMethodCall() {
 mockitoCore.when(null);
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<Object> stubbing = mockitoCore.when(mockObject);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.verify(mockObject);

 verify(mockObject).toString();
 verify(mockObject).hashCode();
 verify(mockObject).equals(mockObject);

 assertEquals("Hello World", result);
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mockObject);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.verify(mockObject);

 verify(mockObject).toString();
 verify(mockObject).hashCode();
 verify(mockObject).equals(mockObject);

 assertEquals("Hello World", result);
 }

 @Test
 public void testVerification() {
 verify(mockObject).toString();
 verifyZeroInteractions(mockObject);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null);
 }

 @Test
 public void testVerificationMode() {
 verify(mockObject, Mockito.never());
 verify(mockObject, Mockito.atLeastOnce());
 verify(mockObject, Mockito.atLeast(2));
 verify(mockObject, Mockito.atMostOnce());
 verify(mockObject, Mockito.atMost(2));
 }

 @Test
 public void testVerificationInOrder() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mockObject1, mockObject2);

 when(mockObject1.toString()).thenReturn("Object1");
 when(mockObject2.toString()).thenReturn("Object2");

 inOrder.verify(mockObject1).toString();
 inOrder.verify(mockObject2).toString();
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = invocation -> "Answer from Mockito";
 when(mockObject).thenAnswer(answer);
 String result = (String) mockitoCore.verify(mockObject);

 verify(mockObject).toString();
 verify(mockObject).hashCode();
 verify(mockObject).equals(mockObject);

 assertEquals("Answer from Mockito", result);
 }

 @Test
 public void testStubbingVoidMethod() {
 mockitoCore.stubVoid(mockObject);
 mockitoCore.verify(mockObject).voidMethod();
 }

 @Test
 public void testReset() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 mockitoCore.reset(mockObject1, mockObject2);
 verifyZeroInteractions(mockObject1, mockObject2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetWithNullMock() {
 mockitoCore.reset(null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mockObject = mock(Object.class);
 verify(mockObject).toString();
 verifyNoMoreInteractions(mockObject);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullMockList() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mockObject1, mockObject2);
 inOrder.verify(mockObject1).toString();
 inOrder.verify(mockObject2).toString();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockObject1, mockObject2), inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrderWithNullInOrder() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 Object mockOrder = mock(InOrder.class);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockObject1, mockObject2), mockOrder);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationWithNoOngoingStubbing() {
 mockitoCore.getLastInvocation();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = mock(InvocationOnMock.class);
 OngoingStubbing<Object> stubbing = mockitoCore.when(mockObject);
 stubbing.thenReturn("Hello World");
 mockitoCore.verify(mockObject);
 Invocation lastInvocation = mockitoCore.getLastInvocation();

 assertEquals(invocation, lastInvocation);
 }
}