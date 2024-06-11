package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private Object target;

 @Before
 public void setUp() {
 target = mock(ReturnsSmartNulls.class, new ReturnsSmartNulls());
 }

 @Test
 public void testReturnsSmartNullForToStringMethod() {
 String expected = "SmartNull returned by unstubbed method";
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Mockito.when(target.answer(ArgumentMatchers.eq(invocation))).thenReturn("SmartNull");
 String result = (String) target.answer(invocation);
 verify(target).throwing(new NullPointerException(""));
 verify(target).answer(invocation).equals(expected);
 }

 @Test
 public void testReturnsNullForNonMockableType() {
 final int expected = 0;
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Mockito.when(target.answer(invocation)).thenReturn(null);
 int result = (int) target.answer(invocation);
 verify(target).throwing(new NullPointerException(""));
 verify(target).answer(invocation).equals(null);
 }

 @Test
 public void testReturnsDefaultValue() {
 final String expected = "expected value";
 Answer<String> answer = Mockito.mock(Answer.class);
 Mockito.when(target.delegate.answer(ArgumentMatchers.eq(Mockito.any(InvocationOnMock.class)))).thenReturn(answer);
 Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(expected);
 String result = (String) target.answer(Mockito.mock(InvocationOnMock.class));
 verify(answer).answer(Mockito.any(InvocationOnMock.class)).thenReturn(expected);
 verify(target).answer(Mockito.any(InvocationOnMock.class)).thenReturn(expected);
 verify(target).answer(Mockito.any(InvocationOnMock.class)).thenReturn(expected);
 }

 @Test(expected = NullPointerException.class)
 public void testThrowsNullPointerExceptionForUnstubbedMethod() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 target.answer(invocation);
 }
}