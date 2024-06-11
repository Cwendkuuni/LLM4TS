package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 private static final String FORMAT_METHOD_CALL = "formatMethodCall";

 @Test
 public void testAnswerNull() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     Object result = returnsSmartNulls.answer(invocation);
     assertNull(result);
 }

 @Test
 public void testAnswerInstanceOfClass() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(invocation.getMethod()).thenReturn(new Method() {
         @SuppressWarnings("unused")
         public Class<?> getReturnType() {
             return String.class;
         }
     });
     String result = (String) returnsSmartNulls.answer(invocation);
     assertNull(result);
 }

 @Test
 public void testAnswerInstanceOfMockableClass() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(invocation.getMethod()).thenReturn(new Method() {
         @SuppressWarnings("unused")
         public Class<?> getReturnType() {
             return Mockito.class;
         }
     });
     Object mock = mock(Mockito.class);
     when(returnsSmartNulls.answer(invocation)).thenReturn(mock);
     assertEquals(mock, returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerToStringMethod() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     Object result = returnsSmartNulls.answer(invocation);
     assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
     verify(returnsSmartNulls).new ThrowingInterceptor(invocation);
 }

 @Test
 public void testNewInterceptor() {
     ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
     assertEquals(invocation, throwingInterceptor.invocation);
 }

 @Test(expected = NullPointerException.class)
 public void testNewInterceptorNullInvocation() {
     ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
     InvocationOnMock invocation = null;
     ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
 }

 @Test
 public void testIsToString() {
     ReturnsMoreEmptyValues returnsMoreEmptyValues = new ReturnsMoreEmptyValues();
     MethodMethodsGuru methodMethodsGuru = new ObjectMethodsGuru();
     Method method = new Method() {
         @SuppressWarnings("unused")
         public Class<?> getReturnType() {
             return String.class;
         }

         @SuppressWarnings("unused")
         public Class<?>[] getParameterTypes() {
             return new Class[0];
         }
     };
     assertEquals(true, methodMethodsGuru.isToString(method));
 }

 @Test
 public void testIsToStringFalse() {
     ReturnsMoreEmptyValues returnsMoreEmptyValues = new ReturnsMoreEmptyValues();
     MethodMethodsGuru methodMethodsGuru = new ObjectMethodsGuru();
     Method method = new Method() {
         @SuppressWarnings("unused")
         public Class<?> getReturnType() {
             return Integer.class;
         }

         @SuppressWarnings("unused")
         public Class<?>[] getParameterTypes() {
             return new Class[0];
         }
     };
     assertEquals(false, methodMethodsGuru.isToString(method));
 }
}