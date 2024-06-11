import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyMethod() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationData data = new VerificationData("mockMethod", ArgumentMatchers.any(String.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);

        verificationOverTime.verify(data);
        Mockito.verify(delegate).verify(data);
    }

    @Test
    public void testUnsatisfiedDelegateWithinTimeout() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationData data = new VerificationData("mockMethod", ArgumentMatchers.any(String.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 50L, delegate, true);

        try {
            verificationOverTime.verify(data);
            assertThrows(MockitoAssertionError.class, () -> delegate.verify(data));
        } catch (MockitoAssertionError e) {
            assertEquals("Expected: verification < VerificationData(methodName='mockMethod', arguments=[...]) > "
                    + "but it was not called.", e.getMessage());
        }
    }

    @Test
    public void testSatisfiedDelegateBeforeTimeout() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationData data = new VerificationData("mockMethod", ArgumentMatchers.any(String.class));
        Mockito.doReturn(true).when(delegate).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 50L, delegate, true);
        verificationOverTime.verify(data);

        Mockito.verifyNoMoreInteractions(delegate);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateThrowsException() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationData data = new VerificationData("mockMethod", ArgumentMatchers.any(String.class));
        Mockito.doThrow(new RuntimeException()).when(delegate).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);
        verificationOverTime.verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerifyConditionsNotMet() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationData data = new VerificationData("mockMethod", ArgumentMatchers.any(String.class));
        VerificationData data2 = new VerificationData("mockMethod", "differentArgument");

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);
        verificationOverTime.verify(data);
        verificationOverTime.verify(data2);
    }

    @Test
    public void testDelegateCannotRecoverFromFailure() {
        VerificationMode delegate = Mockito.mock(AtMost.class);
        VerificationData data = new VerificationData("mockMethod", ArgumentMatchers.any(String.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);

        Mockito.doThrow(new RuntimeException()).when(delegate).verify(data);
        verificationOverTime.verify(data);

        try {
            verificationOverTime.verify(data);
            assertThrows(RuntimeException.class, () -> delegate.verify(data));
        } catch (RuntimeException e) {
            // expected exception
        }
    }
}