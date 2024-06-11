package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.exceptions.Reporter.cannotStubWithNullAnswer;
import static org.mockito.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.exceptions.Reporter.onlyVoidMethodsCanBeSetToDoNothing;
import static org.mockito.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.exceptions.Reporter.wrongTypeOfReturnValue;
import static org.mockito.internal.invocation.Invocation.invocation;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationWithType;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 AnswersValidator validator = new AnswersValidator();

 @Test
 public void testNullValidator() {
 Assert.assertThrows(NullPointerException.class, () -> validator = null);
 }

 @Test
 public void testNullAnswer() {
 Invocation invocation = invocation(Object.class, new Class[0], new Object[0]);
 Answer<?> nullAnswer = null;

 assertNull(validator.validate(nullAnswer, invocation));
 assertThrows(NullPointerException.class, () -> validator.validate(nullAnswer, invocation));
 }

 @Test
 public void testEmptyInvocation() {
 Invocation invocation = invocation(Object.class, new Class[0], new Object[0]);

 ThrowsException throwsAnswer = new ThrowsException(new RuntimeException("Exception message"));
 validateThrowable(validator, throwsAnswer, invocation);

 Returns returnsAnswer = new Returns("return value");
 validateReturnValue(validator, returnsAnswer, invocation);

 DoesNothing doNothingAnswer = new DoesNothing();
 validateDoNothing(validator, doNothingAnswer, invocation);
 }

 @Test
 public void testValidateExceptionWithValidThrowable() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new RuntimeException("Exception message"));

 validator.validate(throwsAnswer, invocation);
 }

 @Test
 public void testValidateExceptionWithInvalidThrowable() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new Exception("Invalid exception"));

 assertThrows(RuntimeException.class, () -> validator.validate(throwsAnswer, invocation));
 }

 @Test
 public void testValidateExceptionWithRuntimeOrErrorThrowable() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new RuntimeException("Runtime exception"));

 validator.validate(throwsAnswer, invocation);
 }

 @Test
 public void testValidateReturnValueWithValidValueAndVoidMethod() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 Returns returnsAnswer = new Returns(null);

 validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateReturnValueWithNullValueAndPrimitiveMethod() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 Returns returnsAnswer = new Returns(null);

 assertThrows(AssertionError.class, () -> validator.validate(returnsAnswer, invocation));
 }

 @Test
 public void testValidateReturnValueWithNonNullValueAndInvalidType() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 Returns returnsAnswer = new Returns(new Object() {});

 assertThrows(AssertionError.class, () -> validator.validate(returnsAnswer, invocation));
 }

 @Test
 public void testValidateReturnValueWithNonnullValueAndValidType() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 Returns returnsAnswer = new Returns("return value");

 validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateDoNothingWithVoidMethod() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 DoesNothing doNothingAnswer = new DoesNothing();

 validator.validate(doNothingAnswer, invocation);
 }

 @Test
 public void testValidateDoNothingWithNonVoidMethod() {
 InvocationWithType<String, String> invocation = invocation(String.class, new Class[0], new Object[0]);
 DoesNothing doNothingAnswer = new DoesNothing();

 assertThrows(AssertionError.class, () -> validator.validate(doNothingAnswer, invocation));
 }

 private void validateThrowable(AnswersValidator validator, ThrowsException answer, Invocation invocation) {
 ThrowsException throwsAnswer = answer;
 validateAnswer(validator, throwsAnswer, invocation);
 }

 private void validateAnswer(AnswersValidator validator, Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 }

 private void validateReturnValue(AnswersValidator validator, Returns answer, Invocation invocation) {
 Returns returnsAnswer = answer;
 validateAnswer(validator, returnsAnswer, invocation);
 }
}