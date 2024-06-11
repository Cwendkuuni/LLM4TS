package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidate_NullAnswer() {
 AnswersValidator validator = new AnswersValidator();
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 validator.validate(null, invocation);

 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ThrowsExceptionAnswer_NullInvocation() {
 AnswersValidator validator = new AnswersValidator();
 ThrowsException answer = new ThrowsException(new RuntimeException("Test exception"));

 validator.validate(answer, null);

 Assert.assertEquals("Invocation is null", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ThrowsExceptionAnswer_ValidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 ThrowsException answer = new ThrowsException(new RuntimeException("Test exception"));
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 validator.validate(answer, invocation);

 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ThrowsExceptionAnswer_InvalidThrowableType() {
 AnswersValidator validator = new AnswersValidator();
 ThrowsException answer = new ThrowsException(new Object());
 Invocation invocation = new Invocation(null, null, null, null, null, null);

 validator.validate(answer, invocation);

 Assert.assertEquals("Invalid throwable type: class java.lang.Object", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ReturnsAnswer_VoidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns answer = new Returns(null);
 Invocation invocation = new Invocation(null, null, null, null, null, null, true);

 validator.validate(answer, invocation);

 Assert.assertEquals("Cannot stub void method with a return value", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ReturnsAnswer_NullReturnValue() {
 AnswersValidator validator = new AnswersValidator();
 Returns answer = new Returns(null);
 Invocation invocation = new Invocation(null, null, null, null, null, null, false);

 validator.validate(answer, invocation);

 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ReturnsAnswer_InvalidReturnType() {
 AnswersValidator validator = new AnswersValidator();
 Returns answer = new Returns(new Object());
 Invocation invocation = new Invocation(null, null, Integer.class, null, null, null, false);

 validator.validate(answer, invocation);

 Assert.assertEquals("Wrong type of return value: class java.lang.Object", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_ReturnsAnswer_ValidReturnType() {
 AnswersValidator validator = new AnswersValidator();
 Returns answer = new Returns("Test string");
 Invocation invocation = new Invocation(null, null, String.class, null, null, null, false);

 validator.validate(answer, invocation);

 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidate_DoesNothingAnswer_NonVoidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 DoesNothing answer = new DoesNothing();
 Invocation invocation = new Invocation(null, null, null, null, null, null, false);

 validator.validate(answer, invocation);

 Assert.assertEquals("Only void methods can be set to do:nothing()", validator.reporter.getLastMessage());
 }
}