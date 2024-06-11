package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateWithNullAnswer() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(null, new Invocation(null, null, null, null, null, null, null, null));
 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateWithNullInvocation() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new ThrowsException(new RuntimeException("test")), null);
 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateWithValidAnswerAndInvocation() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new Returns("test"), new Invocation(new Class[]{int.class}, "test", new Object[]{1}, null, null, null, null, null));
 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionWithNullAnswer() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(null, new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Cannot stub with null answer", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionWithInvalidExceptionType() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new ThrowsException(new Object()), new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Invalid exception type. Expected:java.lang.Object; but was:java.lang.Object", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateExceptionWithValidExceptionType() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new ThrowsException(new RuntimeException("test")), new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueWithNullAnswer() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(null, new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Cannot stub with null answer", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueWithVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new Returns(null), new Invocation(new Class[]{Void.TYPE}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Only void methods can be set to Returns.doNothing()", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueWithNullReturnValueAndPrimitiveType() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new Returns(null), new Invocation(new Class[]{boolean.class}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Invalid return value type for method: test(...) of class: <not set>. Expected:boolean; but was:null", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueWithValidReturnType() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new Returns("test"), new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertNull(validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateReturnValueWithInvalidReturnType() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new Returns(new Object()), new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Invalid return value type for method: test(...) of class: <not set>. Expected:int; but was:java.lang.Object", validator.reporter.getLastMessage());
 }

 @Test
 public void testValidateDoNothingWithNonVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(new DoesNothing(), new Invocation(new Class[]{int.class}, "test", null, null, null, null, null, null));
 Assert.assertEquals("Only void methods can be set to do nothing", validator.reporter.getLastMessage());
 }
}