package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.junit.Runners;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

 private static class DummyClass {
 void doSomething() {}
 String getSomething() { return null; }
 }

 @Test
 public void testAnswerWithNull() {
 DummyClass dummy = mock(DummyClass.class, new ReturnsSmartNulls());

 verify(dummy).doSomething(); // no interaction here, just checking the mock was created

 // test for normal return value
 when(dummy.getSomething()).thenReturn("expected value");
 assertEquals("expected value", dummy.getSomething());

 // test for null return value
 verify(dummy).doSomething(); // no interaction here, just testing the unstubbed method call
 try {
 dummy.getSomething();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected, as the getSomething method was not stubbed
 assertEquals("SmartNull returned by unstubbed getSomething() method on mock", e.getMessage());
 }
 }

 @Test
 public void testAnswerWithMockableReturnType() {
 DummyClass dummy = mock(DummyClass.class, new ReturnsSmartNulls());

 verify(dummy).doSomething(); // no interaction here, just checking the mock was created

 // test for stubbed method with a mockable return type
 when(dummy.getSomething()).thenReturn(mock(String.class));
 assertEquals(ArgumentMatchers.isInstanceOf(String.class), dummy.getSomething());
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerWithNonMockableFinalReturnType() {
 DummyClass dummy = mock(DummyClass.class, new ReturnsSmartNulls());

 verify(dummy).doSomething(); // no interaction here, just checking the mock was created

 // test for stubbing a final class, which is not mockable
 try {
 when(dummy.getClass()).thenReturn(String.class);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected, as the class is final and not mockable
 }
 }
}