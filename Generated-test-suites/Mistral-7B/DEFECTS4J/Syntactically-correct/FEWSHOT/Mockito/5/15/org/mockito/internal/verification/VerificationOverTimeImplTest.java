package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.Invocation;
import org.mockito.internal.verification.api.VerificationModeBase;
import org.mockito.internal.verification.api.VerificationModeWithTimeout;

public class VerificationOverTimeImplTest {
    private VerificationMode delegate;
    private VerificationMode overTimeVerification;
    private Timer timer;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        overTimeVerification = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);
    }

    @Test
    public void testVerifySuccessImmediateReturn() {
        VerificationData data = createVerificationData();
        overTimeVerification.verify(data);
        verify(delegate).verify(data);
    }

    @Test
    public void testVerifySuccessDelayedReturn() {
        VerificationData data = createVerificationData();
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        overTimeVerification.verify(data);
        verify(delegate).verify(data);
        verifyNoMoreInteractions(delegate);
        timer.assertStopped();
    }

    @Test
    public void testVerifyFailure() {
        VerificationData data = createVerificationData();

        assertThrows(MockitoAssertionError.class, () -> overTimeVerification.verify(data));
    }

    @Test
    public void testVerifyWithAtMostVerificationMode() {
        VerificationData data = createVerificationData();
        VerificationModeAtMost verificationModeAtMost = mock(VerificationModeAtMost.class);
        overTimeVerification = new VerificationOverTimeImpl(100L, 500L, verificationModeAtMost, false, timer);

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        assertThrows(MockitoAssertionError.class, () -> overTimeVerification.verify(data));
        timer.assertStopped();
    }

    @Test
    public void testVerifyWithNoMoreInteractionsVerificationMode() {
        VerificationData data = createVerificationData();
        VerificationModeNoMoreInteractions verificationModeNoMoreInteractions = mock(VerificationModeNoMoreInteractions.class);
        overTimeVerification = new VerificationOverTimeImpl(100L, 500L, verificationModeNoMoreInteractions, false, timer);

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        assertThrows(MockitoAssertionError.class, () -> overTimeVerification.verify(data));
        timer.assertStopped();
    }

    @Test
    public void testGetDelegate() {
        assertSame(delegate, overTimeVerification.getDelegate());
    }

    @Test
    public void testGetPollingPeriod() {
        assertSame(100L, overTimeVerification.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        assertSame(500L, overTimeVerification.getDuration());
    }

    private VerificationData createVerificationData() {
        return new VerificationData(ArgumentMatchers.any(Invocation.class), ArgumentMatchers.eq(VerificationModeBase.class), ArgumentMatchers.eq(1L));
    }
}

class VerificationModeAtMost extends VerificationModeWithTimeout {
    @Override
    public void verify(VerificationData data) {
        // This verification mode fails intentionally to test the behaviour of VerificationOverTimeImpl
        // when the delegate fails
    }
}

class VerificationModeNoMoreInteractions extends VerificationModeBase {
    @Override
    public void verify(VerificationData data) {
        // This verification mode fails intentionally to test the behaviour of VerificationOverTimeImpl
        // when the delegate fails
    }
}