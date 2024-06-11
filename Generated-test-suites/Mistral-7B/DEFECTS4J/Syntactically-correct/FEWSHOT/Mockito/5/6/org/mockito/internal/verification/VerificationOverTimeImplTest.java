package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.MockitoTimer;
import org.mockito.internal.verification.api.MockitoTimerImpl;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyWithTimeoutAndReturnOnSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, true);
        VerificationData verificationData = mock(VerificationData.class);

        doNothing().when(delegate).verify(verificationData);

        verificationMode.verify(verificationData);

        assertTrue(verificationMode.isSatisfied());
    }

    @Test
    public void testVerifyWithTimeoutAndNoReturnOnSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);
        VerificationData verificationData = mock(VerificationData.class);

        doThrow(new RuntimeException("Expected exception")).when(delegate).verify(verificationData);

        verificationMode.verify(verificationData);

        assertFalse(verificationMode.isSatisfied());

        // The verification should have thrown an exception
        assertThrows(RuntimeException.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerifyWithTimeoutAndCannotRecover() {
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.getClass()).thenReturn(AtMost.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);
        VerificationData verificationData = mock(VerificationData.class);

        doThrow(new RuntimeException("Expected exception")).when(delegate).verify(verificationData);

        assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerifyWithTimeoutAndRecoverableFailure() {
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.getClass()).thenReturn(NoMoreInteractions.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);
        VerificationData verificationData = mock(VerificationData.class);

        doThrow(new RuntimeException("Expected exception")).when(delegate).verify(verificationData);

        // The first time the delegate throws an exception, the test should ignore it and continue polling
        verificationMode.verify(verificationData);

        // The second time the delegate throws an exception, the test should throw an exception
        assertThrows(RuntimeException.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerifyWithNoTimeout() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(0, 0, delegate, false);
        VerificationData verificationData = mock(VerificationData.class);

        doNothing().when(delegate).verify(verificationData);

        verificationMode.verify(verificationData);

        assertTrue(verificationMode.isSatisfied());
    }

    @Test
    public void testVerifyWithNoTimeoutAndImmediateSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(0, 0, delegate, true);
        VerificationData verificationData = mock(VerificationData.class);

        doNothing().when(delegate).verify(verificationData);

        verificationMode.verify(verificationData);

        assertTrue(verificationMode.isSatisfied());
    }

    @Test
    public void testVerifyWithNoTimeoutAndNoImmediateSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(0, 0, delegate, false);
        VerificationData verificationData = mock(VerificationData.class);

        doThrow(new RuntimeException("Expected exception")).when(delegate).verify(verificationData);

        // The verification should not succeed immediately, so it should not be satisfied yet
        assertFalse(verificationMode.isSatisfied());

        // The verification should throw an exception when called
        assertThrows(RuntimeException.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testGetters() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);

        assertEquals(100, verificationMode.getPollingPeriod());
        assertEquals(500, verificationMode.getDuration());
        assertEquals(delegate, verificationMode.getDelegate());
        assertFalse(verificationMode.isSatisfied());
    }

    @Test
    public void testTimer() {
        Timer timer = new MockitoTimerImpl();
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        verificationMode.verify(mock(VerificationData.class));

        assertTrue(timer.isCounting());
        assertTrue(timer.hasElapsed(500));

        // After the timeout, the timer should no longer be counting
        assertFalse(timer.isCounting());
    }

    private static class AtMost extends org.mockito.verification.VerificationMode {
    }

    private static class NoMoreInteractions extends org.mockito.verification.VerificationMode {
    }

}