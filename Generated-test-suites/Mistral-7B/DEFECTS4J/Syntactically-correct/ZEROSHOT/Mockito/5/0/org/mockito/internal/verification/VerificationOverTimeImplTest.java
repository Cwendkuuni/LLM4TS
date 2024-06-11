package org.mockito.internal.verification.tests;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.internal.verification.VerificationAfterDelayImpl;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationWithTimeout() {
        long pollingPeriod = 10;
        long duration = 50;
        VerificationMode delegate = new VerificationWithTimeoutImpl(1, TimeUnit.SECONDS);

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriod, duration, delegate, true);

        Object mock = Mockito.mock(Object.class);
        Mockito.doAnswer(invocation -> {
            throw new InvocationOnMockException("Invocation not allowed");
        }).when(mock).someMethod(ArgumentMatchers.anyInt());

        VerificationData data = new VerificationData(mock, "someMethod", ArgumentMatchers.anyInt(), VerificationMode.times(1));

        try {
            verificationMode.verify(data);
            Assert.fail("Expected exception not thrown");
        } catch (AssertionError e) {
            Assert.assertEquals("Invocation not allowed", e.getMessage());
        }

        verificationMode.verify(data); // This time it should succeed
    }

    @Test(expected = AssertionError.class)
    public void testVerificationWithTimeout_delegateFails() {
        long pollingPeriod = 10;
        long duration = 50;
        VerificationMode delegate = new VerificationWithTimeoutImpl(0, TimeUnit.SECONDS);

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriod, duration, delegate, true);

        Object mock = Mockito.mock(Object.class);
        Mockito.doAnswer(invocation -> {
            throw new InvocationOnMockException("Invocation not allowed");
        }).when(mock).someMethod(ArgumentMatchers.anyInt());

        VerificationData data = new VerificationData(mock, "someMethod", ArgumentMatchers.anyInt(), VerificationMode.times(1));

        verificationMode.verify(data);
    }

    @Test
    public void testVerificationAfterDelay() {
        long pollingPeriod = 10;
        long duration = 50;
        VerificationMode delegate = new VerificationAfterDelayImpl(1, TimeUnit.SECONDS);

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriod, duration, delegate, false);

        Object mock = Mockito.mock(Object.class);
        Mockito.doAnswer(invocation -> {
            throw new InvocationOnMockException("Invocation not allowed");
        }).when(mock).someMethod(ArgumentMatchers.anyInt());

        VerificationData data = new VerificationData(mock, "someMethod", ArgumentMatchers.anyInt(), VerificationMode.times(1));

        try {
            verificationMode.verify(data);
            Assert.fail("Expected exception not thrown");
        } catch (AssertionError e) {
            Assert.assertEquals("Invocation not allowed", e.getMessage());
        }

        sleep(duration + pollingPeriod);

        verificationMode.verify(data); // This time it should succeed
    }

    @Test(expected = AssertionError.class)
    public void testVerificationAfterDelay_delegateFails() {
        long pollingPeriod = 10;
        long duration = 50;
        VerificationMode delegate = new VerificationAfterDelayImpl(0, TimeUnit.SECONDS);

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriod, duration, delegate, false);

        Object mock = Mockito.mock(Object.class);
        Mockito.doAnswer(invocation -> {
            throw new InvocationOnMockException("Invocation not allowed");
        }).when(mock).someMethod(ArgumentMatchers.anyInt());

        VerificationData data = new VerificationData(mock, "someMethod", ArgumentMatchers.anyInt(), VerificationMode.times(1));

        verificationMode.verify(data);
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }
}