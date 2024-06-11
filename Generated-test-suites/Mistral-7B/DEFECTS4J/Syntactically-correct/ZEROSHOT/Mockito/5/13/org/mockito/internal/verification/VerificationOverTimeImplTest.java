import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timerMock;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 500L;
        VerificationMode delegateMock = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        timerMock = Mockito.mock(Timer.class);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegateMock, returnOnSuccess, timerMock);
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        Mockito.when(timerMock.start()).thenReturn(null);
        Mockito.when(timerMock.stop()).thenReturn(null);
    }

    @Test
    public void testVerifySuccessImmediately() {
        VerificationData data = Mockito.mock(VerificationData.class);

        Mockito.doNothing().when(delegateMock).verify(data);

        verificationOverTime.verify(data);

        Assert.assertTrue(verificationOverTime.isSatisfied());
        Mockito.verify(delegateMock).verify(data);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyFailureWithRecoverableDelegate() {
        VerificationData data = Mockito.mock(VerificationData.class);

        Mockito.doThrow(new AssertionError("Delegate error")).when(delegateMock).verify(data);

        verificationOverTime.verify(data);

        Mockito.verify(delegateMock).verify(data);
        Mockito.verify(timerMock, Mockito.times(1)).start();
        Mockito.verify(timerMock, Mockito.times(1)).stop();
        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyFailureWithNonRecoverableDelegate() {
        VerificationData data = Mockito.mock(VerificationData.class);

        Mockito.doThrow(new AssertionError("Delegate error")).when(delegateMock).verify(data);

        verificationOverTime.verify(data);

        Mockito.verify(delegateMock, Mockito.times(1)).verify(data);
        Mockito.verify(timerMock, Mockito.times(1)).start();
        Mockito.verify(timerMock, Mockito.times(1)).stop();
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Mockito.verify(timerMock, Mockito.times(1)).isCounting();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyFailureAfterTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);

        Mockito.doThrow(new AssertionError("Delegate error")).when(delegateMock).verify(data);

        long timeoutMillis = 100L;
        verificationOverTime = new VerificationOverTimeImpl(10L, timeoutMillis, delegateMock, false, timerMock);

        verificationOverTime.verify(data);

        Mockito.verify(delegateMock, Mockito.never()).verify(data);
        Mockito.verify(timerMock, Mockito.times(timeoutMillis / verificationOverTime.getPollingPeriod()))
                .start();
        Mockito.verify(timerMock, Mockito.times(timeoutMillis / verificationOverTime.getPollingPeriod()))
                .stop();
        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testIsSatisfiedSuccess() {
        VerificationData data = Mockito.mock(VerificationData.class);

        Mockito.doNothing().when(delegateMock).verify(data);

        verificationOverTime.verify(data);

        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testIsSatisfiedFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);

        Mockito.doThrow(new AssertionError("Delegate error")).when(delegateMock).verify(data);

        verificationOverTime.verify(data);

        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

}