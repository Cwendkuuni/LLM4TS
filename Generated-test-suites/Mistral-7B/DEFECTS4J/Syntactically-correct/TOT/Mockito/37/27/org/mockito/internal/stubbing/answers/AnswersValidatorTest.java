package org.mockito.internal.stubbing.answers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();

 @Test
 void testValidateDoNothingWithVoidMethod() {
     Answer<?> answer = mock(Answer.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);
     AnswersValidator validator = spy(new AnswersValidator());
     Invocation invocation = mock(Invocation.class);
     doCallRealMethod().when(validator).validate(answer, invocation);

     validator.validate(answer, invocation);

     verify(validator, times(1)).validateDoNothing((DoesNothing) answer, invocation);
     verifyNoMoreInteractions(validator);
 }

 @Test
 void testValidateDoNothingWithNonVoidMethod() {
     Answer<String> answer = mock(Answer.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);
     AnswersValidator validator = spy(new AnswersValidator());
     Invocation invocation = mock(Invocation.class);
     doCallRealMethod().when(validator).validate(answer, invocation);

     assertThrows(MockitoException.class, () -> validator.validate(answer, invocation));
     verify(validator).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 void testValidateReturnValueWithPrimitiveTypeAndNull() {
     Returns<Integer> answer = mock(Returns.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(null);
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);
     doReturn(Type.INT_TYPE).when(invocation).getReturnType();

     assertThrows(MockitoException.class, () -> validator.validate(answer, invocation));
     verify(validator).wrongTypeOfReturnValue(eq(int.class), eq(null), anyString());
 }

 @Test
 void testValidateReturnValueWithNonPrimitiveTypeAndNull() {
     Returns<String> answer = mock(Returns.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(null);
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);
     doAnswer(invocation1 -> "test").when(invocation).getMethodName();

     assertThrows(MockitoException.class, () -> validator.validate(answer, invocation));
     verify(validator).wrongTypeOfReturnValue(eq("java.lang.String"), eq(null), eq("test"));
 }

 @Test
 void testValidateReturnValueWithValidReturnType() {
     Returns<String> answer = mock(Returns.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn("test");
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);
     doAnswer(invocation1 -> "test").when(invocation).getMethodName();

     validator.validate(answer, invocation);

     verify(validator, times(1)).validateReturnValue((Returns) answer, invocation);
     verifyNoMoreInteractions(validator);
 }

 @Test
 void testValidateReturnValueWithInvalidReturnType() {
     Returns<Integer> answer = mock(Returns.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn("test");
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);
     doReturn(Type.INT_TYPE).when(invocation).getReturnType();

     assertThrows(MockitoException.class, () -> validator.validate(answer, invocation));
     verify(validator).wrongTypeOfReturnValue(eq(int.class), eq("java.lang.String"), eq("test"));
 }

 @Test
 void testValidateExceptionWithNullThrowable() {
     ThrowsException answer = mock(ThrowsException.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(i -> null);
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);

     assertThrows(MockitoException.class, () -> validator.validate(answer, invocation));
     verify(validator).cannotStubWithNullThrowable();
 }

 @Test
 void testValidateExceptionWithRuntimeException() {
     ThrowsException answer = mock(ThrowsException.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(i -> new RuntimeException());
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);

     validator.validate(answer, invocation);

     verify(validator, times(1)).validateException((ThrowsException) answer, invocation);
     verifyNoMoreInteractions(validator);
 }

 @Test
 void testValidateExceptionWithCheckedException() {
     ThrowsException answer = mock(ThrowsException.class);
     when(answer.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(i -> new IOException());
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = mock(Invocation.class);

     assertThrows(MockitoException.class, () -> validator.validate(answer, invocation));
     verify(validator).checkedExceptionInvalid(instanceOf(IOException.class));
 }
}