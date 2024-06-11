package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeWithSuccess() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, true);
        VerificationData data = mock(VerificationData.class);

        when(delegate.verify(data)).thenReturn(null);

        verificationOverTime.verify(data);

        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationOverTimeWithFailureAndCanRecover() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, true);
        VerificationData data = mock(VerificationData.class);

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Error"));

        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            // expected
        }

        Thread.sleep(150L);

        verificationOverTime.verify(data);

        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationOverTimeWithFailureAndCannotRecover() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);
        VerificationData data = mock(VerificationData.class);

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Error"));

        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            // expected
        }

        Thread.sleep(300L);

        verificationOverTime.verify(data);

        // this should be thrown
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationOverTimeWithTimeout() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, delegate, false);
        VerificationData data = mock(VerificationData.class);

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Error"));

        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            // expected
        }

        Thread.sleep(100L);

        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            // expected
        }

        Thread.sleep(390L);

        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            throw e;
        }

        verify(delegate, never()).verify(data);
    }

    @Test
    public void testVerificationOverTimeWithNoInteractions() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);
        VerificationData data = mock(VerificationData.class);

        when(delegate.verify(data)).thenReturn(null);
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Error"));

        try {
            verificationOverTime.verify(data);
            fail();
        } catch (AssertionError e) {
            // expected
        }

        verify(delegate, never()).verify(data);
    }

    @Test
    public void testVerificationOverTimeWithMultipleInteractions() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);
        VerificationData data = mock(VerificationData.class);

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Error"));
        when(delegate.verify(data)).thenReturn(null);

        verificationOverTime.verify(data);

        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationOverTimeWithInterleavedInteractions() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);
        VerificationData data1 = mock(VerificationData.class);
        VerificationData data2 = mock(VerificationData.class);

        when(delegate.verify(data1)).thenReturn(null);
        when(delegate.verify(data2)).thenThrow(new MockitoAssertionError("Error"));

        verificationOverTime.verify(data1);
        verificationOverTime.verify(data2);

        verify(delegate, times(1)).verify(data1);
        verify(delegate, times(1)).verify(data2);
    }

    @Test
    public void testGetters() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);

        assertEquals(100L, verificationOverTime.getPollingPeriod());
        assertEquals(200L, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
    }
}