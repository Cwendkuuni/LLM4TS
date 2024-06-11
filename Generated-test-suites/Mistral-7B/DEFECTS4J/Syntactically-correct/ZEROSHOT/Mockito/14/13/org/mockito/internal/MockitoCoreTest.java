package org.mockito.internal. MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Class<String> stringClass = String.class;
 MockSettings mockSettings = new MockSettingsImpl();
 String mock = mockitoCore.mock(stringClass, mockSettings);
 assertSame(stringClass, mock.getClass());
 }

 @Test
 public void testStubbingAndVerification() {
 final String value = "Mock Value";
 String mock = mock(String.class);
 ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

 when(mock).thenReturn(value);
 String result = mock.getText();
 assertEquals(value, result);

 InOrder inOrder = inOrder(mock);
 inOrder.verify(mock).getText();
 }

 @Test
 public void testVerifyNoInteractions() {
 Object mock = mock(Object.class);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyInteractionsInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);

 when(mock1.doSomething()).thenReturn("Result 1");
 when(mock2.doSomething()).thenReturn("Result 2");

 mock1.doSomething();
 mock2.doSomething();

 inOrder.verify(mock1).doSomething();
 inOrder.verify(mock2).doSomething();
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNullMock() {
 verify(null, Mockito.eq(null));
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock, Mockito.eq("Mock Value"));
 }

 @Test
 public void testAnswer() {
 final String answer = "Answer Value";
 Object mock = mock(Object.class);
 Answer<String> answerStub = arguments -> answer;
 when(mock).thenAnswer(answerStub);

 assertEquals(answer, mock.answerQuestion());
 }

 @Test
 public void testStubbingAndVerificationWithoutStubbingStarted() {
 mockitoCore.mock(String.class, new MockSettingsImpl());
 mockitoCore.verify(mockitoCore.getLastInvocation(), Mockito.eq("Method Call"));
 assertThrows(IllegalStateException.class, () -> mockitoCore.stub());
 }

 @Test
 public void testStubbingAndVerificationWithMultipleMocks() {
 String mock1 = mock(String.class);
 String mock2 = mock(String.class);
 ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

 when(mock1).thenReturn(mock1);
 when(mock1).thenReturn(mock2);
 when(mock2).thenReturn("Mock 2 Value");

 String result1 = mock1.getText();
 assertEquals(mock1, result1);

 String result2 = mock1.getText();
 assertEquals("Mock 2 Value", result2);

 InOrder inOrder = inOrder(mock1, mock2);
 inOrder.verify(mock1).getText();
 inOrder.verify(mock2).getText();
 }
}