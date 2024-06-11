package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.api.VerificationModeFactory.*;
import static org.mockito.internal.verification.api.Stubbing;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationMode;
    private Timer timer;

    @Before
    public void setup() {
        this.timer = new Timer(1000);
        this.verificationMode = new VerificationOverTimeImpl(100, 1000, times(1), true, timer);
    }

    @Test
    public void testSuccessWithinTimeout() {
        doNothing().when(mock(Object.class)).verifyNoMoreInteractions();
        VerificationData data = VerificationData.forObject(mock(Object.class));
        verificationMode.verify(data);
        verify(mock(Object.class)).verifyNoMoreInteractions();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testFailureBeforeTimeout() throws InterruptedException {
        doThrow(new IllegalStateException("Failed to verify")).when(mock(Object.class)).verify();
        VerificationData data = VerificationData.forObject(mock(Object.class));
        verificationMode.verify(data);
        verifyZeroInteractions(mock(Object.class));

        Thread.sleep(500);
        verificationMode.verify(data);

        verify(mock(Object.class)).verify(); // should fail here
    }

    @Test(expected = MockitoAssertionError.class)
    public void testFailureAfterTimeout() throws InterruptedException {
        doThrow(new IllegalStateException("Failed to verify")).when(mock(Object.class)).verify();
        VerificationData data = VerificationData.forObject(mock(Object.class));
        verificationMode.verify(data);

        Thread.sleep(1500); // wait for timeout to elapse

        verificationMode.verify(data); // should throw exception here
    }

    @Test(expected = MockitoAssertionError.class)
    public void testFailureWithExceptionThatCantBeRecovered() throws InterruptedException {
        doThrow(new AssertionError("Failed to verify")).when(mock(Object.class)).verify();
        VerificationData data = VerificationData.forObject(mock(Object.class));
        verificationMode.verify(data);

        Thread.sleep(1500); // wait for timeout to elapse

        verificationMode.verify(data); // should throw exception here
    }

    @Test
    public void testGetters() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(200, 2000, never(), true, new Timer(2000));

        assertEquals(200, verificationMode.getPollingPeriod());
        assertEquals(2000, verificationMode.getDuration());
        assertEquals(never(), verificationMode.getDelegate());
    }

    @Test
    public void testDelegateVerifyFunctionality() {
        VerificationMode delegate = Stubbing.with(times(1)).verify();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        VerificationData data = VerificationData.forObject(mock(Object.class));
        verificationMode.verify(data);
        verify(mock(Object.class)).verify();
    }

    @Test
    public void testSleepFunctionality() throws InterruptedException {
        verificationMode.sleep(500);
        Thread.sleep(500);

        long elapsed = timer.elapsed();
        assertTrue(elapsed >= 500);
    }
}