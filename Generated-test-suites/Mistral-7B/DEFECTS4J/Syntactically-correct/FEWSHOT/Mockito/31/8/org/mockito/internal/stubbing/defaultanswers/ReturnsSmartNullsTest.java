package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 @Test
 public void testAnswerReturnsDefaultValueIfAvailable() {
  ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);

  Object defaultValue = new Object();
  when(returnsSmartNulls.answer(invocation)).thenReturn(defaultValue);

  Object result = returnsSmartNulls.answer(invocation);
  assertNotNull(result);
  assertEquals(defaultValue, result);
 }

 @Test
 public void testAnswerReturnsSmartNullIfDefaultValueIsNotAvailableAndTypeIsMockable() throws Exception {
  ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Class<?> mockableType = mock(Class.class);

  Method method = mock(Method.class);
  when(method.getName()).thenReturn("getName");
  when(method.getReturnType()).thenReturn(mockableType);
  when(invocation.getMethod()).thenReturn(method);

  Object result = returnsSmartNulls.answer(invocation);
  assertNotNull(result);
  assertEquals("SmartNull returned by unstubbed getName() method on mock", result.getClass().getCanonicalName());
 }

 @Test
 public void testAnswerReturnsNullIfTypeIsNotMockable() throws Exception {
  ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Class<?> finalType = mock(Class.class);

  Method method = mock(Method.class);
  when(method.getName()).thenReturn("getName");
  when(method.getReturnType()).thenReturn(finalType);
  when(invocation.getMethod()).thenReturn(method);

  Object result = returnsSmartNulls.answer(invocation);
  assertNull(result);
 }

 @Test
 public void testThrowingInterceptorCatchToStringCall() throws Exception {
  ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
  InvocationOnMock invocation = mock(InvocationOnMock.class);

  Method method = mock(Method.class);
  when(method.getName()).thenReturn("toString");
  when(invocation.getMethod()).thenReturn(method);

  try {
   returnsSmartNulls.answer(invocation);
   fail("Expected smartNullPointerException to be thrown.");
  } catch (RuntimeException e) {
   assertEquals("java.lang.RuntimeException: SmartNull pointer exception at", e.getMessage());
  }
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerReturnsNullAndNullIsReturnedByMethod() throws Exception {
  ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);

  Method method = mock(Method.class);
  when(method.getName()).thenReturn("getName");
  when(invocation.getMethod()).thenReturn(method);

  Object result = returnsSmartNulls.answer(invocation);
  assertNull(result);
 }

}