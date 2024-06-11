import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timer;

    @Before
    public void setup() {
        verificationMode = mock(VerificationMode.class);
        timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 1000L, verificationMode, true, timer);
    }

    @Test
    public void testDelegateVerifiedWithinTimeout() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);

        VerificationData data = mock(VerificationData.class);
        verificationMode.verify(data);

        verificationOverTime.verify(data);

        verify(verificationMode, times(1)).verify(dataCaptor.capture());

        // verify delegate was satisfied within timeout
        InOrder inOrder = inOrder(verificationMode);
        inOrder.verify(verificationMode).verify(dataCaptor.capture());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateNotVerifiedWithinTimeout() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);

        VerificationData data = mock(VerificationData.class);

        doThrow(new RuntimeException("Delegate should not be verified")).when(verificationMode).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 1000L, verificationMode, true, timer);

        verificationOverTime.verify(data);

        // verify delegate was not verified within timeout and exception is thrown
        InOrder inOrder = inOrder(verificationMode);
        inOrder.never(verificationMode).verify(dataCaptor.capture());
        inOrder.verify(verificationMode).verify(dataCaptor.capture());
        inOrder.verify(verificationMode).verify(dataCaptor.capture());
    }

    @Test
    public void testDelegateVerifiedAfterTimeout() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);

        VerificationData data = mock(VerificationData.class);

        when(verificationMode.verify(data)).thenReturn(null);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 2000L, verificationMode, false, timer);

        verificationOverTime.verify(data);

        // verify delegate was not verified within timeout, but satisfied after timeout
        verifyZeroInteractions(verificationMode);
        verify(verificationMode).verify(dataCaptor.capture());
        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void testDelegateNeverVerified() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);

        VerificationData data = mock(VerificationData.class);

        when(verificationMode.verify(data)).thenThrow(new AssertionError("Delegate should never be verified"));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 1000L, verificationMode, false, timer);

        verificationOverTime.verify(data);

        // verify delegate was not verified within timeout and exception is thrown
        verifyZeroInteractions(verificationMode);
        verify(verificationMode).verify(dataCaptor.capture());
        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void testVerifyWithDifferentTimer() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(false);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 1000L, verificationMode, true, timer);

        VerificationData data = mock(VerificationData.class);

        verificationOverTime.verify(data);

        // verify delegate was not called as the timer is not counting
        verifyZeroInteractions(verificationMode);
    }
}