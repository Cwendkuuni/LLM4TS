package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerCallable;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.Throws;

import static org.mockito.Mockito.*;
import static org.mockito.internal.stubbing.answers.AnswersValidator.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.internal.stubbing.answers.AnswersValidator.Reporter.onlyVoidMethodsCanBeSetToDoNothing;
import static org.mockito.internal.stubbing.answers.AnswersValidator.Reporter.wrongTypeOfReturnValue;
import static org.mockito.internal.stubbing.answers.AnswersValidator.Reporter.checkedExceptionInvalid;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingVoidMethod() {
     AnswerStub answer = answer(new DoesNothing());
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("voidMethod", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
     onlyVoidMethodsCanBeSetToDoNothing.match(invocation);
 }

 @Test
 public void testValidateDoNothingValidMethod() {
     AnswerStub answer = answer(new DoesNothing());
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("validate", ArgumentMatchers.any());
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueVoidMethod() {
     AnswerCallable<String> answer = answer("returnNullValue");
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("voidMethod", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
     cannotStubVoidMethodWithAReturnValue.match(invocation);
 }

 @Test
 public void testValidateReturnValuePrimitiveType() {
     Answer<Integer> answer = answer(1);
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("methodReturningPrimitiveType", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueNullAndPrimitiveType() {
     Returns<Integer> answer = returns(null);
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("methodReturningPrimitiveType", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
     wrongTypeOfReturnValue.match(invocation.printMethodReturnType(), "null", invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValueValidType() {
     Returns<String> answer = returns("testString");
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("methodReturningString", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionNullThrowable() {
     Throws<RuntimeException> answer = throwsException(null);
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("methodThrowingNullException", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
     cannotStubWithNullThrowable.match(invocation);
 }

 @Test
 public void testValidateExceptionValidRuntimeException() {
     Throws<RuntimeException> answer = throwsException(new RuntimeException("testException"));
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("methodThrowingRuntimeException", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionInvalidException() {
     Throws<NumberFormatException> answer = throwsException(new IllegalArgumentException("testIllegalArgumentException"));
     InvocationOnMock invocation = mock(Object.class).invocationOnMethod("methodThrowingNumberFormatException", ArgumentMatchers.anyInt());
     validator.validate(answer, invocation);
     checkedExceptionInvalid.match(invocation, NumberFormatException.class);
 }
}