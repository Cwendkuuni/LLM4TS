package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private Timer timer;
    private VerificationOverTimeImpl verificationOverTime;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationData = mock(VerificationData.class);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);
    }

    @Test
    public void testVerifySuccessWithinDuration() {
        when(delegate.verify(verificationData)).thenReturn(true);

        verificationOverTime.verify(verificationData);

        assertFalse(timer.isCounting());
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testVerifySuccessWithinDurationReturnOnFalse() {
        when(delegate.verify(verificationData)).thenReturn(true);

        verificationOverTime.verify(verificationData);

        assertFalse(timer.isCounting());
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testVerifyFailureWithinDuration() {
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError(""));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(verificationData);
        });

        assertFalse(timer.isCounting());
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testVerifyFailureWithinDurationWaitForFullDuration() {
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError(""));

        verificationOverTime.verify(verificationData);

        assertTrue(timer.isCounting());
        try {
            TimeUnit.MILLISECONDS.sleep(VerificationOverTimeImpl.class.getDeclaredField("durationMillis").getInt(verificationOverTime) + 100L);
        } catch (InterruptedException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testVerifyFailureWithinDurationCannotRecover() {
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError(""));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(verificationData);
        });

        assertFalse(timer.isCounting());
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testDelegateNeverReturns() {
        when(delegate.verify(verificationData)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            verificationOverTime.verify(verificationData);
        });

        assertFalse(timer.isCounting());
        verify(delegate, never()).verify(verificationData);
    }

    @Test
    public void testDelegateVerifyZeroInteractions() {
        when(delegate.verify(verificationData)).thenReturn(null);
        when(verificationData.getMock()).thenReturn(spy(mock(Object.class)));

        verificationOverTime.verify(verificationData);

        assertFalse(timer.isCounting());
        verifyZeroInteractions(verificationData.getMock());
    }

    @Test
    public void testDelegateVerifyNoMoreInteractions() {
        when(delegate.verify(verificationData)).thenReturn(null);
        when(verificationData.getMock()).thenReturn(spy(mock(Object.class)));
        when(verificationData.getMock()).thenReturn(spy(mock(Object.class)));

        verificationOverTime.verify(verificationData);

        assertFalse(timer.isCounting());
        verifyNoMoreInteractions(verificationData.getMock());
    }

    @Test
    public void testDelegateVerifyWithArgumentsAreDifferentException() {
        when(delegate.verify(verificationData)).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(""));

        assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> {
            verificationOverTime.verify(verificationData);
        });

        assertFalse(timer.isCounting());
        verify(delegate).verify(verificationData);
    }
}