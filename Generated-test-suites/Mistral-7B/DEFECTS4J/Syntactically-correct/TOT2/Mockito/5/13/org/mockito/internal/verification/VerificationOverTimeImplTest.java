package org.mockito.internal.verification.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationMode = mock(VerificationMode.class);
        verificationData = new VerificationData(ArgumentMatchers.any());
    }

    @Test
    public void testVerify() throws InterruptedException {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, false);
        verificationOverTime.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }

    // Test verification with a shorter polling period
    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithShorterPollingPeriod() throws InterruptedException {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(50L, 500L, verificationMode, false);
        verificationOverTime.verify(verificationData);
    }

    // Test verification with a longer polling period
    @Test
    public void testVerifyWithLongerPollingPeriod() throws InterruptedException {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1500L, 500L, verificationMode, false);
        verificationOverTime.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }

    // Test verification when the delegate verification mode never recovers from failure
    public void testVerifyWithDelegateThatNeverRecovers() throws InterruptedException {
        VerificationMode delegateThatNeverRecovers = mock(VerificationMode.class);
        when(delegateThatNeverRecovers.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate failed"));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegateThatNeverRecovers, false);

        try {
            verificationOverTime.verify(verificationData);
            fail("Verification should have thrown an exception");
        } catch (MockitoAssertionError e) {
            // Expected exception
        }
    }

    // Test the behavior of the VerificationOverTimeImpl when the delegate throws a mockito-specific exception
    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithDelegateThatThrowsMockitoSpecificException() throws InterruptedException {
        VerificationMode delegateThatThrowsException = mock(VerificationMode.class);
        when(delegateThatThrowsException.verify(verificationData)).thenThrow(new ArgumentsAreDifferent(""));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegateThatThrowsException, false);

        try {
            verificationOverTime.verify(verificationData);
            fail("Verification should have thrown an exception");
        } catch (MockitoAssertionError e) {
            // Expected exception
        }
    }
}