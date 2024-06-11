package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateDoNothingNotNullMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[0]);
 DoesNothing answer = new DoesNothing();
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateDoNothingVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[]{Void.TYPE});
 DoesNothing answer = new DoesNothing();
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingInvalidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[]{1}, new Class[]{"int"});
 DoesNothing answer = new DoesNothing();
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueNullReturnAndVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[]{Void.TYPE});
 Returns answer = new Returns(null);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueNullReturnAndNonVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[]{1}, new Class[]{"int"});
 Returns answer = new Returns(null);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueValidReturnAndVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[]{Void.TYPE});
 Returns answer = new Returns("String");
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueInvalidReturnAndVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[]{Void.TYPE});
 Returns answer = new Returns(1);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueValidReturnAndNonVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[]{1}, new Class[]{"int"});
 Returns answer = new Returns(1);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueInvalidReturnAndNonVoidMethod() {
 Invocation invocation = new Invocation("testMethod", new Object[]{1}, new Class[]{"int"});
 Returns answer = new Returns("String");
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateExceptionNullThrowable() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[0]);
 ThrowsException answer = new ThrowsException(null);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateExceptionRuntimeExceptionOrError() {
 Invocation invocation = new Invocation("testMethod", new Object[0], new Class[0]);
 Throwable throwable = new RuntimeException();
 ThrowsException answer = new ThrowsException(throwable);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionInvalidThrowable() {
 Invocation invocation = new Invocation("testMethod", new Object[]{1}, new Class[]{"int"});
 Throwable throwable = new IllegalArgumentException();
 ThrowsException answer = new ThrowsException(throwable);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }
}