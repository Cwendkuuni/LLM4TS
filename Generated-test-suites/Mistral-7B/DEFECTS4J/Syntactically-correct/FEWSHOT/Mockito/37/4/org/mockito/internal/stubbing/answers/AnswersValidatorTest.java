package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.stubbing.answer.ThrowsException;
import org.mockito.internal.stubbing.answer.Returns;
import org.mockito.internal.stubbing.answer.DoesNothing;
import org.mockito.invocation.Invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(AnswersValidator.class);

 @Test
 public void testValidate_AnswerIsNull() {
  Invocation invocation = mock(Invocation.class);
  validator.validate(null, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_AnswerIsValid() {
  Invocation invocation = mock(Invocation.class);

  ThrowsException throwsException = mock(ThrowsException.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validate(throwsException, invocation);
  verifyNoMoreInteractions(reporter);

  Returns returns = mock(Returns.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validate(returns, invocation);
  verifyNoMoreInteractions(reporter);

  DoesNothing doesNothing = mock(DoesNothing.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validate(doesNothing, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidate_AnswerThrowsRuntimeException() {
  Invocation invocation = mock(Invocation.class);

  ThrowsException throwsException = mock(ThrowsException.class);
  Throwable runtimeException = new RuntimeException("Test Exception");
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  setFieldValue(ThrowsException.class, "throwable", runtimeException);
  validator.validate(throwsException, invocation);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidate_AnswerThrowsError() {
  Invocation invocation = mock(Invocation.class);

  ThrowsException throwsException = mock(ThrowsException.class);
  Throwable error = new Error("Test Error");
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  setFieldValue(ThrowsException.class, "throwable", error);
  validator.validate(throwsException, invocation);
 }

 @Test
 public void testValidateException_NullThrowable() {
  Invocation invocation = mock(Invocation.class);

  ThrowsException throwsException = mock(ThrowsException.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateException(throwsException, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateException_InvalidExceptionType() {
  Invocation invocation = mock(Invocation.class);

  ThrowsException throwsException = mock(ThrowsException.class);
  Throwable invalidException = new IllegalArgumentException("Test Exception");
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  setFieldValue(ThrowsException.class, "throwable", invalidException);
  validator.validateException(throwsException, invocation);
  reporter.checkedExceptionInvalid(invalidException);
 }

 @Test
 public void testValidateReturnValue_VoidMethod() {
  Invocation invocation = mock(Invocation.class);

  Returns returnsVoid = mock(Returns.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateReturnValue(returnsVoid, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateReturnValue_InvalidReturnTypeForNull() {
  Invocation invocation = mock(Invocation.class);
  setFieldValue(Invocation.class, "isVoid", false);

  Returns returnsNull = mock(Returns.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateReturnValue(returnsNull, invocation);
  reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValue_InvalidReturnTypeForPrimitive() {
  Invocation invocation = mock(Invocation.class);
  setFieldValue(Invocation.class, "isVoid", false);
  setFieldValue(Invocation.class, "isValidReturnType", false);

  Returns returnsString = mock(Returns.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateReturnValue(returnsString, invocation);
  reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), returnsString.printReturnType(), invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValue_ValidReturnType() {
  Invocation invocation = mock(Invocation.class);
  setFieldValue(Invocation.class, "isVoid", false);
  setFieldValue(Invocation.class, "isValidReturnType", true);

  Returns returnsString = mock(Returns.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateReturnValue(returnsString, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateDoNothing_VoidMethod() {
  Invocation invocation = mock(Invocation.class);
  setFieldValue(Invocation.class, "isVoid", true);

  DoesNothing doNothing = mock(DoesNothing.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateDoNothing(doNothing, invocation);
  verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateDoNothing_InvalidMethodType() {
  Invocation invocation = mock(Invocation.class);
  setFieldValue(Invocation.class, "isVoid", false);

  DoesNothing doNothing = mock(DoesNothing.class);
  setFieldValue(AnswersValidator.class, "reporter", reporter);
  validator.validateDoNothing(doNothing, invocation);
  reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }
}