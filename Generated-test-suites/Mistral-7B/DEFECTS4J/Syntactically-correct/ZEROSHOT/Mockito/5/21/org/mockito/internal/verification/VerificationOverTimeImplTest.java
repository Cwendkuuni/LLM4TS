package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationMode;
    private long pollingPeriodMillis = 50;
    private long durationMillis = 500;

    @Before
    public void setUp() {
        delegate = Mockito.mock(VerificationMode.class);
        verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
    }

    @Test
    public void testDelegateVerifiedImmediatelyAndReturnOnSuccessIsTrue() {
        // Given: Delegate verification mode is satisfied immediately
        Mockito.when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        // When: VerificationOverTimeImpl is used to verify the data
        verificationMode.verify(Mockito.mock(VerificationData.class));

        // Then: VerificationOverTimeImpl is satisfied immediately as returnOnSuccess is true
        Assert.assertNull(verificationMode.getError());
    }

    @Test(expected = AssertionError.class)
    public void testDelegateNotVerifiedBeforeTimeoutAndReturnOnSuccessIsFalse() {
        // Given: Delegate verification mode is not verified before timeout
        Mockito.when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate verification error"));

        // When: VerificationOverTimeImpl is used to verify the data
        verificationMode.verify(Mockito.mock(VerificationData.class));

        // Then: VerificationOverTimeImpl throws an exception as it did not verify the data and returnOnSuccess is false
        Assert.assertNotNull(verificationMode.getError());
        Assert.assertEquals("Delegate verification error", verificationMode.getError().getMessage());
    }

    @Test(expected = AssertionError.class)
    public void testDelegateNotVerifiedBeforeTimeoutAndReturnOnSuccessIsFalseWithPolling() {
        // Given: Delegate verification mode is not verified before timeout
        Mockito.when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate verification error"));

        // When: VerificationOverTimeImpl is used to verify the data
        verificationMode.verify(Mockito.mock(VerificationData.class));

        // Wait for some time to allow polling
        try {
            TimeUnit.MILLISECONDS.sleep(TimeUnit.MILLISECONDS.toMillis(durationMillis + pollingPeriodMillis));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Then: VerificationOverTimeImpl throws an exception as it did not verify the data and returnOnSuccess is false
        Assert.assertNotNull(verificationMode.getError());
        Assert.assertEquals("Delegate verification error", verificationMode.getError().getMessage());
    }

    @Test
    public void testDelegateVerifiedBeforeTimeoutAndReturnOnSuccessIsFalse() {
        // Given: Delegate verification mode is verified before timeout
        Mockito.when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        // When: VerificationOverTimeImpl is used to verify the data
        verificationMode.verify(Mockito.mock(VerificationData.class));

        // Wait for some time to allow the delegate to be verified
        try {
            TimeUnit.MILLISECONDS.sleep(TimeUnit.MILLISECONDS.toMillis(durationMillis - pollingPeriodMillis));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Then: VerificationOverTimeImpl does not throw an exception as it did not need to wait for the full timeout
        Assert.assertNull(verificationMode.getError());
    }
}