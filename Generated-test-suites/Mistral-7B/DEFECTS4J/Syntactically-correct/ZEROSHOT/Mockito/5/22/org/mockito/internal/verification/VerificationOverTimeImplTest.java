package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

/**
 * Test class for VerificationOverTimeImpl
 */
public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationMode verificationOverTime;
    private Timer timer;

    @Before
    public void setup() {
        verificationMode = new VerificationWithTimeout(1, 100L, ArgumentMatchers.any(), VerificationModeFactory.createNever());
        timer = Mockito.spy(new Timer(100L));
        verificationOverTime = new VerificationOverTimeImpl(1, 500L, verificationMode, true, timer);
    }

    @Test
    public void testVerificationWithinLimit() {
        VerificationData data = Mockito.mock(VerificationData.class);
        verificationOverTime.verify(data);
        Assert.assertFalse("Verification should not fail", verificationOverTime.isFailed());
    }

    @Test(expected = AssertionError.class)
    public void testVerificationOutsideLimit() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new InvocationOnMockException()).when(verificationMode).verify(data);

        timer.start();
        verificationOverTime.verify(data);
        timer.stop();
        Assert.assertTrue("Verification should have failed", verificationOverTime.isFailed());
    }

    @Test
    public void testVerificationWithRecoverableFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new MockitoAssertionError("Expected exception")).when(verificationMode).verify(data);
        Assert.assertFalse("Verification should not fail initially", verificationOverTime.isFailed());

        timer.start();
        verificationOverTime.verify(data);
        timer.stop();
        Assert.assertTrue("Verification should have failed", verificationOverTime.isFailed());

        timer.start();
        verificationOverTime.verify(data);
        timer.stop();
        Assert.assertFalse("Verification should not fail again once recovered", verificationOverTime.isFailed());
    }

    @Test(expected = AssertionError.class)
    public void testVerificationWithNonRecoverableFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new AssertionError("Expected error")).when(verificationMode).verify(data);

        timer.start();
        verificationOverTime.verify(data);
        timer.stop();
        Assert.assertTrue("Verification should have failed", verificationOverTime.isFailed());

        timer.start();
        verificationOverTime.verify(data);
        timer.stop();
        Assert.assertTrue("Verification should still fail", verificationOverTime.isFailed());
    }

    @Test
    public void testGetters() {
        Assert.assertEquals("Polling period should be correct", 1, verificationOverTime.getPollingPeriod());
        Assert.assertEquals("Duration should be correct", 500, verificationOverTime.getDuration());
        Assert.assertSame("Delegate should be correct", verificationMode, verificationOverTime.getDelegate());
    }
}