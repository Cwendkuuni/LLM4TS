package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.stubbing.answer.DoesNothing;
import org.mockito.internal.stubbing.answer.Returns;
import org.mockito.internal.stubbing.answer.ThrowsException;
import org.mockito.invocation.Invocation;

import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static org.mockito.mockitoSession.internal.MockitoSession.currentSession;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidate_ReturnsNullAnswer_NoInvocation() {
     AnswersValidator validator = new AnswersValidator();
     validator.validate(null, null);
  }

 @Test
 public void testValidate_ReturnsNullAnswer_InvocationIsVoid() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     validator.validate(null, invocation);
  }

 @Test(expected = IllegalStateException.class)
 public void testValidate_ReturnsNullAnswer_InvocationIsNotVoid() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     validator.validate(null, invocation);
  }

 @Test
 public void testValidate_DoesNothingAnswer() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     DoesNothing answer = new DoesNothing();
     validator.validate(answer, invocation);
  }

 @Test
 public void testValidate_DoesNothingAnswer_InvocationIsVoid() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     DoesNothing answer = new DoesNothing();
     validator.validate(answer, invocation);
     setInternalState(currentSession(), "invocation", invocation);
     assertNull(getInternalState(answer, "invoked"));
  }

 @Test
 public void testValidate_DoesNothingAnswer_InvocationIsNotVoid() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     DoesNothing answer = new DoesNothing();
     validator.validate(answer, invocation);
     setInternalState(currentSession(), "invocation", invocation);
     assertNull(getInternalState(answer, "invoked"));
     getInternalState(answer, "reporter").reportError(reporter, "only void methods can be set to DoNothing");
  }

 @Test
 public void testValidate_ReturnsAnswer() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     Returns answer = new Returns("foo");
     validator.validate(answer, invocation);
  }

 @Test(expected = IllegalStateException.class)
 public void testValidate_ReturnsAnswer_InvocationIsVoid() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     Returns answer = new Returns("foo");
     validator.validate(answer, invocation);
  }

 @Test
 public void testValidate_ReturnsAnswer_ValidReturnType() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     Returns answer = new Returns("foo");
     validator.validate(answer, invocation);
  }

 @Test
 public void testValidate_ReturnsAnswer_InvalidReturnType() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     Returns answer = new Returns(new Object() {}.getClass());
     validator.validate(answer, invocation);
     getInternalState(answer, "reporter").reportError(reporter, "wrong type of return value: %s, %s, %s",
         invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
  }

 @Test
 public void testValidate_ThrowsExceptionAnswer() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     ThrowsException answer = new ThrowsException(new IllegalStateException("Test exception"));
     validator.validate(answer, invocation);
  }

 @Test
 public void testValidate_ThrowsExceptionAnswer_ValidExceptionType() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     ThrowsException answer = new ThrowsException(new IllegalStateException("Test exception"));
     validator.validate(answer, invocation);
  }

 @Test(expected = IllegalStateException.class)
 public void testValidate_ThrowsExceptionAnswer_InvalidExceptionType() {
     AnswersValidator validator = new AnswersValidator();
     Invocation invocation = getDummyInvocation();
     ThrowsException answer = new ThrowsException(new RuntimeException("Test exception"));
     validator.validate(answer, invocation);
  }

 private Invocation getDummyInvocation() {
     return new Invocation(new Object(), new Class[0], new Object[0]);
  }
}