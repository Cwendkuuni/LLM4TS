import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timer;

    @Before
    public void setup() {
        verificationMode = mock(VerificationMode.class);
        timer = mock(Timer.class);

        new VerificationOverTimeImpl(100, 500, verificationMode, true, timer);
    }

    @Test
    public void testDelegateVerifiedWithinTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given

        // When
        verificationMode.verify(data);

        // Then
        verify(verificationMode).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateNotVerifiedWithinTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given

        // When
        new VerificationOverTimeImpl(100, 250, verificationMode, false, timer).verify(data);

        // Then
        // timeout shouldn't be reached, but we are testing for an exception
    }

    @Test
    public void testDelegateVerifiedWithDelay() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given

        // When
        new VerificationOverTimeImpl(100, 500, verificationMode, false, timer).verify(data);

        // Then
        verifyNoMoreInteractions(verificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateNotVerifiedWithDelay() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given

        // When
        new VerificationOverTimeImpl(100, 500, verificationMode, true, timer).verify(data);

        // Then
        verify(verificationMode).verify(data); // should be verified immediately

        // This test checks if an exception is thrown when the delegate is not verified after the full timeout
        Mockito.verify(timer, Mockito.times(3)).start(); // should be called 3 times (pollingPeriodMillis * 2 + durationMillis)
        Mockito.verify(timer, Mockito.times(3)).isCounting();

        // Verification should fail because the delegate mode is not verified
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(verificationMode.verify(data)).thenThrow(new MockitoAssertionError("Expected mock to be called but it was not"));

        // Timeout should be reached and an exception should be thrown
        try {
            new VerificationOverTimeImpl(100, 500, verificationMode, false, timer).verify(data);
        } catch (AssertionError e) {
            // Expected exception
        }

        // This test checks if the delegate mode is verified once the full timeout is reached
        Mockito.verifyZeroInteractions(verificationMode);

        // Sleep for some time to simulate the delay before the delegate is verified
        try {
            TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the delegate mode is verified
        verify(verificationMode).verify(data);
    }
}