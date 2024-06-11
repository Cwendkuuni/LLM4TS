package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

 private static final Class<ReturnsSmartNulls> RETURNS_SMART_NULLS_CLASS = ReturnsSmartNulls.class;

 @Test
 public void testAnswerNull() {
 final ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
 final InvocationOnMock invocation = mock(InvocationOnMock.class);

 verify(returnsSmartNulls).answer(invocation);
 assertNull(returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerEmptyArray() {
 final ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
 final InvocationOnMock invocation = mock(InvocationOnMock.class);
 final Object[] emptyArray = new Object[0];

 verify(returnsSmartNulls).answer(invocation);
 assertEquals(emptyArray, returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerInstanceOfObject() {
 final ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
 final Object instance = new Object();
 final InvocationOnMock invocation = mock(InvocationOnMock.class);

 verify(returnsSmartNulls).answer(invocation);
 assertEquals(instance, returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerInstanceOfString() {
 final ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
 final String string = "SmartNull";
 final InvocationOnMock invocation = mock(InvocationOnMock.class);

 verify(returnsSmartNulls).answer(invocation);
 assertEquals(string, returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerInstanceOfThrowingInterceptor() throws Exception {
 final ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
 final InvocationOnMock invocation = mock(InvocationOnMock.class);
 final ThrowingInterceptor throwingInterceptor = new ThrowingInterceptor(invocation);
 final Class<ThrowingInterceptor> throwingInterceptorClass = ThrowingInterceptor.class;

 verify(returnsSmartNulls).answer(invocation);
 assertEquals(throwingInterceptor, ClassImposterizer.INSTANCE.imposterise(throwingInterceptor, throwingInterceptorClass));
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerNullPointerException() throws Exception {
 final ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
 final InvocationOnMock invocation = mock(InvocationOnMock.class);
 final ThrowingInterceptor throwingInterceptor = new ThrowingInterceptor(invocation);

 verify(returnsSmartNulls).answer(invocation);
 new Reporter().smartNullPointerException(throwingInterceptor.location);
 assertNull(returnsSmartNulls.answer(invocation));
 }
}