package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImplIsSatisfiedBeforeTimeoutWhenDelegateIsSatisfied() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        VerificationData data = mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerificationOverTimeImplIsNotSatisfiedBeforeTimeoutWhenDelegateIsNotSatisfied() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(mock(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate method call threw an exception"));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // When / Then
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
    }

    @Test
    public void testVerificationOverTimeImplIsSatisfiedAfterTimeoutWhenDelegateIsSatisfied() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(mock(VerificationData.class))).thenReturn(null);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerificationOverTimeImplIsNotSatisfiedAfterTimeoutWhenDelegateIsNotSatisfied() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(mock(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate method call threw an exception"));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // When / Then
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));

        // Given - simulate the timeout
        verificationOverTime.timer.stop();

        // When / Then
        assertTrue(verificationOverTime.isTimeoutReached());
    }

    @Test
    public void testVerificationOverTimeImplIsNotSatisfiedWhenDelegateReturnsDifferentArgument() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(eq(mock(VerificationData.class)))).thenReturn(null);
        when(delegate.verify(any(VerificationData.class))).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Expected :<[...]>, actual:<[...]>"));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData dataExpected = mock(VerificationData.class);
        VerificationData dataActual = mock(VerificationData.class);

        // When
        verificationOverTime.verify(dataExpected);

        // Then
        verifyNoMoreInteractions(delegate);

        // When
        assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> verificationOverTime.verify(dataActual));
    }

    @Test
    public void testVerificationOverTimeImplIsSatisfiedWhenDelegateIsSatisfiedAndReturnOnSuccessIsTrue() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(mock(VerificationData.class))).thenReturn(null);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        VerificationData data = mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerificationOverTimeImplIsNotSatisfiedWhenDelegateIsNotSatisfiedAndReturnOnSuccessIsFalse() {
        // Given
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(mock(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate method call threw an exception"));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // When / Then
        assertFalse(verificationOverTime.isSatisfied());

        // Given - simulate the timeout
        verificationOverTime.timer.stop();

        // When / Then
        assertTrue(verificationOverTime.isTimeoutReached());

        // Given
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        // When
        verificationOverTime.verify(data);

        // Then
        assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testCanRecoverFromFailure() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);

        // When
        assertTrue(new VerificationOverTimeImpl(10L, 50L, delegate, false).canRecoverFromFailure(delegate));

        // Given
        VerificationMode atMost = mock(AtMost.class);

        // When
        assertFalse(new VerificationOverTimeImpl(10L, 50L, atMost, false).canRecoverFromFailure(atMost));
    }

    @Test
    public void testSleep() {
        // Given
        long sleep = 50L;

        // When
        new VerificationOverTimeImpl(0L, 0L, null, false).sleep(sleep);

        // Then
        // Wait for some time to ensure the thread has slept
        try {
            Thread.sleep(60L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPollingPeriod() {
        // Given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, null, false);

        // Then
        assertEquals(10L, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        // Given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, null, false);

        // Then
        assertEquals(50L, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        // Given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, null, false);

        // Then
        assertSame(null, verificationOverTime.getDelegate());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNullVerificationData() {
        // Given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, null, false);

        // When
        verificationOverTime.verify(null);
    }

    @Test
    public void testVerifyNullDelegate() {
        // Given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, null, false);

        // When / Then
        assertThrows(NullPointerException.class, () -> new VerificationOverTimeImpl(10L, 50L, null, false).delegate);
    }
}