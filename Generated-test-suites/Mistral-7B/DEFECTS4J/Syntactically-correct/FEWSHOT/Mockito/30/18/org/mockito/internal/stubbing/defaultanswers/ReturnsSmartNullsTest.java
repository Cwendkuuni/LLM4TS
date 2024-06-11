package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Object mockObject = mock(Object.class);

 @Test
 public void testAnswerNull() {
 Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
 assertNull(result);
 }

 @Test
 public void testAnswerNonMockableType() {
 final int result = returnsSmartNulls.answer(mock(InvocationOnMock.class).withSettings(new Setting().withReturnType(int.class)));
 assertEquals(0, result); // assumes default return value for non-mocked methods is 0
 }

 @Test
 public void testAnswerToStringMethod() {
 String result = (String) returnsSmartNulls.answer(mock(InvocationOnMock.class).withSettings(new Setting().withMethodName("toString")));
 assertEquals("SmartNull returned by unstubbed toString method on mock", result);
 }

 @Test
 public void testThrowsExceptionForToStringMethod() {
 final String expectedMessage = "SmartNull returned by unstubbed toString method on mock";
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);

 try {
 spyReturnsSmartNulls.answer(invocation);
 fail("Expected smartNullPointerException to be thrown");
 } catch (Throwable e) {
 assertEquals(expectedMessage, e.getMessage());
 }

 verify(spyReturnsSmartNulls).new ThrowingInterceptor(invocation);
 }

 @Test
 public void testAnswerObject() {
 Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class).withSettings(new Setting().withArguments(new Object[0])));
 assertNull(result);
 }

 @Test
 public void testAnswerObjectArray() {
 Object[] result = returnsSmartNulls.answer(mock(InvocationOnMock.class).withSettings(new Setting().withArguments(new Object[1])));
 assertNull(result[0]);
 }

 @Test
 public void testAnswerObjectArrayPrimitives() {
 Object[] result = returnsSmartNulls.answer(mock(InvocationOnMock.class).withSettings(new Setting().withArguments(new Object[] { 1, 2, 3 })));
 assertNull(result[0]);
 assertNull(result[1]);
 assertNull(result[2]);
 }
}