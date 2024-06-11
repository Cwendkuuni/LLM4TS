package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();

 @Test(expected = MockitoException.class)
 public void testThrowsExceptionNullThrowable() {
     Answer<?> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     validator.validate(answer, invocation);
     getInternalState(validator, "reporter").throwNew(new MockitoException("Test message"));
  }

 @Test(expected = MockitoException.class)
 public void testThrowsExceptionInvalidType() {
     Answer<Integer> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(answer.getThrowable()).thenReturn(new RuntimeException("Test message"));
     validator.validate(answer, invocation);
     getInternalState(validator, "reporter").throwNew(new MockitoException("Invalid exception type:" +
             " expected: java.lang.RuntimeException, but was: org.junit.internal.runners.InternalException"));
  }

 @Test(expected = MockitoException.class)
 public void testReturnsNullAndVoidMethod() {
     Answer<Object> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     validator.validate(answer, invocation);
     getInternalState(validator, "reporter").throwNew(new MockitoException("Cannot stub void method with a return value: null"));
  }

 @Test(expected = MockitoException.class)
 public void testReturnsNullAndInvalidType() {
     Answer<Object> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(answer.getReturnValue()).thenReturn(null);
     when(invocation.getReturnType()).thenReturn(String.class);
     validator.validate(answer, invocation);
     getInternalState(validator, "reporter").throwNew(new MockitoException("Wrong type of return value: " +
             "expected: java.lang.String, but was: null"));
  }

 @Test(expected = MockitoException.class)
 public void testReturnsInvalidType() {
     Answer<Object> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(answer.getReturnValue()).thenReturn("Test string");
     when(invocation.getReturnType()).thenReturn(Integer.class);
     validator.validate(answer, invocation);
     getInternalState(validator, "reporter").throwNew(new MockitoException("Wrong type of return value: " +
             "expected: int, but was: java.lang.String"));
  }

 @Test
 public void testDoesNothingVoidMethod() {
     Answer<?> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     validator.validate(answer, invocation);
  }

 @Test
 public void testDoesNothingNonVoidMethod() {
     Answer<?> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     validator.validate(answer, invocation);
  }
}