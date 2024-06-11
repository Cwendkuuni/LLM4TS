package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

 @Test
 public void testAnswer_NullReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mockedObject = mock(Object.class);

  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(Object.class.getDeclaredMethod("toString"), new Class[0]));
  invocation.setTarget(mockedObject);

  assertNull(returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswer_NonMockableReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mockedObject = mock(Object.class);

  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(int[].class.getDeclaredMethod("length"), new Class[0]));
  invocation.setTarget(mockedObject);

  assertNull(returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswer_StringReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mockedObject = mock(Object.class);

  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(String.class.getDeclaredMethod("toString"), new Class[0]));
  invocation.setTarget(mockedObject);

  String result = (String) returnsSmartNulls.answer(invocation);
  assertEquals("SmartNull returned by unstubbed toString method on mock", result);
 }

 @Test
 public void testAnswer_ArrayReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mockedObject = mock(Object.class);

  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(int[].class.getDeclaredMethod("toString"), new Class[0]));
  invocation.setTarget(mockedObject);

  ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
  verify(mockedObject).toString();

  returnsSmartNulls.answer(invocation);
 }

 @Test
 public void testAnswer_ObjectReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mockedObject = mock(Object.class);

  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(Object.class.getDeclaredMethod("toString"), new Class[0]));
  invocation.setTarget(mockedObject);

  Object result = returnsSmartNulls.answer(invocation);
  assertNotNull(result);
  assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
 }
}