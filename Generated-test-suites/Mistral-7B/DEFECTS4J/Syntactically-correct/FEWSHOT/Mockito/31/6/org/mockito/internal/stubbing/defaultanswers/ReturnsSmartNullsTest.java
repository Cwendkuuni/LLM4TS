package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.answers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.answers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private static final Class<ReturnsSmartNulls> RETURNS_SMART_NULLS_CLASS = ReturnsSmartNulls.class;

 @Test
 public void testAnswerInstanceOfReturnsSmartNullsInstance() {
 assertNotNull(Mockito.mock(Answer.class, RETURNS_SMART_NULLS_CLASS));
 }

 @Test
 public void testAnswerMethodReturnsNullWhenDelegateAnswerReturnsNull() {
 Class<?> type = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);

 Mockito.when(returnsSmartNulls.answer(Mockito.eq(invocation))).thenCallRealMethod();

 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 @Test
 public void testAnswerMethodReturnsSmartNullWhenDelegateAnswerReturnsSmartNull() {
 Class<?> type = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);
 Mockito.when(returnsSmartNulls.delegate.answer(invocation)).thenReturn("stubbed result");

 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
 }

 @Test
 public void testAnswerMethodReturnsSmartNullWhenDelegateAnswerReturnsNullAndTypeIsMockable() throws Exception {
 Class<?> type = ClassImposterizer.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 ClassImposterizer classImposterizer = new ClassImposterizer();

 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);

 Object result = returnsSmartNulls.answer(invocation);
 assertNotNull(result);
 assertInstanceOf(ClassImposterizer.class, result);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerMethodReturnsNullWhenDelegateAnswerReturnsNullAndTypeIsNotMockable() throws Exception {
 Class<?> type = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);
 Mockito.when(returnsSmartNulls.delegate.answer(invocation)).thenReturn(null);

 ReturnsSmartNulls throwingAnswer = new ReturnsSmartNulls();
 ThrowingInterceptor throwingInterceptor = new ThrowingInterceptor(invocation);
 Class<?> throwingAnswerType = throwingAnswer.getClass();

 ClassImposterizer classImposterizer = Mockito.spy(ClassImposterizer.class);
 Mockito.when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);

 Object result = throwingAnswer.answer(invocation);
 assertNull(result);

 Mockito.verify(classImposterizer, Mockito.times(1)).imposterise(Mockito.eq(throwingInterceptor), Mockito.eq(type));
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerMethodReturnsNullWhenDelegateAnswerReturnsNullAndMethodToStringIsCalled() {
 Class<?> type = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);
 Mockito.when(returnsSmartNulls.delegate.answer(invocation)).thenReturn(null);

 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);

 Mockito.verify(invocation, Mockito.times(1)).getMethod().toString();
 }

 @Test
 public void testAnswerMethodReturnsDelegateAnswerWhenDelegateAnswerIsNotNull() {
 Class<?> type = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);
 Mockito.when(returnsSmartNulls.delegate.answer(invocation)).thenReturn("stubbed result");

 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("stubbed result", result);
 }

 @Test
 public void testIsToStringCalledWhenDelegateAnswerToStringIsCalled() {
 Class<?> type = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);
 Mockito.when(returnsSmartNulls.delegate.answer(invocation)).thenReturn("stubbed result");

 Mockito.verify(invocation, Mockito.times(1)).getMethod().toString();
 }

 @Test
 public void testToStringMethodReturnsSmartNullStringWhenAnswerIsNull() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 assertEquals("SmartNull returned by unstubbed method on mock", returnsSmartNulls.toString());
 }

 @Test
 public void testToStringMethodReturnsDelegateToStringStringWhenAnswerIsNotNull() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 String delegateToStringString = "delegate toString string";
 Mockito.when(returnsSmartNulls.delegate.toString()).thenReturn(delegateToStringString);
 assertEquals(delegateToStringString, returnsSmartNulls.toString());
 }

}