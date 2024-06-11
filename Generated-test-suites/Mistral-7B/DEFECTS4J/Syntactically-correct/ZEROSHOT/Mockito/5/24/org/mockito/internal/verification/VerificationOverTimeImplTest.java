package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.MockitoStubInvocationHandler;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeSuccess() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 200L;
        VerificationMode delegate = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchers.any(), ArgumentMatchers.any());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        MockitoStubInvocationHandler handler = new MockitoStubInvocationHandler();

        VerificationData data = new VerificationData(handler, new InvocationOnMock(handler, "methodToMock"), null);

        verificationOverTime.verify(data);

        Assert.assertTrue(delegate.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeFailure() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 100L;
        VerificationMode delegate = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchers.any(), ArgumentMatchers.any());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        MockitoStubInvocationHandler handler = new MockitoStubInvocationHandler();

        VerificationData data = new VerificationData(handler, new InvocationOnMock(handler, "methodToMock"), null);

        verificationOverTime.verify(data);
        verificationOverTime.verify(data);

        // verification will fail because it was not satisfied before the full duration has passed
        Assert.assertFalse(delegate.isSatisfied());
    }

    @Test
    public void testVerificationOverTimeTimeout() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 100L;
        VerificationMode delegate = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchgers.any(), ArgumentMatchers.any());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        MockitoStubInvocationHandler handler = new MockitoStubInvocationHandler();

        VerificationData data = new VerificationData(handler, new InvocationOnMock(handler, "methodToMock"), null);

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            Assert.assertEquals(delegate.getMessage(), e.getMessage());
            return;
        }

        Assert.fail("Expected MockitoAssertionError to be thrown");
    }

    @Test
    public void testVerificationOverTimeSleep() throws InterruptedException {
        long pollingPeriodMillis = 50L;
        long durationMillis = 200L;
        VerificationMode delegate = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchers.any(), ArgumentMatchers.any());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        MockitoStubInvocationHandler handler = new MockitoStubInvocationHandler();

        VerificationData data = new VerificationData(handler, new InvocationOnMock(handler, "methodToMock"), null);

        long startTime = System.nanoTime();
        verificationOverTime.verify(data);

        long elapsedTime = TimeUnit.MILLISECONDS.toMillis(System.nanoTime() - startTime);

        Assert.assertTrue(elapsedTime > durationMillis);
    }

    @Test
    public void testVerificationOverTimeReturnOnSuccess() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 200L;
        VerificationMode delegate = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchers.any(), ArgumentMatchers.any());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        MockitoStubInvocationHandler handler = new MockitoStubInvocationHandler();

        VerificationData data = new VerificationData(handler, new InvocationOnMock(handler, "methodToMock"), null);

        verificationOverTime.verify(data);

        Assert.assertTrue(delegate.isSatisfied());
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }
}