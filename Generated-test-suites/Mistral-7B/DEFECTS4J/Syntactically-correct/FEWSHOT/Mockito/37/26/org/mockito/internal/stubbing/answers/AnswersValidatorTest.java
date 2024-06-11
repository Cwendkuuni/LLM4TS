package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateNullAnswerAndInvocation() {
 Answer<?> answer = null;
 Invocation invocation = null;

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsExceptionAnswerAndInvocation() {
 Throwable expectedThrowable = new IllegalStateException("Test Exception");
 Answer<Void> answer = new ThrowsException(expectedThrowable);
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsNullAnswerAndVoidInvocation() {
 Answer<Void> answer = new Returns(null);
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsNullAnswerAndNonVoidInvocation() {
 Answer<Integer> answer = new Returns(null);
 Invocation invocation = new Invocation(null, null, null, null, null, 1);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);

 Assert.assertEquals("Void methods can only be set to do nothing", reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnsAnswerAndVoidInvocation() {
 Answer<Integer> answer = new Returns(1);
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerAndNonVoidInvocation() {
 Answer<Integer> answer = new Returns(1);
 Invocation invocation = new Invocation(null, null, null, null, null, 1);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerAndNonValidReturnType() {
 Answer<String> answer = new Returns("abc");
 Invocation invocation = new Invocation(String.class, null, null, null, null, 1);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);

 Assert.assertEquals("Returned value " + answer.getReturnType() + " is not compatible with the method return type " + invocation.getMethodReturnType(), reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnsAnswerAndValidReturnType() {
 Answer<Integer> answer = new Returns(1);
 Invocation invocation = new Invocation(Integer.class, null, null, null, null, null);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsExceptionAnswerAndInvalidException() {
 Throwable expectedThrowable = new IllegalArgumentException("Test Exception");
 Answer<Void> answer = new ThrowsException(expectedThrowable);
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);

 Assert.assertEquals("Exception " + expectedThrowable.getClass().getName() + " is not a valid exception", reporter.getLastMessage());
 }

 @Test
 public void testValidateThrowsExceptionAnswerAndValidException() {
 Throwable expectedThrowable = new RuntimeException("Test Exception");
 Answer<Void> answer = new ThrowsException(expectedThrowable);
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }
}