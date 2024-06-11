import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 reporter = mock(Reporter.class);
 validator = new AnswersValidator();
 validator.reporter = reporter;
 }

 @Test
 public void testValidateDoNothingWhenMethodIsVoid() {
 DoesNothing answer = Mockito.mock(DoesNothing.class);
 Invocation invocation = new Invocation(new Object(), new Object()[0], new Object[0], 0, null);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateDoNothingWhenMethodIsNotVoid() {
 DoesNothing answer = Mockito.mock(DoesNothing.class);
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, null);
 invocation.setReturnType(String.class);
 validator.validate(answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValueWhenMethodIsVoid() {
 Returns answer = Mockito.mock(Returns.class);
 Invocation invocation = new Invocation(new Object(), new Object()[0], new Object[0], 0, null);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateReturnValueWhenMethodIsNotVoidAndReturnNull() {
 Returns answer = Mockito.mock(Returns.class);
 answer.returnsNull();
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, null);
 invocation.setReturnType(String.class);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateReturnValueWhenMethodIsNotVoidAndReturnWrongType() {
 Returns answer = Mockito.mock(Returns.class);
 answer.returns(1);
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, null);
 invocation.setReturnType(Integer.class);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValueWhenMethodIsNotVoidAndReturnRightType() {
 Returns answer = Mockito.mock(Returns.class);
 answer.returns(1);
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, null);
 invocation.setReturnType(Integer.class);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateExceptionWhenExceptionIsRuntimeOrError() {
 ThrowsException answer = Mockito.mock(ThrowsException.class);
 Throwable throwable = new RuntimeException();
 answer.getThrowable(throwable);
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, throwable);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateExceptionWhenExceptionIsNotRuntimeOrError() {
 ThrowsException answer = Mockito.mock(ThrowsException.class);
 Throwable throwable = new Exception();
 answer.getThrowable(throwable);
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, throwable);
 validator.validate(answer, invocation);
 verify(reporter).checkedExceptionInvalid(throwable);
 }

 @Test
 public void testValidateExceptionWhenExceptionIsNotThrown() {
 ThrowsException answer = Mockito.mock(ThrowsException.class);
 Invocation invocation = new Invocation(new Object(), new Object[0], new Object[0], 0, null);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }
}