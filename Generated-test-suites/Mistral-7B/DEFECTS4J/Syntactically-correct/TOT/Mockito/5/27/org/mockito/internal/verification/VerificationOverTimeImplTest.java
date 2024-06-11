package org.mockito.internal.verification.impl.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTimeImpl;
    private Timer timer;

    @Before
    public void setup() {
        verificationMode = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(500, 1000, verificationMode, false, timer);
    }

    @Test
    public void shouldSatisfyDelegateVerificationModeWithinGivenTime() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doReturn(true).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertFalse(timer.isCounting());
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowExceptionIfDelegateVerificationModeNotSatisfiedWithinGivenTime() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doThrow(new MockitoAssertionError("Delegate verification mode not satisfied")).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertTrue(timer.isCounting());
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowExceptionIfDelegateVerificationModeNotRecoversFromFailure() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doThrow(new MockitoAssertionError("Delegate verification mode failed")).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertTrue(timer.isCounting());

        Mockito.verify(verificationMode).verify(data); // Should fail and throw an exception
    }

    @Test
    public void shouldNotSatisfyDelegateVerificationModeIfNotSatisfiedWithinGivenTimeAndReturnOnSuccessIsFalse() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doReturn(true).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertTrue(timer.isCounting());

        Mockito.verifyZeroInteractions(verificationMode);
    }

    @Test
    public void shouldSatisfyDelegateVerificationModeWithDelayAndReturnOnSuccessIsTrue() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doReturn(true).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertFalse(timer.isCounting());

        Mockito.verify(verificationMode).verify(data);
    }

    @Test
    public void shouldNotSatisfyDelegateVerificationModeWithDelayAndReturnOnSuccessIsFalse() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doReturn(true).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertTrue(timer.isCounting());

        Mockito.verifyZeroInteractions(verificationMode);

        TimeUnit.MILLISECONDS.sleep(1500); // Wait for the timeout to elapse

        Mockito.verify(verificationMode).verify(data);
    }

    @Test
    public void shouldNotSatisfyDelegateVerificationModeIfDelegateIsNoMoreInteractions() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doReturn(null).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertTrue(timer.isCounting());

        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void shouldNotSatisfyDelegateVerificationModeIfDelegateIsAtMost() throws InterruptedException {
        VerificationData data = mock(VerificationData.class);

        Mockito.doThrow(new MockitoAssertionError("Delegate verification mode invocation limit reached")).when(verificationMode).verify(data);

        verificationOverTimeImpl.verify(data);

        Assert.assertTrue(timer.isCounting());

        Mockito.verify(verificationMode).verify(data); // Should fail and throw an exception
    }
}