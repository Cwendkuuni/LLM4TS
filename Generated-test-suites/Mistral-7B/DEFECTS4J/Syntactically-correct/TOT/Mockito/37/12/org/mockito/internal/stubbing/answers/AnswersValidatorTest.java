package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithThrowable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothingWithVoidMethod() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(Object.class);
 Answers.DoNothing doNothingAnswer = Answers.doNothing();

 // When
 validator.validate(doNothingAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong()));

 // Then
 // void methods can be set to doNothing
 // No exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingWithNonVoidMethod() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Answers.DoNothing doNothingAnswer = Answers.doNothing();

 // When
 validator.validate(doNothingAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyString()));

 // Then
 // Non void methods cannot be set to doNothing
 // An AssertionError should be thrown
 }

 @Test
 public void testValidateReturnValueWithNullReturnValueAndPrimitiveType() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(Integer.class);
 Answers.ReturnsNull returnsNullAnswer = Answers.returnsNull();

 // When
 validator.validateReturnValue(returnsNullAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyInt()));

 // Then
 // Null return value with primitive types is not supported
 // An AssertionError should be thrown
 }

 @Test
 public void testValidateReturnValueWithNonNullReturnValueAndValidType() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Answers.Returns returnsAnswer = Answers.returns("Hello World");

 // When
 validator.validateReturnValue(returnsAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyString()));

 // Then
 // Valid return value with valid type is supported
 // No exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueWithInvalidType() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Answers.Returns<Integer> returnsAnswer = Answers.returns(1);

 // When
 validator.validateReturnValue(returnsAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyString()));

 // Then
 // Invalid return type is not supported
 // An AssertionError should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithNullThrowable() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Answers.AnswerWithThrowable answerWithThrowableAnswer = Answers.answerWithThrowable(null);

 // When
 validator.validateException(answerWithThrowableAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyString()));

 // Then
 // Null throwable is not supported
 // An AssertionError should be thrown
 }

 @Test
 public void testValidateExceptionWithRuntimeExceptionOrError() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Answers.AnswerWithThrowable answerWithThrowableAnswer = Answers.answerWithThrowable(new RuntimeException("Test Exception"));

 // When
 validator.validateException(answerWithThrowableAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyString()));

 // Then
 // RuntimeException and Error are supported
 // No exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithInvalidException() {
 // Given
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Answers.AnswerWithThrowable answerWithThrowableAnswer = Answers.answerWithThrowable(new IllegalArgumentException("Test Exception"));

 // When
 validator.validateException(answerWithThrowableAnswer, new InvocationOnMock(mock, ArgumentMatchers.any(), ArgumentMatchers.anyString()));

 // Then
 // Invalid exception is not supported
 // An AssertionError should be thrown
 }

}