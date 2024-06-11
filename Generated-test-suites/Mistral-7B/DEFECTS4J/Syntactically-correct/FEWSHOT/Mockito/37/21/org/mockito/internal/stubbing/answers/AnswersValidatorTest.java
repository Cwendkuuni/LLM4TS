package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateDoNothingWhenMethodIsVoid() {
 Invocation invocation = new Invocation(null, new Object[0], new Class[0]);
 DoesNothing answer = new DoesNothing();
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingWhenMethodIsNotVoid() {
 Invocation invocation = new Invocation(null, new Object[0], int.class);
 DoesNothing answer = new DoesNothing();
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueWhenMethodIsVoid() {
 Invocation invocation = new Invocation(null, new Object[0], void.class);
 Returns answer = new Returns(null);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueWhenMethodIsNotVoidAndAnswerReturnsNull() {
 Invocation invocation = new Invocation(null, new Object[0], int.class);
 Returns answer = new Returns(null);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueWhenMethodIsNotVoidAndAnswerReturnsInvalidType() {
 Invocation invocation = new Invocation(null, new Object[0], String.class);
 Returns answer = new Returns(1);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueWhenMethodIsNotVoidAndAnswerReturnsValidType() {
 Invocation invocation = new Invocation(null, new Object[0], int.class);
 Returns answer = new Returns(1);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateExceptionWhenThrowableIsNotNullAndInstanceOfRuntimeExceptionOrError() {
 Invocation invocation = new Invocation(null, new Object[0], String.class);
 ThrowsException answer = new ThrowsException(new RuntimeException("Exception message"));
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateExceptionWhenThrowableIsNotNullAndIsNotInstanceOfRuntimeExceptionOrError() {
 Invocation invocation = new Invocation(null, new Object[0], String.class);
 ThrowsException answer = new ThrowsException(new Exception("Exception message"));
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWhenThrowableIsNull() {
 Invocation invocation = new Invocation(null, new Object[0], String.class);
 ThrowsException answer = new ThrowsException(null);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }
}