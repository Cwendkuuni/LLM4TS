import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.waitForQuiescence;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationMode verificationOverTime;
    private long pollingPeriodMillis = 10;
    private long durationMillis = 100;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, new Timer(durationMillis));
    }

    @Test
    public void testDelegateIsSatisfiedWithinTimeout() {
        given(delegate.verify(any()))
                .thenReturn(null);

        verificationOverTime.verify(givenVerificationData());

        // Since returnOnSuccess is false, the test will wait for the full timeout before passing
        waitForQuiescence();

        verify(delegate).verify(eq(givenVerificationData()));
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateIsNotSatisfiedWithinTimeout() {
        doThrow(new AssertionError("Delegate verification failed"))
                .when(delegate)
                .verify(any());

        verificationOverTime.verify(givenVerificationData());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateIsNotSatisfiedAndCannotRecover() {
        doThrow(new AssertionError("Delegate verification failed"))
                .when(delegate)
                .verify(any());

        given(delegate)
                .then(invocation -> {
                    throw new AtMost(1);
                });

        verificationOverTime.verify(givenVerificationData());
    }

    @Test
    public void testDelegateIsSatisfiedBeforeTimeout() {
        given(delegate.verify(any()))
                .thenReturn(null);

        doCallRealMethod().when(verificationOverTime).verify(any());

        verificationOverTime.verify(givenVerificationData());

        // Since returnOnSuccess is true, the test will return once the delegate is satisfied
        waitForQuiescence();

        verify(delegate, times(1)).verify(eq(givenVerificationData()));
    }

    @Test(expected = InterruptedException.class)
    public void testSleepInterrupted() {
        given(delegate.verify(any()))
                .thenThrow(new InvocationOnMockException());

        verificationOverTime.verify(givenVerificationData());
    }

    private VerificationData givenVerificationData() {
        return ArgumentMatchers.any(VerificationData.class);
    }
}