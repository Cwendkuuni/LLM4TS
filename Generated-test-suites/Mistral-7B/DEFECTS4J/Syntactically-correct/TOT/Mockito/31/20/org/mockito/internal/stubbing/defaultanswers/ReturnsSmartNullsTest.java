package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private interface TestInterface {
 Object method1();
 String method2();
 }

 @Test
 public void testMethod1ReturnsSmartNull() {
 TestInterface testInterface = mock(TestInterface.class, new ReturnsSmartNulls());

 verify(testInterface).method1(); // this call should return SmartNull

 // verify that no exception was thrown
 }

 @Test(expected = NullPointerException.class)
 public void testMethod2ReturnsNullAndThrowsNPE() {
 TestInterface testInterface = mock(TestInterface.class);

 String result = testInterface.method2(); // this call should return null and throw NPE

 // verify that method was called
 verify(testInterface).method2();
 }

 @Test
 public void testMethod2ReturnsSmartNull() {
 TestInterface testInterface = mock(TestInterface.class, new ReturnsSmartNulls());

 String result = testInterface.method2(); // this call should return SmartNull

 // verify that method was called
 verify(testInterface).method2();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPassingNonMockableTypeToReturnsSmartNulls() {
 TestInterface testInterface = mock(Object[].class, new ReturnsSmartNulls());

 // this should throw an exception as Object[] is not mockable
 }
}