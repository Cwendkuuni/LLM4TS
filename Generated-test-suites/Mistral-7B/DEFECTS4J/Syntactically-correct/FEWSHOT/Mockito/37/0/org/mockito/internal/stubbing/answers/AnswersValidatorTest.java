package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.util.reflection.FieldAccessUtils.writeField;

public class AnswersValidatorTest {

 private Reporter reporter;
 private Invocation invocation;
 private ThrowsException throwsAnswer;
 private Returns returnsAnswer;
 private DoesNothing doesNothingAnswer;

 @Before
 public void setup() {
 reporter = spy(new Reporter());
 invocation = mock(Invocation.class);
 throwsAnswer = mock(ThrowsException.class);
 writeField(AnswersValidator.class, "reporter", reporter);
 returnsAnswer = mock(Returns.class);
 doesNothingAnswer = mock(DoesNothing.class);
 }

 @Test
 public void validate_noAnswer_shouldDoNothing() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(null, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_throwsAnswer_shouldValidateThrowable() {
 writeField(AnswersValidator.class, "reporter", reporter);
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 writeField(AnswersValidator.class, "thowsAnswer", throwsAnswer);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(throwsAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_throwsAnswer_withNullThrowable_shouldReportError() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 writeField(AnswersValidator.class, "thowsAnswer", throwsAnswer);
 writeField(AnswersValidator.class, "reporter", reporter);
 throwableNull(invocation);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(throwsAnswer, invocation);
 reporter.cannotStubWithNullThrowable();
 }

 @Test
 public void validate_throwsAnswer_withInvalidThrowableType_shouldReportError() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 writeField(AnswersValidator.class, "thowsAnswer", throwsAnswer);
 writeField(AnswersValidator.class, "reporter", reporter);
 writeField(Invocation.class, "isValidException", false);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(throwsAnswer, invocation);
 reporter.checkedExceptionInvalid(mock(Throwable.class));
 }

 @Test
 public void validate_returnsAnswer_shouldValidateReturnValue() {
 writeField(AnswersValidator.class, "reporter", reporter);
 Object returnValue = new Object();
 Returns returnsAnswer = mock(Returns.class);
 writeField(AnswersValidator.class, "returnsAnswer", returnsAnswer);
 writeField(Returns.class, "getReturnValue", returnValue);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(returnsAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_returnsAnswer_withVoidMethod_shouldReportError() {
 writeField(AnswersValidator.class, "reporter", reporter);
 Object returnValue = new Object();
 Returns returnsAnswer = mock(Returns.class);
 writeField(AnswersValidator.class, "returnsAnswer", returnsAnswer);
 writeField(Returns.class, "getReturnValue", returnValue);
 writeField(Invocation.class, "isVoid", true);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(returnsAnswer, invocation);
 reporter.cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void validate_returnsAnswer_withNullReturnValue_shouldReportErrorWhenPrimitive() {
 writeField(AnswersValidator.class, "reporter", reporter);
 Object returnValue = null;
 Returns returnsAnswer = mock(Returns.class);
 writeField(AnswersValidator.class, "returnsAnswer", returnsAnswer);
 writeField(Invocation.class, "isValidReturnType", false);
 writeField(Invocation.class, "isPrimitive", true);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(returnsAnswer, invocation);
 reporter.wrongTypeOfReturnValue(IsA.primitiveType(), "null", invokeMethodName());
 }

 @Test
 public void validate_returnsAnswer_withNullReturnValue_shouldNotReportErrorWhenNotPrimitive() {
 writeField(AnswersValidator.class, "reporter", reporter);
 Object returnValue = null;
 Returns returnsAnswer = mock(Returns.class);
 writeField(AnswersValidator.class, "returnsAnswer", returnsAnswer);
 writeField(Invocation.class, "isValidReturnType", true);
 writeField(Invocation.class, "isPrimitive", false);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(returnsAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_returnsAnswer_withInvalidReturnType_shouldReportError() {
 writeField(AnswersValidator.class, "reporter", reporter);
 Object returnValue = new Object();
 Class<String> returnType = String.class;
 Returns returnsAnswer = mock(Returns.class);
 writeField(AnswersValidator.class, "returnsAnswer", returnsAnswer);
 writeField(Returns.class, "getReturnValue", returnValue);
 writeField(Invocation.class, "getMethodName", "testMethod");
 writeField(Invocation.class, "isValidReturnType", false);
 writeField(Invocation.class, "printMethodReturnType", returnType.getName());
 AnswersValidator validator = new AnswersValidator();
 validator.validate(returnsAnswer, invocation);
 reporter.wrongTypeOfReturnValue(returnType.getName(), "returnValue", "testMethod");
 }

 @Test
 public void validate_doesNothingAnswer_shouldValidateMethodReturnVoid() {
 writeField(AnswersValidator.class, "reporter", reporter);
 DoesNothing doesNothingAnswer = mock(DoesNothing.class);
 writeField(AnswersValidator.class, "doesNothingAnswer", doesNothingAnswer);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(doesNothingAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_doesNothingAnswer_shouldReportErrorForNonVoidMethod() {
 writeField(AnswersValidator.class, "reporter", reporter);
 DoesNothing doesNothingAnswer = mock(DoesNothing.class);
 writeField(AnswersValidator.class, "doesNothingAnswer", doesNothingAnswer);
 writeField(Invocation.class, "isVoid", false);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(doesNothingAnswer, invocation);
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }

 private void throwableNull(Invocation invocation) {
 writeField(Invocation.class, "isValidException", false);
}

 private String invokeMethodName() {
 return invocation.getMethodName();
}

}