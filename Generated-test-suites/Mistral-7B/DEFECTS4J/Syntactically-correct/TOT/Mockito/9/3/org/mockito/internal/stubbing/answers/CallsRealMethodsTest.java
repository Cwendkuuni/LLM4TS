package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

 private Object realObject;
 private CallsRealMethods callsRealMethods;

 @Before
 public void setup() {
 realObject = new Object() {
 public String method1() {
 return "Real Method 1";
 }
 };
 callsRealMethods = new CallsRealMethods();
 }

 @Test
 public void testCallsRealMethod() {
 Object result = callsRealMethods.answer(Mockito.mock(InvocationOnMock.class).withArguments(ArgumentMatchers.any()));
 verify(Mockito.mock(Object.class)).method1(); // Verify real method call
 assertNotNull(result);
 assertEquals("Real Method 1", result.toString());
 }

 @Test(expected = Throwable.class)
 public void testThrowsException() {
 try {
 callsRealMethods.answer(Mockito.mock(InvocationOnMock.class).withArguments(ArgumentMatchers.any()));
 } catch (Throwable e) {
 // Expected exception
 }
 realObject = new Object() {
 @Override
 public void method2() throws Exception {
 throw new RuntimeException("Exception in real method");
 }
 };
 callsRealMethods = new CallsRealMethods();
 callsRealMethods.answer(Mockito.mock(InvocationOnMock.class).withArguments(ArgumentMatchers.any()));
 }

 @Test
 public void testReturnsNull() {
 Object result = callsRealMethods.answer(Mockito.mock(InvocationOnMock.class).withArguments(ArgumentMatchers.any()));
 assertNull(result);
 }
}