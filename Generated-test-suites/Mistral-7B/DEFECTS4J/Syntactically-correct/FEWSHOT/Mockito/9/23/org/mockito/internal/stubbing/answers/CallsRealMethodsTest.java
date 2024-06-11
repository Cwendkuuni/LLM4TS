package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.invocation.InvocationOnMock.callRealMethod;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

 @Test
 public void testCallsRealMethods() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 CallsRealMethods answer = new CallsRealMethods();

 // Test null invocation
 assertNull(answer.answer(null));

 // Test no arguments method
 assertEquals("Default value", answer.answer(invocation));

 // Test method with one argument
 Object[] args = new Object[]{"arg1"};
 InvocationOnMock invocationWithArgs = mock(InvocationOnMock.class, args);
 assertEquals("arg1", answer.answer(invocationWithArgs));

 // Test method with multiple arguments
 args = new Object[]{1, "arg2", 3.0};
 InvocationOnMock invocationWithMultipleArgs = mock(InvocationOnMock.class, args);
 assertEquals(3.0, answer.answer(invocationWithMultipleArgs).doubleValue());
 }
}