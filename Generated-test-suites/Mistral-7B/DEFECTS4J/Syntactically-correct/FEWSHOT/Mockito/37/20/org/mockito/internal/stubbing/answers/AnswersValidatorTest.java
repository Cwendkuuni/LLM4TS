package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test
 public void testValidate_nullAnswerAndInvocation() {
  validator.validate(null, null);
  verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidate_nullAnswerAndValidInvocation() {
  Invocation invocation = mock(Invocation.class);
  validator.validate(null, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_validAnswerAndValidInvocation() {
  Invocation invocation = mock(Invocation.class);
  Answer<String> answer = mock(Answer.class);
  validator.validate(answer, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_answerWithThrowsExceptionAndValidInvocation() {
  Invocation invocation = mock(Invocation.class);
  ThrowsException answer = mock(ThrowsException.class);
  when(answer.getThrowable()).thenReturn(new RuntimeException("Test error message"));
  validator.validate(answer, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_answerWithThrowsExceptionAndInvalidInvocation() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isValidException(any(Throwable.class))).thenReturn(false);
  ThrowsException answer = mock(ThrowsException.class);
  when(answer.getThrowable()).thenReturn(new RuntimeException("Test error message"));
  validator.validate(answer, invocation);
  assertTrue(reporter.hasWarningBeenThrown());
  assertEquals("checked exception invalid: java.lang.RuntimeException: Test error message", reporter.getLatestWarningMessage());
 }

 @Test
 public void testValidate_answerWithReturnsAndValidInvocation() {
  Invocation invocation = mock(Invocation.class);
  Returns answer = mock(Returns.class);
  when(answer.getReturnValue()).thenReturn("Test return value");
  validator.validate(answer, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_answerWithReturnsAndValidVoidInvocation() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isVoid()).thenReturn(true);
  Returns answer = mock(Returns.class);
  validator.validate(answer, invocation);
  reporter.reset();
  validator.validate(answer, invocation);
  assertTrue(reporter.hasWarningBeenThrown());
  assertEquals("cannot stub void method with a return value", reporter.getLatestWarningMessage());
 }

 @Test
 public void testValidate_answerWithReturnsAndInvalidInvocationType() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isValidReturnType(any())).thenReturn(false);
  Returns answer = mock(Returns.class);
  when(answer.getReturnValue()).thenReturn(1);
  validator.validate(answer, invocation);
  assertTrue(reporter.hasWarningBeenThrown());
  assertEquals("wrong type of return value: int, null expected", reporter.getLatestWarningMessage());
 }

 @Test
 public void testValidate_answerWithReturnsAndInvalidReturnType() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isValidReturnType(any())).thenReturn(true);
  Returns answer = mock(Returns.class);
  when(answer.getReturnType()).thenReturn(int[].class);
  validator.validate(answer, invocation);
  assertTrue(reporter.hasWarningBeenThrown());
  assertEquals("wrong type of return value: [I, String expected", reporter.getLatestWarningMessage());
 }

 @Test
 public void testValidate_answerWithDoesNothingAndValidInvocation() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isVoid()).thenReturn(true);
  DoesNothing answer = mock(DoesNothing.class);
  validator.validate(answer, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_answerWithDoesNothingAndInvalidInvocation() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isVoid()).thenReturn(false);
  DoesNothing answer = mock(DoesNothing.class);
  validator.validate(answer, invocation);
  assertTrue(reporter.hasWarningBeenThrown());
  assertEquals("only void methods can be set to DoNothing", reporter.getLatestWarningMessage());
 }

 @Test(expected = InvalidUseOfMatchersException.class)
 public void testValidate_answerWithNullMismatch() {
  Invocation invocation = mock(Invocation.class);
  when(invocation.isValidReturnType(null)).thenReturn(true);
  Returns answer = mock(Returns.class);
  when(answer.getReturnValue()).thenReturn(null);
  validator.validate(answer, invocation);
  assertTrue(reporter.hasWarningBeenThrown());
  assertEquals("checked exception invalid: org.junit.AssertionError: null is not a valid return value", reporter.getLatestWarningMessage());
  assertThrows(InvalidUseOfMatchersException.class, () -> validator.validate(answer, invocation));
 }
}