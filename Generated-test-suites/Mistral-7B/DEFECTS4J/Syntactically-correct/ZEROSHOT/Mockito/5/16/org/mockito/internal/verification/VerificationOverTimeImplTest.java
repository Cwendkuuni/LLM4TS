package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private long pollingPeriodMillis = 1;
    private long durationMillis = 10;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationMode = new VerificationWithTimeout(pollingPeriodMillis, durationMillis);
        verificationData = mock(VerificationData.class);
    }

    @Test
    public void testVerifySuccessWithShortDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationMode, true);
        verificationOverTime.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifySuccessWithLongDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis * 2, verificationMode, true);
        verificationOverTime.verify(verificationData);
        verify(verificationMode, times(2)).verify(verificationData);
    }

    @Test
    public void testVerifyFailureWithShortDuration() {
        VerificationMode failureVerificationMode = mock(VerificationMode.class);
        Mockito.doThrow(new MockitoAssertionError("Verification failed")).when(failureVerificationMode).verify(ArgumentMatchers.any(VerificationData.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, failureVerificationMode, false);
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerifyFailureWithLongDuration() {
        VerificationMode failureVerificationMode = mock(VerificationMode.class);
        Mockito.doThrow(new MockitoAssertionError("Verification failed")).when(failureVerificationMode).verify(ArgumentMatchers.any(VerificationData.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis * 2, failureVerificationMode, false);
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
        verify(failureVerificationMode, never()).verify(verificationData);
    }

    @Test
    public void testVerifySuccessAndReturnOnSuccess() {
        VerificationMode successVerificationMode = mock(VerificationMode.class);
        Mockito.doReturn(true).when(successVerificationMode).verify(ArgumentMatchers.any(VerificationData.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, successVerificationMode, true);
        verificationOverTime.verify(verificationData);
        verify(successVerificationMode, times(1)).verify(verificationData);
    }

    @Test
    public void testVerifyFailureAndReturnOnSuccess() {
        VerificationMode failureVerificationMode = mock(VerificationMode.class);
        Mockito.doThrow(new MockitoAssertionError("Verification failed")).when(failureVerificationMode).verify(ArgumentMatchers.any(VerificationData.class));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, failureVerificationMode, true);
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
        verify(failureVerificationMode, never()).verify(verificationData);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationMode, false);
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationMode, false);
        assertEquals(durationMillis, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationMode, false);
        assertSame(verificationMode, verificationOverTime.getDelegate());
    }
}