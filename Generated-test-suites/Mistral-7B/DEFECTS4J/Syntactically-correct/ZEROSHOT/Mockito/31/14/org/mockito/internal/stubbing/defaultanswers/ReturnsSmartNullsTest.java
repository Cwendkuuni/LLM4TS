package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object stub;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 stub = mock(String.class, returnsSmartNulls);
 }

 @Test
 public void testAnswerNull() {
 InvocationOnMock invocation = Mockito.mock(new Class[0].getClass());
 // returnsSmartNulls.answer should return null when delegate returns null
 assertEquals(null, returnsSmartNulls.answer(invocation));

 // verify that no method was called on the stub
 verify(stub).toString();
 }

 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testAnswerToStringMethod() {
 // returnsSmartNulls.answer should return "SmartNull returned by unstubbed  method on mock" when invoked toString method
 String result = (String) returnsSmartNulls.answer(Mockito.mock(String.class, new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException();
 }
 }));

 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test
 public void testAnswerNotMockableType() {
 InvocationOnMock invocation = Mockito.mock(Integer.class.getClass(), new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException();
 }
 });

 // returnsSmartNulls.answer should return null when return type is not mockable
 assertEquals(null, returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerDelegateReturnValue() {
 String expected = "expected";
 InvocationOnMock invocation = Mockito.mock(String.class.getClass(), new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return expected;
 }
 });

 // returnsSmartNulls.answer should return delegate return value when it's not null
 assertEquals(expected, returnsSmartNulls.answer(invocation));
 }
}