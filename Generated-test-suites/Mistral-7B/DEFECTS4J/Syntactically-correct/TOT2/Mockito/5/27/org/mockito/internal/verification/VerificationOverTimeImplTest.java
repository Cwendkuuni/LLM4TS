package org.mockito.internal.verification.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class VerificationOverTimeImplTest {
    private static final long POLLING_PERIOD_MS = 100L;
    private static final long DURATION_MS = 500L;

    @Test
    public void testConstructor() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, delegate, true);

        // Check constructor arguments
        assertEquals(POLLING_PERIOD_MS, verificationOverTime.getPollingPeriod());
        assertEquals(DURATION_MS, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
        assertEquals(true, verificationOverTime.isReturnOnSuccess());

        // Test delegate verification
        verify(delegate).verify(ArgumentMatchers.any(VerificationData.class));

        // Test no more interactions with delegate
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, mock(VerificationMode.class), true);

        assertEquals(POLLING_PERIOD_MS, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, mock(VerificationMode.class), true);

        assertEquals(DURATION_MS, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, delegate, true);

        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    @Test
    public void testIsReturnOnSuccess() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, mock(VerificationMode.class), true);

        assertTrue(verificationOverTime.isReturnOnSuccess());
    }

    @Test
    public void testSleep() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, mock(VerificationMode.class), true);

        long initialTime = System.currentTimeMillis();
        verificationOverTime.sleep(50L);
        long elapsedTime = System.currentTimeMillis() - initialTime;

        assertEquals(50L, elapsedTime, 10);
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode atLeastOnce = mock(VerificationMode.class);
        VerificationMode atMostOnce = mock(VerificationMode.class);

        assertTrue(verificationOverTime.canRecoverFromFailure(atLeastOnce));
        assertFalse(verificationOverTime.canRecoverFromFailure(atMostOnce));
    }

    @Test
    public void testVerify() {
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, delegate, true);

        // Mock exception when delegate verification fails
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(data);

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (AssertionError e) {
            assertEquals("Delegate verification failed", e.getMessage());
        }

        // Test successful verification
        verifyNoMoreInteractions(delegate);
        verificationOverTime.verify(data);
        verify(delegate).verify(data);
    }

    @Test
    public void testHandleVerifyException() {
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, delegate, true);

        // Mock exception when delegate verification fails
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(data);

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (AssertionError e) {
            assertEquals("Delegate verification failed", e.getMessage());
        }

        // Test recovery from failure
        try {
            verificationOverTime.handleVerifyException(Mockito.mock(AssertionError.class));
            fail("Expected exception to be rethrown");
        } catch (AssertionError e) {
            // Expected
        }

        // Test cannot recover from failure
        VerificationMode atMost = mock(VerificationMode.class);
        try {
            verificationOverTime.handleVerifyException(Mockito.mock(AtMost.class));
            fail("Expected MockitoAssertionError");
        } catch (AssertionError e) {
            // Expected
        }
    }
}