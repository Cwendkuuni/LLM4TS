package org.mockito.internal.verification.impl; // change the package if needed

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationWithSuccess() throws InterruptedException {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        VerificationData data = Mockito.mock(VerificationData.class);

        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenAnswer(invocation -> {
            InvocationOnMock invocationMock = (InvocationOnMock) invocation.getArguments()[0];
            invocationMock.invokeExplictly();
        });

        verification.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationWithTimeout() throws InterruptedException {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10, 500, delegate, false);
        VerificationData data = Mockito.mock(VerificationData.class);

        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new MockitoAssertionError("Expected exception"));

        try {
            verification.verify(data);
            verifyZeroInteractions(delegate);
            fail("Expected exception");
        } catch (MockitoAssertionError e) {
            // expected
        }

        Thread.sleep(1001);
        verification.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationWithRetryableFailure() throws InterruptedException {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10, 1000, delegate, false);
        VerificationData data = Mockito.mock(VerificationData.class);

        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new RuntimeException("Unexpected Exception"));

        try {
            verification.verify(data);
            fail("Expected exceededTimeout exception");
        } catch (MockitoAssertionError e) {
            if (e.getCause() instanceof InterruptedException) {
                // expected, we'll retry
            } else {
                throw e;
            }
        }

        Thread.sleep(10);
        verification.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerificationWithFailureThatCannotRecover() throws InterruptedException {
        VerificationMode delegate = Mockito.mock(AtMost.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10, 1000, delegate, false);
        VerificationData data = Mockito.mock(VerificationData.class);

        try {
            verification.verify(data);
            fail("Expected exception");
        } catch (MockitoAssertionError e) {
            // expected
        } finally {
            verifyNoMoreInteractions(delegate);
        }
    }

    @Test
    public void testVerificationWithZeroDuration() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0, 0, delegate, false);

        VerificationData data = Mockito.mock(VerificationData.class);

        try {
            verification.verify(data);
            fail("Expected exception");
        } catch (AssertionError e) {
            // expected
            verifyZeroInteractions(delegate);
        }
    }

    @Test
    public void testVerificationWithNegativePollingPeriod() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(-1, 1000, delegate, false);

        VerificationData data = Mockito.mock(VerificationData.class);

        try {
            verification.verify(data);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testVerificationWithNegativeDuration() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, -1, delegate, false);

        VerificationData data = Mockito.mock(VerificationData.class);

        try {
            verification.verify(data);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}