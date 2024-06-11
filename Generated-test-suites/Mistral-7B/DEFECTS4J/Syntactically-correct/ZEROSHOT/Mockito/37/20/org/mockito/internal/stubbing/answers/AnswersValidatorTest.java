package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class AnswersValidatorTest {
 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setup() {
 reporter = mock(Reporter.class);
 validator = new AnswersValidator();
 validator.reporter = reporter;
 }

 @Test
 public void testValidateDoNothingNullMethod() {
 Invocation invocation = mock(Invocation.class);
 DoesNothing answer = mock(DoesNothing.class);
 validator.validate(answer, invocation);
 verify(reporter).notCalled();
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingNonVoidMethod() {
 Invocation invocation = mock(Invocation.class);
 DoesNothing answer = mock(DoesNothing.class);
 when(invocation.isVoid()).thenReturn(false);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueVoidMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);
 Returns answer = mock(Returns.class);
 validator.validate(answer, invocation);
 verify(reporter).notCalled();
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueVoidMethodWithReturnValue() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);
 Returns answer = mock(Returns.class);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValueNullReturnValueAndPrimitiveMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);
 when(invocation.returnsPrimitive()).thenReturn(true);
 Returns answer = mock(Returns.class);
 validator.validate(answer, invocation);
 verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidateReturnValueNullReturnValueAndNonPrimitiveMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);
 when(invocation.returnsPrimitive()).thenReturn(false);
 Returns answer = mock(Returns.class);
 validator.validate(answer, invocation);
 verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidateReturnValueInvalidTypeReturnValueAndPrimitiveMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);
 when(invocation.returnsPrimitive()).thenReturn(true);
 Returns answer = mock(Returns.class);
 when(answer.getReturnType()).thenReturn(String.class);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue("primitive", answer.printReturnType(), invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValueInvalidTypeReturnValueAndNonPrimitiveMethod() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);
 when(invocation.returnsPrimitive()).thenReturn(false);
 Returns answer = mock(Returns.class);
 when(answer.getReturnType()).thenReturn(String.class);
 validator.validate(answer, invocation);
 verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidateReturnValueValidTypeReturnValue() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);
 when(invocation.returnsPrimitive()).thenReturn(false);
 Returns answer = mock(Returns.class);
 when(answer.getReturnType()).thenReturn(Integer.class);
 validator.validate(answer, invocation);
 verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidateExceptionNullThrowable() {
 Invocation invocation = mock(Invocation.class);
 ThrowsException answer = mock(ThrowsException.class);
 validator.validate(answer, invocation);
 verify(reporter).notCalled();
 }

 @Test
 public void testValidateExceptionRuntimeExceptionOrError() {
 Invocation invocation = mock(Invocation.class);
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new RuntimeException("Test Exception"));
 validator.validate(answer, invocation);
 verify(reporter).notCalled();
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionInvalidException() {
 Invocation invocation = mock(Invocation.class);
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new Object());
 validator.validate(answer, invocation);
 }
}