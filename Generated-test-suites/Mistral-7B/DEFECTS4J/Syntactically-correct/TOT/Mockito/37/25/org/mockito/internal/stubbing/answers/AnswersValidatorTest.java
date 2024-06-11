import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*; // For BDD style testing

import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.invocation.Invocation;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothing_voidMethod() {
 given(mock(Answer.class)).willReturn(mock(DoesNothing.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[]{String.class}, new Object[]{ ArgumentMatchers.anyString() }, new Object[0]);
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[0], null), invocation);
 then(validator).should().validateDoNothing((DoesNothing) AnswerMockitoHelper.createAnswer(new Class[0], DoesNothing.class), invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateDoNothing_nonVoidMethod() {
 given(mock(Answer.class)).willReturn(mock(DoesNothing.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[]{String.class, Integer.TYPE}, new Object[]{ ArgumentMatchers.anyString(), 42 }, new Object[0]);
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[]{Integer.TYPE}, null), invocation);
 then(validator).should().reporter().onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnValue_voidMethod() {
 given(mock(Answer.class)).willReturn(mock(Returns.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[0], null, null);
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[0], String.class), invocation);
 then(validator).should().reporter().cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidateReturnValue_validReturnValue() {
 given(mock(Answer.class)).willReturn(mock(Returns.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[]{String.class}, null, "Test String");
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[]{String.class}, "Test String"), invocation);
 then(validator).should().validateReturnValue((Returns) AnswerMockitoHelper.createAnswer(new Class[]{String.class}, "Test String".getClass()), invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnValue_invalidReturnValueType() {
 given(mock(Answer.class)).willReturn(mock(Returns.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[]{String.class}, null, 42);
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[]{Integer.TYPE}, null), invocation);
 then(validator).should().reporter().wrongTypeOfReturnValue(invocation.printMethodReturnType(), Returns.class.getName(), invocation.getMethodName());
 }

 @Test
 public void testValidateException_nullThrowable() {
 given(mock(Answer.class)).willReturn(mock(ThrowsException.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[0], null, null);
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[0], null), invocation);
 then(validator).should().validateException((ThrowsException) AnswerMockitoHelper.createAnswer(new Class[0], null.getClass()), invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateException_invalidThrowable() {
 given(mock(Answer.class)).willReturn(mock(ThrowsException.class));
 Invocation invocation = new Invocation(mock(Class.class), "testMethod", new Class[0], null, new RuntimeException("Test Exception"));
 validator.validate(AnswerMockitoHelper.createAnswer(new Class[0], new IllegalArgumentException("Test IllegalArgumentException")), invocation);
 then(validator).should().reporter().checkedExceptionInvalid(new IllegalArgumentException("Test IllegalArgumentException"));
 }

}

class AnswerMockitoHelper {
 static Answer createAnswer(Class<?> returnType, Object returnValue) {
 return mock(Answer.class, new AnswerStub(returnType, returnValue));
 }
}

class AnswerStub implements Answer<Object> {
 private final Class<?> returnType;
 private final Object returnValue;

 public AnswerStub(Class<?> returnType, Object returnValue) {
 this.returnType = returnType;
 this.returnValue = returnValue;
 }

 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 if (returnValue == null) {
 return null;
 } else {
 return returnValue;
 }
 }

 @Override
 public Class<?> getReturnType() {
 return returnType;
 }
}