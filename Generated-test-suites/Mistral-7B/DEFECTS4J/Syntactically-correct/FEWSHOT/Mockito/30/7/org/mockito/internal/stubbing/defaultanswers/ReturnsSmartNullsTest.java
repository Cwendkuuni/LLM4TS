package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.internal.stubbing.answers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.answers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 private static final Class<?> TYPE_STRING = String.class;
 private static final Class<?> TYPE_INTEGER = Integer.class;

 @Test
 public void testAnswerWithDefaultReturnValue() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(TYPE_STRING, "toString", new Class[0]));
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("SmartNull returned by unstubbed toString() method on mock", answer);
 }

 @Test
 public void testAnswerWithNullReturnValue() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(TYPE_STRING, "toString", new Class[0]));
  invocation.setTarget(mock);
  InvocationOnMock spy = mock(InvocationOnMock.class);
  spy.stubReturnValue(null);
  invocation.stubReturnValue(spy);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForString() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(TYPE_STRING, "toString", new Class[0]));
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("SmartNull returned by unstubbed toString() method on mock", answer.getClass().getSimpleName());
 }

 @Test
 public void testAnswerWithSmartNullForInteger() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Integer.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(TYPE_INTEGER, "intValue", new Class[0]));
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("SmartNull returned by unstubbed intValue() method on mock", answer.getClass().getSimpleName());
 }

 @Test
 public void testAnswerWithSmartNullForFinalClass() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(TYPE_STRING, "getClass", new Class[0]));
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForArray() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Object[].class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  invocation.setMethod(new Method(TYPE_OBJECT, "clone", new Class[0]));
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("[SmartNull]", answer.toString());
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithArguments() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(TYPE_STRING, "concat", new Class[] { TYPE_STRING });
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object[] arguments = new Object[1];
  arguments[0] = "foo";
  invocation.setArguments(arguments);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("SmartNull returned by unstubbed concat(String) method on mock", answer.getClass().getSimpleName());
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithParameters() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(StringBuilder.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  MethodInterceptor interceptor = mock(MethodInterceptor.class);
  MethodProxy proxy = mock(MethodProxy.class);
  Method method = new Method(TYPE_STRING, "append", new Class[] { TYPE_STRING });
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object[] arguments = new Object[1];
  arguments[0] = "foo";
  invocation.setArguments(arguments);
  invocation.setProxy(proxy);
  invocation.setInterceptor(interceptor);
  method.setAccessible(true);
  try {
   method.invoke(mock, arguments);
  } catch (Throwable e) {
   assert e.getCause() instanceof NullPointerException;
  }
  invocation.stubReturnValue(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("SmartNull returned by unstubbed append(String) method on mock", answer.getClass().getSimpleName());
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithVarargs() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String[].class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(TYPE_OBJECT, "toArray", new Class[0]);
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object[] arguments = new Object[1];
  arguments[0] = new String[] { "foo" };
  invocation.setArguments(arguments);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("[SmartNull]", answer.toString());
 }

 @Test
 public void testAnswerWithSmartNullForStaticMethod() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Integer.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(TYPE_INTEGER, "valueOf", new Class[] { TYPE_STRING });
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithFinalReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(TYPE_STRING, "intern", new Class[0]);
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithGenericReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(ArrayList.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(new Type[]{ TYPE_LIST }, "size", new Class[0]);
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals("[SmartNull]", answer.toString());
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithArrayReturnType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(String.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(new Type[] { TYPE_STRING }, "split", new Class[] { TYPE_REGEX });
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object[] arguments = new Object[1];
  arguments[0] = "/";
  invocation.setArguments(arguments);
  Object answer = returnsSmartNulls.answer(invocation);
  assertEquals(new String[] { "[SmartNull]" }, answer.toString().split("\\]|\\["));
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithReturnTypeThatCannotBeImposterised() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Runtime.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(TYPE_LONG, "availableProcessors", new Class[0]);
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithReturnTypeThatCannotBeImposterisedWithArguments() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Math.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  Method method = new Method(TYPE_DOUBLE, "sqrt", new Class[] { TYPE_DOUBLE });
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object[] arguments = new Object[1];
  arguments[0] = 2D;
  invocation.setArguments(arguments);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithReturnTypeThatCannotBeImposterisedWithParameters() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Object mock = mock(Math.class);
  InvocationOnMock invocation = mock(InvocationOnMock.class);
  MethodInterceptor interceptor = mock(MethodInterceptor.class);
  MethodProxy proxy = mock(MethodProxy.class);
  Method method = new Method(TYPE_DOUBLE, "sqrt", new Class[] { TYPE_DOUBLE });
  invocation.setMethod(method);
  invocation.setTarget(mock);
  Object[] arguments = new Object[1];
  arguments[0] = 2D;
  invocation.setArguments(arguments);
  invocation.setProxy(proxy);
  invocation.setInterceptor(interceptor);
  method.setAccessible(true);
  try {
   method.invoke(mock, arguments);
  } catch (Throwable e) {
   assert e.getCause() instanceof NullPointerException;
  }
  invocation.stubReturnValue(mock);
  Object answer = returnsSmartNulls.answer(invocation);
  assertNull(answer);
 }
}