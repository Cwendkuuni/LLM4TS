package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.verification.api.VerificationData.verificationData;
import static org.mockito.verification.VerificationMode.atLeastOnce;
import static org.mockito.verification.VerificationMode.never;
import static org.mockito.verification.VerificationMode.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {
    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;
    private long pollingPeriodMillis;
    private long durationMillis;

    @Before
    public void setup() {
        delegate = atLeastOnce(times(1));
        pollingPeriodMillis = 10L;
        durationMillis = 100L;
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
    }

    @Test
    public void testVerifySuccess() {
        VerificationData data = verificationData(delegate);
        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyFailureWithRecoverableDelegate() {
        VerificationData data = verificationData(never());
        AssertionError error = assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(data);
        });
        assertFalse(verificationOverTime.isSatisfied());
        assertFalse(error.getMessage().contains("timeout"));
        assertTrue(verificationOverTime.canRecoverFromFailure(delegate));
        sleep(pollingPeriodMillis);
        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyFailureWithNonRecoverableDelegate() {
        VerificationData data = verificationData(atMost(1));
        AssertionError error = assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(data);
        });
        assertFalse(verificationOverTime.isSatisfied());
        assertFalse(error.getMessage().contains("timeout"));
        assertFalse(verificationOverTime.canRecoverFromFailure(delegate));
        sleep(durationMillis);
        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSatisfied());
        assertTrue(error.getMessage().contains("timeout"));
    }

    @Test
    public void testGetters() {
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException ie) {
            // swallow the exception
        }
    }
}

class AtMost implements VerificationMode {
    @Override
    public void verify(VerificationData data) {
        throw new MockitoAssertionError("Verification failed: expected no more than 1 call but actually was called 2 or more times.");
    }
}