package org.mockito.internal.verification.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;
import static org.junit.Assert.assertEquals;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        // Given
        VerificationData data = new VerificationData();
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, false);

        // When & Then
        testVerifySuccess(verificationOverTime, delegate);

        // When the delegate always throws an exception
        then(delegate).verify(ArgumentMatchers.eq(data)).throwsNew(MockitoAssertionError.class);
        testVerifyFailure(verificationOverTime, delegate);

        // When the delegate never returns from verification
        then(delegate).verify(ArgumentMatchers.eq(data)).never();
        testVerifyTimeout(verificationOverTime, delegate);

        // When the delegate succeeds immediately
        then(delegate).verify(ArgumentMatchers.eq(data)).times(1);
        testVerifySuccessImmediate(verificationOverTime, delegate);

        // When the delegate succeeds after the timeout
        then(delegate).verify(ArgumentMatchers.eq(data)).times(1);
        testVerifySuccessAfterTimeout(verificationOverTime, delegate);
    }

    // Add other test cases for getPollingPeriod(), getDuration(), and getDelegate() here

    // Helper methods for testVerify()
    private void testVerifySuccess(VerificationOverTimeImpl verificationOverTime, VerificationMode delegate) {
        // Given
        then(delegate).verify(ArgumentMatchers.eq(data)).times(1);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    private void testVerifyFailure(VerificationOverTimeImpl verificationOverTime, VerificationMode delegate) {
        // Given
        AssertionError expectedError = mock(AssertionError.class);
        then(delegate).verify(ArgumentMatchers.eq(data)).throwsNew(expectedError.getClass());

        // When
        AssertionError actualError = assertThrows(AssertionError.class, () -> verificationOverTime.verify(data));

        // Then
        assertEquals(expectedError, actualError);
    }

    private void testVerifyTimeout(VerificationOverTimeImpl verificationOverTime, VerificationMode delegate) {
        // Given
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true);
        when(timer.start()).thenThrow(new InterruptedException());

        // When
        VerificationOverTimeImpl verificationOverTimeWithTimer = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        // Then
        AssertionError expectedError = mock(AssertionError.class);
        then(delegate).verify(ArgumentMatchers.eq(data)).never();
        assertThrows(expectedError.getClass(), () -> verificationOverTimeWithTimer.verify(data));
    }

    private void testVerifySuccessImmediate(VerificationOverTimeImpl verificationOverTime, VerificationMode delegate) {
        // Given
        then(delegate).verify(ArgumentMatchers.eq(data)).times(1);
        when(delegate.verify(ArgumentMatchers.eq(data))).thenReturn(null);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    private void testVerifySuccessAfterTimeout(VerificationOverTimeImpl verificationOverTime, VerificationMode delegate) {
        // Given
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(false);

        // When
        VerificationOverTimeImpl verificationOverTimeWithTimer = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        // Then
        then(delegate).verify(ArgumentMatchers.eq(data)).times(1);
        verificationOverTimeWithTimer.verify(data);
    }
}