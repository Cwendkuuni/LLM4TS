package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test
 public void validate_nullAnswer_doesNotThrow() {
     Invocation invocation = mock(Invocation.class);
     validator.validate(null, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_answerOfTypeThrowsException_validatesThrowable() {
     Invocation invocation = mock(Invocation.class);
     ThrowsException answer = mock(ThrowsException.class);
     when(answer.getThrowable()).thenReturn(new RuntimeException("Exception message"));

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test
 public void validate_answerOfTypeThrowsException_invalidThrowableType() {
     Invocation invocation = mock(Invocation.class);
     ThrowsException answer = mock(ThrowsException.class);
     when(answer.getThrowable()).thenReturn(new IllegalArgumentException("Exception message"));

     validator.validate(answer, invocation);
     reporter.checkedExceptionInvalid(IllegalArgumentException.class);
     verify(reporter).checkedExceptionInvalid(argument(hasClass(IllegalArgumentException.class)));
 }

 @Test
 public void validate_answerOfTypeReturns_validatesReturnValue() {
     Invocation invocation = mock(Invocation.class);
     when(invocation.isValidReturnType(isA(String.class))).thenReturn(true);

     Returns answer = mock(Returns.class);
     when(answer.getReturnValue()).thenReturn("Return value");

     validator.validate(answer, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_answerOfTypeReturns_invalidReturnType() {
     Invocation invocation = mock(Invocation.class);
     when(invocation.isValidReturnType(isA(Integer.class))).thenReturn(false);

     Returns answer = mock(Returns.class);
     when(answer.getReturnValue()).thenReturn("Return value");

     validator.validate(answer, invocation);
     reporter.wrongTypeOfReturnValue(String.class.getName(), "Return value", invocation.getMethodName());
     verify(reporter).wrongTypeOfReturnValue(argument(eq(String.class.getName())), argument(equalTo("Return value")), argument(eq(invocation)));
 }

 @Test
 public void validate_answerOfTypeReturns_returnsNullAndValidInvocationType() {
     Invocation invocation = mock(Invocation.class);
     when(invocation.isValidReturnType(isA(String[].class))).thenReturn(true);
     when(invocation.returnsPrimitive()).thenReturn(false);

     Returns answer = mock(Returns.class);
     when(answer.returnsNull()).thenReturn(true);

     validator.validate(answer, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_answerOfTypeReturns_returnsNullAndInvalidInvocationType() {
     Invocation invocation = mock(Invocation.class);
     when(invocation.isValidReturnType(isA(int.class))).thenReturn(false);
     when(invocation.returnsPrimitive()).thenReturn(true);

     Returns answer = mock(Returns.class);
     when(answer.returnsNull()).thenReturn(true);

     validator.validate(answer, invocation);
     reporter.cannotStubVoidMethodWithAReturnValue();
     verify(reporter).cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void validate_answerOfTypeDoesNothing_validInvocationType() {
     Invocation invocation = mock(Invocation.class);
     when(invocation.isVoid()).thenReturn(false);

     DoesNothing answer = mock(DoesNothing.class);

     validator.validate(answer, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_answerOfTypeDoesNothing_invalidInvocationType() {
     Invocation invocation = mock(Invocation.class);
     when(invocation.isVoid()).thenReturn(true);

     DoesNothing answer = mock(DoesNothing.class);

     validator.validate(answer, invocation);
     reporter.onlyVoidMethodsCanBeSetToDoNothing();
     verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }
}