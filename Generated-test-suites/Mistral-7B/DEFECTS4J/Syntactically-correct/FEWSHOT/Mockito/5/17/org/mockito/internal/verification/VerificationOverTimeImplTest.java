package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    private static final long POLLING_PERIOD_MILLIS = 10;
    private static final long DURATION_MILLIS = 200;
    private static final long TIMEOUT_MILLIS = 250;

    private VerificationMode delegate;
    private Timer timer;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = spy(new Timer(DURATION_MILLIS));
        verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, delegate, false, timer);
    }

    @Test
    public void testVerifySucceedsImmediately() {
        // Given: The delegate verification mode is satisfied immediately
        verify(delegate, times(1)).verify(any());

        // When: The verificationOverTime is used to verify the data
        verificationOverTime.verify(mock(VerificationData.class));

        // Then: The verificationOverTime is also satisfied immediately
        verify(delegate, times(1)).verify(any());
        assertTrue(verificationOverTime.getDelegate().isSatisfied());
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifySucceedsAfterTimeout() {
        // Given: The delegate verification mode is not satisfied initially
        verifyZeroInteractions(delegate);

        // When: The verificationOverTime is used to verify the data
        try {
            verificationOverTime.verify(mock(VerificationData.class));
        } catch (MockitoAssertionError e) {
            // Then: The verificationOverTime waits for the full timeout before throwing the error
            timer.advanceTimeBy(TIMEOUT_MILLIS);
            verify(delegate, times(1)).verify(any());
            assertTrue(verificationOverTime.getDelegate().isSatisfied());
            assertTrue(verificationOverTime.isSatisfied());
            throw e;
        }
    }

    @Test
    public void testVerifyFailsImmediately() {
        // Given: The delegate verification mode fails immediately
        doThrow(new AssertionError()).when(delegate).verify(any());

        // When: The verificationOverTime is used to verify the data
        assertThrows(AssertionError.class, () -> {
            verificationOverTime.verify(mock(VerificationData.class));
        });

        // Then: The verificationOverTime also fails immediately
        verify(delegate, never()).verify(any());
        assertFalse(verificationOverTime.getDelegate().isSatisfied());
        assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyFailsAfterTimeout() {
        // Given: The delegate verification mode is not satisfied initially and cannot recover from the failure
        doThrow(new AssertionError()).when(delegate).verify(any());

        // When: The verificationOverTime is used to verify the data
        assertThrows(AssertionError.class, () -> {
            verificationOverTime.verify(mock(VerificationData.class));
        });

        // Then: The verificationOverTime waits for the full timeout before throwing the error
        timer.advanceTimeBy(TIMEOUT_MILLIS);
        verify(delegate, never()).verify(any());
        assertFalse(verificationOverTime.getDelegate().isSatisfied());
        assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyReturnsOnSuccess() {
        // Given: The delegate verification mode is satisfied immediately
        verify(delegate, times(1)).verify(any());

        // When: The verificationOverTime is used to verify the data with returnOnSuccess = true
        verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, delegate, true, timer);
        verificationOverTime.verify(mock(VerificationData.class));

        // Then: The verificationOverTime is also satisfied immediately
        verify(delegate, times(1)).verify(any());
        assertTrue(verificationOverTime.getDelegate().isSatisfied());
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyDoesNotReturnOnFailure() {
        // Given: The delegate verification mode is not satisfied initially
        verifyZeroInteractions(delegate);

        // When: The verificationOverTime is used to verify the data with returnOnSuccess = false
        verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, delegate, false, timer);
        try {
            verificationOverTime.verify(mock(VerificationData.class));
        } catch (MockitoAssertionError e) {
            // Then: The verificationOverTime does not return until the full timeout has passed
            timer.advanceTimeBy(TIMEOUT_MILLIS);
            verify(delegate, times(1)).verify(any());
            assertTrue(verificationOverTime.getDelegate().isSatisfied());
            assertTrue(verificationOverTime.isSatisfied());
            throw e;
        }
    }

    @Test
    public void testVerifyThrowsArgumentsAreDifferentException() {
        // Given: The delegate verification mode throws an ArgumentsAreDifferent exception
        doThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("message"))
                .when(delegate).verify(any());

        // When: The verificationOverTime is used to verify the data
        assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> {
            verificationOverTime.verify(mock(VerificationData.class));
        });

        // Then: The verificationOverTime throws the same exception
        verify(delegate, never()).verify(any());
        assertFalse(verificationOverTime.getDelegate().isSatisfied());
        assertFalse(verificationOverTime.isSatisfied());
    }
}