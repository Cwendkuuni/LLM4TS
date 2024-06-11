package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateDoNothingNullInvocation() {
 validateDoNothing((DoesNothing) mock(Answer.class), null);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Invocation cannot be null", reporter.getLastMessage());
 }

 @Test
 public void testValidateDoNothingVoidMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);

 DoesNothing answer = mock(DoesNothing.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 assertNull(reporter.getLastMessage());
 }

 @Test
 public void testValidateDoNothingNonVoidMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);

 DoesNothing answer = mock(DoesNothing.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Only void methods can be set to DoNothing", reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueNullAnswer() {
 validateReturnValue(null, mock(Invocation.class));
 assertNotNull(reporter.getLastMessage());
 assertEquals("Answer cannot be null", reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueVoidMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);

 Returns answer = mock(Returns.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validateReturnValue(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Cannot stub void method with a return value", reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValuePrimitiveTypeNullReturn() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.returnsPrimitive()).thenReturn(true);

 Returns answer = mock(Returns.class);
 answer.returnsNull();
 AnswersValidator validator = new AnswersValidator();
 validator.validateReturnValue(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Wrong type of return value for primitive type method. Expected null but was: null", reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueStringNullReturn() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(String.class)).thenReturn(true);

 Returns answer = mock(Returns.class);
 answer.returnsNull();
 AnswersValidator validator = new AnswersValidator();
 validator.validateReturnValue(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Wrong type of return value. Expected type: java.lang.String, but was: null", reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueMismatchedTypes() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(Integer.class)).thenReturn(false);

 Returns answer = mock(Returns.class);
 answer.getReturnType().equals(Double.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validateReturnValue(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Wrong type of return value. Expected type: java.lang.Integer, but was: java.lang.Double", reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionNullAnswer() {
 validateException(null, mock(Invocation.class));
 assertNotNull(reporter.getLastMessage());
 assertEquals("Answer cannot be null", reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionNullThrowable() {
 ThrowsException answer = mock(ThrowsException.class);
 answer.getThrowable(null);
 Invocation invocation = mock(Invocation.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validateException(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Cannot stub with null throwable", reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionRuntimeExceptionOrError() {
 ThrowsException answer = mock(ThrowsException.class);
 Throwable throwable = new RuntimeException("Test exception");
 answer.getThrowable().equals(throwable);
 Invocation invocation = mock(Invocation.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validateException(answer, invocation);
 assertNull(reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionInvalidExceptionType() {
 ThrowsException answer = mock(ThrowsException.class);
 Throwable throwable = new IllegalArgumentException("Test exception");
 Invocation invocation = mock(Invocation.class);
 when(invocation.isValidException(throwable)).thenReturn(false);
 AnswersValidator validator = new AnswersValidator();
 validator.validateException(answer, invocation);
 assertNotNull(reporter.getLastMessage());
 assertEquals("Checked exception invalid: java.lang.IllegalArgumentException", reporter.getLastMessage());
 }
}