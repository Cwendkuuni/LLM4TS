import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.internal.invocation.InvocationOnMock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 @Mock
 private Reporter reporter;

 @InjectMocks
 private AnswersValidator validator = new AnswersValidator();

 @Test
 public void testValidateDoNothingWithVoidMethod() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     validator.validate(mock(DoesNothing.class), invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateDoNothingWithNonVoidMethod() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     validator.validate(mock(DoesNothing.class), invocation);
     verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValueWithNullAndPrimitive() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     when(invocation.isValidReturnType(any())).thenReturn(true);
     when(invocation.returnsPrimitive()).thenReturn(true);
     validator.validateReturnValue(mock(Returns.class), invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateReturnValueWithNullAndNonPrimitive() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     when(invocation.isValidReturnType(any())).thenReturn(true);
     when(invocation.returnsPrimitive()).thenReturn(false);
     validator.validateReturnValue(mock(Returns.class), invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateReturnValueWithInvalidType() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     when(invocation.isValidReturnType(eq(String.class))).thenReturn(false);
     validator.validateReturnValue(mock(Returns.class), invocation);
     verify(reporter).wrongTypeOfReturnValue(eq("void"), eq("java.lang.String"), anyString());
 }

 @Test
 public void testValidateExceptionWithNullThrowable() {
     Invocation invocation = mock(InvocationOnMock.class);
     validator.validateException(mock(ThrowsException.class), invocation);
     verify(reporter).cannotStubWithNullThrowable();
 }

 @Test
 public void testValidateExceptionWithValidRuntimeException() {
     Invocation invocation = mock(InvocationOnMock.class);
     ThrowsException exception = mock(ThrowsException.class);
     when(exception.getThrowable()).thenReturn(new RuntimeException("Test exception"));
     validator.validateException(exception, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateExceptionWithInvalidCheckedException() {
     Invocation invocation = mock(InvocationOnMock.class);
     ThrowsException exception = mock(ThrowsException.class);
     when(exception.getThrowable()).thenReturn(new IOException("Test exception"));
     validator.validateException(exception, invocation);
     verify(reporter).checkedExceptionInvalid(instanceOf(IOException.class));
 }
}