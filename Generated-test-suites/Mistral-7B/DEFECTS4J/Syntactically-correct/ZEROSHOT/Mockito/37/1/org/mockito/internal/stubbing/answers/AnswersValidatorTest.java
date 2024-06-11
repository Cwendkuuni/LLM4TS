package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 reporter = mock(Reporter.class);
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothing_valid() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, null);
 DoesNothing answer = new DoesNothing();
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_invalid_void() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "voidMethod()");
 DoesNothing answer = new DoesNothing();
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_valid_null_void() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "voidMethod()");
 Returns answer = new Returns(null);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_valid_wrong_type_void() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "voidMethod()");
 Returns answer = new Returns(1L);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_valid_wrong_type_not_void() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, 1L, "getIntMethod()");
 Returns answer = new Returns("test");
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_valid_null_not_void() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "getIntMethod()");
 Returns answer = new Returns(null);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_valid_wrong_type() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, 1L, "getIntMethod()");
 Returns answer = new Returns("test");
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateException_valid() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "throwExceptionMethod()");
 ThrowsException answer = new ThrowsException(new RuntimeException("Test exception"));
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_invalid_null() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "throwExceptionMethod()");
 ThrowsException answer = new ThrowsException(null);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_invalid_not_runtime_exception() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "throwExceptionMethod()");
 ThrowsException answer = new ThrowsException(new Exception("Test exception"));
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_invalid_not_matching_method() {
 Invocation invocation = new InvocationOnMock(mock(Object.class), null, null, null, null, null, "throwExceptionMethod()");
 ThrowsException answer = new ThrowsException(new RuntimeException("Test exception"));
 when(mock(Object.class).getIntMethod()).thenThrow(new RuntimeException("Test exception"));
 validator.validate(answer, invocation);
 }
}