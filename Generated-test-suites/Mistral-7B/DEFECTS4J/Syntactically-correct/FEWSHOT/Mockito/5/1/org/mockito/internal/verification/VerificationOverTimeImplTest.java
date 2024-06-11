package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.exceptions.base.MockitoAssertionError.createMockitoAssertionError;
import static org.mockito.internal.verification.api.VerificationData.mockVerificationData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.util.Timer;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationData data;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timer;

    @Rule
    public Timer globalTimer = new Timer(100L);

    @Before
    public void setUp() {
        delegate = mockVerificationMode();
        data = mockVerificationData();
        timer = new Timer(1000L);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);
    }

    @Test
    public void testVerifySucceedsImmediately() {
        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSucceeded());
    }

    @Test
    public void testVerifySucceedsAfterDelay() {
        timer.start();
        verificationOverTime.verify(data);

        // The verification should take at least 100ms to complete, but we'll give it some extra time
        // to make sure it has actually finished.
        globalTimer.advanceTimeBy(200L);

        assertFalse(verificationOverTime.isSucceeded());
        globalTimer.advanceTimeBy(800L); // Wait for another 800ms
        assertTrue(verificationOverTime.isSucceeded());
    }

    @Test
    public void testVerifyFailsImmediately() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(data);
        });

        assertFalse(verificationOverTime.isSucceeded());
    }

    @Test
    public void testVerifyFailsAfterDelayWithRecoverableFailure() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(data);
        });

        assertFalse(verificationOverTime.isSucceeded());

        globalTimer.advanceTimeBy(100L);
        timer.start();
        verificationOverTime.verify(data);

        // The verification should take at least 100ms to complete, but we'll give it some extra time
        // to make sure it has actually finished.
        globalTimer.advanceTimeBy(200L);

        // The verification has failed, but we can recover from this failure
        assertFalse(verificationOverTime.isSucceeded());
        assertNull(verificationOverTime.getError());
    }

    @Test
    public void testVerifyFailsAfterDelayWithNonRecoverableFailure() {
        when(delegate.verify(data)).thenThrow(new AssertionError("Delegate verification failed"));

        assertThrows(AssertionError.class, () -> {
            verificationOverTime.verify(data);
        });

        assertFalse(verificationOverTime.isSucceeded());

        globalTimer.advanceTimeBy(100L);
        timer.start();
        verificationOverTime.verify(data);

        // The verification should take at least 100ms to complete, but we'll give it some extra time
        // to make sure it has actually finished.
        globalTimer.advanceTimeBy(200L);

        // The verification has failed, and this failure is not recoverable
        assertFalse(verificationOverTime.isSucceeded());
        assertNotNull(verificationOverTime.getError());
    }

    @Test
    public void testVerifyTimeoutWithRecoverableFailure() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(data);
        });

        assertFalse(verificationOverTime.isSucceeded());

        // The verification should take at least 100ms to complete, but we'll give it some extra time
        // to make sure it has actually timed out
        globalTimer.advanceTimeBy(1500L);

        // The verification has failed, but we can recover from this failure
        assertFalse(verificationOverTime.isSucceeded());
        assertNull(verificationOverTime.getError());
    }

    @Test
    public void testVerifyTimeoutWithNonRecoverableFailure() {
        when(delegate.verify(data)).thenThrow(new AssertionError("Delegate verification failed"));

        assertThrows(AssertionError.class, () -> {
            verificationOverTime.verify(data);
        });

        assertFalse(verificationOverTime.isSucceeded());

        // The verification should take at least 100ms to complete, but we'll give it some extra time
        // to make sure it has actually timed out
        globalTimer.advanceTimeBy(1500L);

        // The verification has failed, and this failure is not recoverable
        assertFalse(verificationOverTime.isSucceeded());
        assertNotNull(verificationOverTime.getError());
    }

    private VerificationMode mockVerificationMode() {
        return mock(VerificationMode.class);
    }

    private VerificationData mockVerificationData() {
        return mock(VerificationData.class);
    }
}