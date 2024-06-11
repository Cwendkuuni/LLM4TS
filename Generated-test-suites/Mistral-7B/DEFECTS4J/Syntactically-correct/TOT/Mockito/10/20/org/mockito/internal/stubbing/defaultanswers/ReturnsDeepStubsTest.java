package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito. ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 // Test for the answer method of ReturnsDeepStubs class
 @Test
 public void testAnswerMethod() {
 InterfaceMock mock = Mockito.mock(InterfaceMock.class, new Answers.ReturnsDeepStubs());
 Number number = mock.getNestedNumber();

 // Test when the invocation matches the stubbed answer
 when(mock.getNestedNumber()).thenReturn(new Number() {
 @Override
 public int intValue() {
 return 1;
 }
 });

 assertEquals(1, number.intValue());
 verify(mock).getNestedNumber();

 // Test when the invocation doesn't match the stubbed answer
 Number nestedNumber = mock.getNestedNumber().iterator().next().getValue().iterator().next();
 assertEquals(2, nestedNumber.intValue());
 verify(mock, Mockito.never()).getNestedNumber();
 }

 // Test for the deepStub method of ReturnsDeepStubs class
 @Test
 public void testDeepStubMethod() throws Exception {
 InterfaceMock mock = Mockito.mock(InterfaceMock.class, new Answers.ReturnsDeepStubs());
 InterfaceMock nestedMock = Mockito.mock(InterfaceMock.class, new Answers.ReturnsDeepStubs());
 when(mock.getNestedNumber()).thenReturn(nestedMock);

 Number number = mock.getNestedNumber().iterator().next().getValue().iterator().next();
 Number nestedNumber = number.iterator().next();

 // Test when the invocation matches the stubbed answer
 when(nestedMock.getIntValue()).thenReturn(1);

 assertEquals(1, number.intValue());
 verify(nestedMock).getIntValue();

 // Test when the invocation doesn't match the stubbed answer
 assertEquals(2, nestedNumber.intValue());
 verify(nestedMock, Mockito.never()).getIntValue();
 }

 // Test for the exception handling in the answer method of ReturnsDeepStubs class
 @Test(expected = UnsupportedOperationException.class)
 public void testExceptionHandling() throws Exception {
 InterfaceMock mock = Mockito.mock(InterfaceMock.class, new Answers.ReturnsDeepStubs());

 when(mock.getUnsupportedMethod()).thenThrow(new UnsupportedOperationException());

 mock.getUnsupportedMethod();
 }
}

interface InterfaceMock {
 Number getNestedNumber();
 Number getUnsupportedMethod();
}