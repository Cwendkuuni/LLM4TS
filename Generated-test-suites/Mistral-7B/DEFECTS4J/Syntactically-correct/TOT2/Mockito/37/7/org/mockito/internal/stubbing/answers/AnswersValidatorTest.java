package org.junit.jupiter.api.extension.mockito.internal.test.stubbing.answers;

import static org.junit.jupiter.api.Assertions.assertDoesNotReachOuterScope;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.invocation.Invocation;

public class AnswersValidatorTest {

    @Test
    public void testValidateWithValidAnswer() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        Returns returns = mock(Returns.class);

        validator.validate(returns, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateDoNothingWithValidArguments() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        DoesNothing doesNothing = mock(DoesNothing.class);

        validator.validateDoNothing(doesNothing, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateDoNothingWithInvalidArguments() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        DoesNothing doesNothing = mock(DoesNothing.class);

        invocation.stubReturnType("int");

        validator.validateDoNothing(doesNothing, invocation);

        verify(validator).reporter_onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValueWithValidArguments() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        Returns returns = mock(Returns.class);

        validator.validateReturnValue(returns, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateReturnValueWithNullReturnValueAndPrimitiveType() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        Returns returns = mock(Returns.class);
        Returns nullReturns = mock(Returns.class);
        nullReturns.returnsNull();

        invocation.stubReturnType("int");

        validator.validateReturnValue(nullReturns, invocation);

        verify(validator).reporter_wrongTypeOfReturnValue("int", "null", invocation.getMethodName());
    }

    @Test
    public void testValidateExceptionWithValidArguments() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);

        ThrowsException exception = mock(ThrowsException.class);
        exception.setThrowable(new IllegalArgumentException("Test Exception"));

        validator.validateException(throwsException, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateExceptionWithInvalidArguments() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);

        validator.validateException(throwsException, invocation);

        verify(validator, never()).reporter_checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidateExceptionWithRuntimeExceptionOrError() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);

        ThrowsException runtimeException = mock(ThrowsException.class);
        runtimeException.setThrowable(new RuntimeException("Test Runtime Exception"));

        validator.validateException(runtimeException, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateExceptionWithInvalidThrowableType() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);

        ThrowsException exception = mock(ThrowsException.class);
        exception.setThrowable(new AssertionError("Test Assertion Error"));

        validator.validateException(exception, invocation);

        verify(validator).reporter_checkedExceptionInvalid(exception.getThrowable());
    }

    @Test
    public void testValidateAnswerWithNullAnswer() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = mock(Invocation.class);

        validator.validate(null, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateAnswerWithNullInvocation() {
        AnswersValidator validator = new AnswersValidator();
        Answer<?> answer = mock(Answer.class);

        validator.validate(answer, null);

        verify(validator).reporter_invocationCannotBeNull();
    }

    @Test
    public void testValidateAnswerWithNullReporter() {
        AnswersValidator validator = new AnswersValidator();
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);

        validator.setReporter(null);

        validator.validate(answer, invocation);

        verify(validator).reporter_cannotBeNull();
    }

    @Test
    public void testValidateAnswerWithEmptyReporter() {
        AnswersValidator validator = new AnswersValidator();
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);

        validator.setReporter(new Reporter() {});

        validator.validate(answer, invocation);

        // No interaction expected
    }

    @Test
    public void testValidateAnswerWithValidAnswerObjects() {
        AnswersValidator validator = new AnswersValidator();
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);

        validator.validate(answer, invocation);

        // No interaction expected
    }

    @Test
    public void testAnswersValidator() {
        AnswersValidator validator = new AnswersValidator();
        assertDoesNotReachOuterScope(() -> validator.validate(mock(Answer.class), mock(Invocation.class)));
    }
}