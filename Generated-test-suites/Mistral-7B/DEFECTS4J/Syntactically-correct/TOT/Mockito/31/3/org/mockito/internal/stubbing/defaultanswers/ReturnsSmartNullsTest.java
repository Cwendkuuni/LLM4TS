package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 class Testee {
 private Answer<Object> answer = mock(Answer.class);

 public Object methodReturningNull(ReturnsSmartNulls returnsSmartNulls) {
 returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class));
 return null;
 }
 }

 @Test
 public void testMethodReturnsSmartNull() {
 Testee testee = new Testee();
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(returnsSmartNulls);

 testee.methodReturningNull(answer);

 verify(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

 // Verify the exception message contains the name of the unstubbed method
 Assert.assertEquals("SmartNull returned by unstubbed methodReturningNull() method on mock",  // NOSONAR
 throwableWithMessage("SmartNull returned by unstubbed methodReturningNull method on mock").getMessage());
 }

 @Test(expected = AssertionError.class)
 public void testMethodReturnsNull() {
 Testee testee = new Testee();

 testee.methodReturningNull(null);
 }

 @Test
 public void testMethodReturnsNonMockableType() {
 Testee testee = new Testee();
 when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(new Object());

 testee.methodReturningNull(answer);
 }
}