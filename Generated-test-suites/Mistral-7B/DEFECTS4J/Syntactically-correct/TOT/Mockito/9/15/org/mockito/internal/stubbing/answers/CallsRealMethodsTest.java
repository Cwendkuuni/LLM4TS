package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

 private CallsRealMethods callRealMethods;
 @MockitoMock private Object targetObject;

 @Before
 public void setup() {
 callRealMethods = new CallsRealMethods();
 }

 @Capture
 @Captor ArgumentCaptor<String> stringArgumentCaptor;

 @Test
 public void testCallRealMethod_whenCalledWithStringArgument_thenReturnStringFromRealObject() {
 when(targetObject.someMethod(anyString())).thenAnswer(callRealMethods);

 String result = targetObject.someMethod("Test String");

 verify(targetObject).someMethod(eq("Test String"));
 assertThat(result, is("Test String from real object"));
 }

 @Test(expected = RuntimeException.class)
 public void testCallRealMethod_whenCalledWithNullArgument_thenThrowRuntimeException() {
 when(targetObject.someMethod(null)).thenAnswer(callRealMethods);

 try {
 targetObject.someMethod(null);
 } catch (RuntimeException e) {
 // Expected exception
 }

 verify(targetObject).someMethod(null);
 }

 @Test
 public void testCallRealMethod_whenCalledWithoutArguments_thenReturnNullFromRealObject() {
 doNothing().when(targetObject).someMethod();

 Object result = targetObject.someMethod();

 verifyNoMoreInteractions(targetObject);
 assertNull(result);
 }
}