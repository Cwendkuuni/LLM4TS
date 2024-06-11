package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.mock.MockVerificationMode;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTimeImpl;
    private Timer timer;

    @Before
    public void setUp() {
        this.verificationMode = new MockVerificationMode();
        this.verificationOverTimeImpl = new VerificationOverTimeImpl(100L, 2000L, verificationMode, true);
        this.timer = new Timer(2000L);
    }

    @Test
    public void testVerifySuccess() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
        verificationOverTimeImpl.verify(verificationData);
        assertTrue(verificationOverTimeImpl.isSatisfied());
    }

    @Test
    public void testVerifyFailureReturnOnSuccess() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
        AssertionError error = assertThrows(AssertionError.class, () -> verificationOverTimeImpl.verify(verificationData));
        assertNull(error);
    }

    @Test
    public void testVerifyFailureDoesNotReturnOnSuccess() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
        AssertionError error = assertThrows(AssertionError.class, () -> {
            verificationOverTimeImpl.verify(verificationData);
            verificationOverTimeImpl.verify(verificationData); // Should throw an error
        });
        assertFalse(verificationOverTimeImpl.isSatisfied());
        assertEquals("Expected invocation on mock at least once but was never invoked", error.getMessage());
    }

    @Test
    public void testVerifyTimeoutReturnOnSuccess() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
        timer.start();
        verificationOverTimeImpl.verify(verificationData);
        assertTrue(verificationOverTimeImpl.isSatisfied());
        assertTrue(timer.isStopped());
    }

    @Test
    public void testVerifyTimeoutDoesNotReturnOnSuccess() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
        timer.start();
        verificationOverTimeImpl.verify(verificationData);
        verificationOverTimeImpl.verify(verificationData); // Should throw an error
        assertFalse(verificationOverTimeImpl.isSatisfied());
        assertEquals("Expected invocation on mock at least once but was never invoked", verificationOverTimeImpl.getLastError().getMessage());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyTimeoutDoesNotReturnOnSuccessWithError() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
        timer.start();
        verificationOverTimeImpl.verify(verificationData);
        verificationOverTimeImpl.verify(verificationData); // Should throw an error
        assertFalse(verificationOverTimeImpl.isSatisfied());
    }

    @Test
    public void testGetPollingPeriod() {
        assertEquals(100L, verificationOverTimeImpl.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        assertEquals(2000L, verificationOverTimeImpl.getDuration());
    }

    @Test
    public void testGetDelegate() {
        assertEquals(verificationMode, verificationOverTimeImpl.getDelegate());
    }
}