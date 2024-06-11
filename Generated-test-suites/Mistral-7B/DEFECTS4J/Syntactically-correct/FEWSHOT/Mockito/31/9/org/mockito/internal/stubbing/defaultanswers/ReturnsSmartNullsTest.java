package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.answers.Answer;
import org.mockito.internal.stubbing.answers.ReturnsMoreEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Answer<Object> delegate;
 private ObjectMethodsGuru objectMethodsGuru;
 private Location location;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 delegate = mock(Answer.class);
 objectMethodsGuru = spy(new ObjectMethodsGuru());
 }

 @Test
 public void testAnswer_MethodReturnsString() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(delegate.answer(invocation)).thenReturn("Test");

 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("Test", result);
 }

 @Test
 public void testAnswer_MethodReturnsNull() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(delegate.answer(invocation)).thenReturn(null);

 AssertNullTellMeWhereYouCalledIt tellMeWhereYouCalledIt = new AssertNullTellMeWhereYouCalledIt();
 doAnswer(tellMeWhereYouCalledIt).when(invocation).answer(eq(invocation));

 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 verify(tellMeWhereYouCalledIt).onNull();
 }

 @Test
 public void testAnswer_MethodReturnsSmartNull() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 ObjectMethodsGuru objectMethodsGuruSpy = spy(objectMethodsGuru);
 when(objectMethodsGuruSpy.isToString(anyMethod())).thenReturn(true);

 when(delegate.answer(invocation)).thenReturn(null);

 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
 }

 @Test
 public void testAnswer_MethodReturnsSmartNull_WithLocation() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 ObjectMethodsGuru objectMethodsGuruSpy = spy(objectMethodsGuru);
 location = new Location();
 when(objectMethodsGuruSpy.isToString(anyMethod())).thenReturn(true);
 when(invocation.getLocation()).thenReturn(location);

 when(delegate.answer(invocation)).thenReturn(null);

 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("SmartNull returned by unstubbed " + location.getClassName() + "." + location.getMethodName() + "() method on mock", result.toString());
 }

 @Test
 public void testAnswer_MethodReturnsSmartNull_WhenTypeIsMockable() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Class<?> type = mock(String[].class);
 when(delegate.answer(invocation)).thenReturn(null);

 Object result = returnsSmartNulls.answer(invocation);
 assertSame(type, result.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswer_MethodReturnsSmartNull_WhenTypeIsNotMockable() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 final Class<?> finalClass = String.class;
 when(delegate.answer(invocation)).thenReturn(null);

 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 verify(objectMethodsGuru).isToString(eq(finalClass.getDeclaredMethod("toString")));
 }
}

 class AssertNullTellMeWhereYouCalledIt implements Answer<Void> {
 private void onNull() {
 // Empty implementation.
 }

 public Void answer(InvocationOnMock invocation) throws Throwable {
 onNull();
 return null;
 }
}