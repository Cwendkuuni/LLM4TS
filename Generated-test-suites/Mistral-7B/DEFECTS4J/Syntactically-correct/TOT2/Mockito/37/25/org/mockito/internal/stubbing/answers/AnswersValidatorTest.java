
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;

public class AnswersValidatorTest {

    @Test
    public void testValidate_ThrowsExceptionAnswer() {
        AnswersValidator validator = new AnswersValidator();
        Invocation invocation = Mockito.mock(Invocation.class);
        ThrowsException answer = Mockito.mock(ThrowsException.class);

        Mockito.doAnswer(answer).when(validator).validate(invocation);

        Throwable throwable = new Exception("Test Exception");
        answer.setThrowable(throwable);

        validator.validate(answer, invocation);

        Mockito.verify(validator).validateException(answer, invocation);
    }

    @Test
    public void testValidateReturnValue_NullReturnAndPrimitiveInvocation() {
        AnswersValidator validator = new AnswersValidator();
        Returns answer = Mockito.mock(Returns.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        Mockito.doAnswer(answer).when(validator).validateReturnValue(invocation);

        validator.validateReturnValue(answer, invocation);

        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(validator.reporter).reportError(errorMessageCaptor.capture());

        String errorMessage = errorMessageCaptor.getValue();
        assert(errorMessage.contains("wrongTypeOfReturnValue"));
    }

    @Test
    public void testValidateDoNothing_NonVoidInvocation() {
        AnswersValidator validator = new AnswersValidator();
        DoesNothing answer = Mockito.mock(DoesNothing.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        Mockito.doAnswer(answer).when(validator).validateDoNothing(invocation);

        validator.validateDoNothing(answer, invocation);

        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(validator.reporter).reportError(errorMessageCaptor.capture());

        String errorMessage = errorMessageCaptor.getValue();
        assert(errorMessage.contains("onlyVoidMethodsCanBeSetToDoNothing"));
    }
}
