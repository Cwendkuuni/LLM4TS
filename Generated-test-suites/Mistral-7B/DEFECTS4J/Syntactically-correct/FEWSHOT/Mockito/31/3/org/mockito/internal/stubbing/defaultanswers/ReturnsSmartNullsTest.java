package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito. internal.debugging.location.StackTraces;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 @Test
 public void testAnswerNull() {
 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 Object mock = mock(Object.class);
 when(mock.methodThatReturnsNull(1)).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = invocationCaptor.capture(mock.methodThatReturnsNull(1));
 assertNull(returnsSmartNulls.answer(invocation));
 verify(mock).methodThatReturnsNull(1);
 assertEquals("SmartNull returned by unstubbed methodThatReturnsNull(int) method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber());
 }

 @Test
 public void testAnswerInstanceOf() {
 Object mock = mock(String.class);
 when(mock.methodThatReturnsString()).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = mock.methodThatReturnsString();
 assertNotNull(returnsSmartNulls.answer(invocation));
 assertEquals("SmartNull returned by unstubbed methodThatReturnsString() method on mock", returnsSmartNulls.answer(invocation).getClass().getName());
 }

 @Test
 public void testAnswerPrimitive() {
 Object mock = mock(Integer.TYPE);
 when(mock.methodThatReturnsInt()).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = mock.methodThatReturnsInt();
 assertNull(returnsSmartNulls.answer(invocation));
 verify(mock).methodThatReturnsInt();
 assertEquals("SmartNull returned by unstubbed methodThatReturnsInt() method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber());
 }

 @Test
 public void testAnswerInterface() {
 Object mock = mock(Serializable.class);
 when(mock.methodThatReturnsSerializable()).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = mock.methodThatReturnsSerializable();
 assertNull(returnsSmartNulls.answer(invocation));
 verify(mock).methodThatReturnsSerializable();
 assertEquals("SmartNull returned by unstubbed methodThatReturnsSerializable() method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber());
 }

 @Test
 public void testAnswerFinalClass() {
 Object mock = mock(String[].class);
 when(mock.methodThatReturnsStringArray()).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = mock.methodThatReturnsStringArray();
 assertNull(returnsSmartNulls.answer(invocation));
 verify(mock).methodThatReturnsStringArray();
 assertEquals("SmartNull returned by unstubbed methodThatReturnsStringArray() method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber());
 }

 @Test
 public void testAnswerArray() {
 Object mock = mock(Object[].class);
 when(mock.methodThatReturnsObjectArray()).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = mock.methodThatReturnsObjectArray();
 assertNull(returnsSmartNulls.answer(invocation));
 verify(mock).methodThatReturnsObjectArray();
 assertEquals("SmartNull returned by unstubbed methodThatReturnsObjectArray() method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber());
 }

 @Test
 public void testAnswerClassName() {
 Object mock = mock(String.class);
 when(mock.methodThatReturnsClassName()).thenAnswer(returnsSmartNulls);
 InvocationOnMock invocation = mock.methodThatReturnsClassName();
 assertNull(returnsSmartNulls.answer(invocation));
 verify(mock).methodThatReturnsClassName();
 assertEquals("SmartNull returned by unstubbed methodThatReturnsClassName() method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber());
 }

 @Test
 public void testToString() {
 Object mock = spy(String.class);
 when(mock.toString()).thenAnswer(returnsSmartNulls);Assert.assertNotNull(mock.toString());assertEquals("SmartNull returned by unstubbed toString() method on mock", mock.toString().getClass().getName()); }

 @Test(expected = StackTraceElement[][].class)
 public void testThrowingException() {
 Object mock = mock(Throwable.class);when(mock.methodThatThrowsException()).thenAnswer(returnsSmartNulls);InvocationOnMock invocation = mock.methodThatThrowsException();returnsSmartNulls.answer(invocation); }

 @Test
 public void testDelegateAnswer() {
 Object mock = mock(Object.class);when(mock.methodThatReturnsNull(1)).thenAnswer(new ReturnsMoreEmptyValues());when(mock.methodThatReturnsNull(1)).thenAnswer(returnsSmartNulls);InvocationOnMock invocation = invocationCaptor.capture(mock.methodThatReturnsNull(1));assertNull(returnsSmartNulls.answer(invocation));verify(mock).methodThatReturnsNull(1);assertEquals("SmartNull returned by unstubbed methodThatReturnsNull(int) method on mock", invocation.getInvocationLogInfo().getLocation().getLineNumber()); }

 @Test
 public void testDelegateAnswerWithImposterisedMethodInterceptor() {
 ClassImposterizer imposterizer = ClassImposterizer.INSTANCE; Mockito mockito = Mockito.mock(Mockito.class);when(mockito.methodThatReturnsSmartNullInterceptor()).thenReturn(new ThrowingInterceptor(null));Object mock = imposterizer.createMock(Object.class, mockito);when(mock.methodThatReturnsSmartNull()).thenAnswer(new ReturnsSmartNulls());InvocationOnMock invocation = mock.methodThatReturnsSmartNull();assertEquals("SmartNull returned by unstubbed methodThatReturnsSmartNull() method on mock", returnsSmartNulls.answer(invocation).getClass().getName()); }
}