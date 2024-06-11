import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.MethodInterceptor;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private MethodInterceptor interceptor;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 interceptor = mock(MethodInterceptor.class);
 }

 @Test
 public void testDoNothingAnswer() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.doNothing());
 validator.validate(Mockito.doNothing(), new Invocation("testMethod", new Object[0], interceptor));
 verifyNoMoreInteractions(interceptor);
 }

 @Test
 public void testDoNothingAnswerWithVoidMethod() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.doNothing());
 validator.validate(Mockito.doNothing(), new Invocation("testMethodVoid", new Object[0], interceptor));
 assertFalse(validator.hasReporterMessage());
 }

 @Test(expected = MockitoException.class)
 public void testDoNothingAnswerWithNonVoidMethod() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.doNothing());
 validator.validate(Mockito.doNothing(), new Invocation("testMethodReturnInt", new Object[0], interceptor));
 assertTrue(validator.hasReporterMessage());
 assertEquals("Only void methods can be set to do nothing", validator.getReporterMessage());
 }

 @Test
 public void testReturnsAnswer() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.returns("testString"));
 validator.validate(Mockito.returns("testString"), new Invocation("testMethod", new Object[0], interceptor));
 assertFalse(validator.hasReporterMessage());
 }

 @Test
 public void testReturnsAnswerWithVoidMethod() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.returns(null));
 validator.validate(Mockito.returns(null), new Invocation("testMethodVoid", new Object[0], interceptor));
 assertTrue(validator.hasReporterMessage());
 assertEquals("Cannot stub void method with a return value", validator.getReporterMessage());
 }

 @Test
 public void testReturnsAnswerWithInvalidReturnType() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.returns(42L));
 validator.validate(Mockito.returns(42), new Invocation("testMethodStringReturn", new Object[0], interceptor));
 assertTrue(validator.hasReporterMessage());
 assertEquals("Wrong type of return value: expected java.lang.String but: long", validator.getReporterMessage());
 }

 @Test
 public void testReturnsAnswerWithValidReturnType() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.returns("testString"));
 validator.validate(Mockito.returns("testString"), new Invocation("testMethodStringReturn", new Object[0], interceptor));
 assertFalse(validator.hasReporterMessage());
 }

 @Test
 public void testThrowsExceptionAnswer() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.throwException(new RuntimeException("Test Exception")));
 validator.validate(Mockito.throwException(RuntimeException.class), new Invocation("testMethod", new Object[0], interceptor));
 assertFalse(validator.hasReporterMessage());
 }

 @Test
 public void testThrowsExceptionAnswerWithInvalidExceptionType() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.throwException(new IllegalArgumentException()));
 validator.validate(Mockito.throwException(RuntimeException.class), new Invocation("testMethod", new Object[0], interceptor));
 assertTrue(validator.hasReporterMessage());
 assertEquals("Checked exception invalid: expected java.lang.RuntimeException but: java.lang.IllegalArgumentException", validator.getReporterMessage());
 }

 @Test
 public void testThrowsExceptionAnswerWithValidExceptionType() {
 given(interceptor.getMethod(ArgumentMatchers.any())).willAnswer(Mockito.throwException(new RuntimeException("Test Exception")));
 validator.validate(Mockito.throwException(RuntimeException.class), new Invocation("testMethod", new Object[0], interceptor));
 assertFalse(validator.hasReporterMessage());
 }

 @Test(expected = NullPointerException.class)
 public void testValidateWithNullAnswer() {
 validator.validate(null, new Invocation("testMethod", new Object[0], interceptor));
 assertTrue(validator.hasReporterMessage());
 assertEquals("Answer cannot be null", validator.getReporterMessage());
 }

 @Test(expected = NullPointerException.class)
 public void testValidateWithNullInvocation() {
 validator.validate(Mockito.returns("testString"), null);
 assertTrue(validator.hasReporterMessage());
 assertEquals("Invocation cannot be null", validator.getReporterMessage());
 }
}