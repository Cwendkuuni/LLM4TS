package test.org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import org.mockito.internal.debugging.Location;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerImpl;

import org.mockito.exceptions.Reporter;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private static final long serialVersionUID = 7618312406617949441L;

 @Test
 public void testSmartNullReturnWhenMethodIsToString() throws Exception {
  Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class, returnsSmartNulls);
  String smartNull = (String) returnsSmartNulls.answer(new InvocationOnMock(mock, new Method[] { Mockito.method("toString") }, new Object[] {}));
  assertEquals("SmartNull returned by unstubbed toString() method on mock", smartNull);
 }

 @Test
 public void testNullReturnWhenMethodIsNotMockable() throws Exception {
  Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(FinalClass.class, returnsSmartNulls);
  assertNull(returnsSmartNulls.answer(new InvocationOnMock(mock, new Method[] { Mockito.method("method1") }, new Object[] {})));
 }

 @Test
 public void testDelegateAnswerReturn() throws Exception {
  Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
  Answer<Object> returnsMoreEmptyValues = mock(Answer.class);
  Answer<Object> delegate = new AnswerImpl<>(returnsMoreEmptyValues);
  ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegate);
  Object mock = mock(Object.class, smartNulls);
  doReturn("delegate answer").answer(mock, new Method("method1", new Class[] {}), new Object[] {});
  Object delegateAnswer = smartNulls.answer(new InvocationOnMock(mock, new Method[] { Mockito.method("method1") }, new Object[] {}));
  assertEquals("delegate answer", delegateAnswer);
 }

 @Test
 public void testEmptyAnswerReturn() throws Exception {
  Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Object.class, returnsSmartNulls);
  Object emptyAnswer = new Object();
  doReturn(emptyAnswer).answer(mock, new Method("method1", new Class[] {}), new Object[] {});
  Object result = returnsSmartNulls.answer(new InvocationOnMock(mock, new Method[] { Mockito.method("method1") }, new Object[] {}));
  assertSame(emptyAnswer, result);
 }

 @Test
 public void testThrowingInterceptor() throws Exception {
  Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Object.class, returnsSmartNulls);
  Method method = new Method("method1", new Class[] {});
  try {
   returnsSmartNulls.answer(new InvocationOnMock(mock, new Method[] { method }, new Object[] {}));
   fail("Expected smart null pointer exception");
  } catch (InvocationTargetException e) {
   Throwable cause = e.getCause();
   assertTrue(cause instanceof Reporter.NullPointerException);
  }
 }

 @Test
 public void testDelegateAnswerThrowingInterceptor() throws Exception {
  Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
  Answer<Object> returnsMoreEmptyValues = mock(Answer.class);
  Answer<Object> delegate = new AnswerImpl<>(returnsMoreEmptyValues);
  ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegate);
  Object mock = mock(Object.class, smartNulls);
  doThrow(new Reporter.NullPointerException("test")).answer(mock, new Method("method1", new Class[] {}), new Object[] {});
  InvocationOnMock invocationOnMock = new InvocationOnMock(mock, new Method[] { Mockito.method("method1") }, new Object[] {});
  smartNulls.answer(invocationOnMock);
  verifyZeroInteractions(mock);
  verify(returnsMoreEmptyValues).answer(invocationOnMock);
 }

 class FinalClass {
  public void method1() {
  }
 }
}