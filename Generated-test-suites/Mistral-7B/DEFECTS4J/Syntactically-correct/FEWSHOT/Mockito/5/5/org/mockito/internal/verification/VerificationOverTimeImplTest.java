package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationModeFactory verificationModeFactory;
    private Timer timer;
    private VerificationMode delegateVerificationMode;
    private VerificationOverTimeImpl verificationOverTimeImpl;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationModeFactory = mock(VerificationModeFactory.class);
        timer = mock(Timer.class);
        delegateVerificationMode = mock(VerificationMode.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(100L, 1500L, delegateVerificationMode, false, timer);
        verificationData = mock(VerificationData.class);
    }

    @Test
    public void testVerifySucceedsImmediately() {
        when(delegateVerificationMode.verify(verificationData)).thenReturn(null);

        verificationOverTimeImpl.verify(verificationData);

        verify(delegateVerificationMode, times(1)).verify(verificationData);
        verifyNoMoreInteractions(delegateVerificationMode);
    }

    @Test
    public void testVerifySucceedsAfterDelay() {
        when(delegateVerificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        verificationOverTimeImpl.verify(verificationData);

        // The delegate verification failed immediately, but the VerificationOverTimeImpl waits for 1500ms before throwing the exception.
        try {
            Thread.sleep(1499L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        verify(delegateVerificationMode, times(1)).verify(verificationData);
        verifyNoMoreInteractions(delegateVerificationMode);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTimeImpl.verify(verificationData);
        });
    }

    @Test
    public void testVerifyFailsImmediately() {
        when(delegateVerificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTimeImpl.verify(verificationData);
        });

        verify(delegateVerificationMode, never()).verify(verificationData);
        verifyNoMoreInteractions(delegateVerificationMode);
    }

    @Test
    public void testVerifyFailsAfterDelay() {
        when(delegateVerificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        verificationOverTimeImpl.verify(verificationData);

        // The delegate verification failed immediately, but the VerificationOverTimeImpl waits for 1500ms before throwing the exception.
        try {
            Thread.sleep(1499L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        verify(delegateVerificationMode, times(1)).verify(verificationData);
        verifyNoMoreInteractions(delegateVerificationMode);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTimeImpl.verify(verificationData);
        });
    }

    @Test
    public void testVerifyThrowsArgumentsAreDifferentException() {
        when(delegateVerificationMode.verify(verificationData)).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Expected", "Actual"));

        assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> {
            verificationOverTimeImpl.verify(verificationData);
        });

        verify(delegateVerificationMode, never()).verify(verificationData);
        verifyNoMoreInteractions(delegateVerificationMode);
    }

    @Test
    public void testGetters() {
        assertEquals(100L, verificationOverTimeImpl.getPollingPeriod());
        assertEquals(1500L, verificationOverTimeImpl.getDuration());
        assertSame(delegateVerificationMode, verificationOverTimeImpl.getDelegate());
    }

    @Test
    public void testHandleVerifyExceptionWithCanRecoverFromFailureTrue() {
        when(delegateVerificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        AssertionError error = verificationOverTimeImpl.handleVerifyException(new MockitoAssertionError("VerificationOverTimeImpl verification failed"));

        assertNotNull(error);
        assertEquals("Delegate verification failed", error.getMessage());
        verifyNoMoreInteractions(delegateVerificationMode);
    }

    @Test
    public void testHandleVerifyExceptionWithCanRecoverFromFailureFalse() {
        when(delegateVerificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        AssertionError error = verificationOverTimeImpl.handleVerifyException(new MockitoAssertionError("Delegate verification failed"));

        assertSame(error, verificationOverTimeImpl.handleVerifyException(new MockitoAssertionError("VerificationOverTimeImpl verification failed")));
        verify(delegateVerificationMode, never()).verify(verificationData);
    }

    @Test
    public void testSleep() {
        long sleepTime = 500L;
        verificationOverTimeImpl.sleep(sleepTime);

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}