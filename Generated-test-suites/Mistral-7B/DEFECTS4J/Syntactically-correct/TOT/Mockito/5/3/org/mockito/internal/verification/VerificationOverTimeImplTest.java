package org.mockito.internal.verification.impl; // replace with the actual package name if needed

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.Stubbing;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timer;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 50;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        this.verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, mock(Timer.class));
        this.timer = mock(Timer.class);
        this.verificationData = mock(VerificationData.class);
    }

    @Test
    public void testDelegateSatisfiedWithinDuration() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate); // this is necessary to intercept the delegate.verify() calls

        // Given
        when(delegateVerifySpy.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn();

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // If returnOnSuccess is true, the test should pass here
        // If returnOnSuccess is false, the test should wait for the full duration to pass before passing
        verify(delegateVerifySpy).verify(this.verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateNotSatisfiedWithinDuration() {
        // Given
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(this.verificationData);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should throw the exception thrown by the delegate verification
    }

    @Test
    public void testDelegateSatisfiedAndReturnOnSuccess() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);

        // Given
        when(delegateVerifySpy.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn();

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should return immediately once the delegate verification succeeds
        verify(delegateVerifySpy).verify(this.verificationData);
    }

    @Test
    public void testDelegateNotSatisfiedAndReturnOnSuccess() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);

        // Given
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(this.verificationData);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should throw the exception thrown by the delegate verification
    }

    @Test
    public void testDelegateNotSatisfiedAndTimeoutExceeded() {
        // Given
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(this.verificationData);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should wait for the full duration to pass before throwing the exception
        // (assuming the delegate verification never recovers, as per the canRecoverFromFailure() implementation)
    }

    @Test
    public void testDelegateSatisfiedAndTimeoutExceeded() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);

        // Given
        when(delegateVerifySpy.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn();

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should return immediately once the delegate verification succeeds
        // (regardless of the duration)
        verify(delegateVerifySpy).verify(this.verificationData);
    }

    @Test
    public void testDelegateSatisfiedWithNoMoreInteractions() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);

        // Given
        when(delegateVerifySpy.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn();
        verifyNoMoreInteractions(delegate);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should throw an exception if the delegate has any more interactions
        verify(delegateVerifySpy, Mockito.times(1)).verify(this.verificationData);
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testDelegateNotSatisfiedWithNoMoreInteractions() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);
        verifyZeroInteractions(delegate);

        // Given
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(this.verificationData);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should throw the exception thrown by the delegate verification
    }

    @Test
    public void testDelegateSatisfiedWithTimeoutAndNoMoreInteractions() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);

        // Given
        when(delegateVerifySpy.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn();
        verifyNoMoreInteractions(delegate);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should return immediately once the delegate verification succeeds
        // (regardless of the duration)
        verify(delegateVerifySpy).verify(this.verificationData);
    }

    @Test
    public void testDelegateNotSatisfiedWithTimeoutAndNoMoreInteractions() {
        Stubbing<Void> delegateVerifySpy = Mockito.spy(delegate);
        verifyZeroInteractions(delegate);

        // Given
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(this.verificationData);

        // When
        this.verificationMode.verify(this.verificationData);

        // Then
        // The test should wait for the full duration to pass before throwing the exception
        // (assuming the delegate verification never recovers, as per the canRecoverFromFailure() implementation)
    }

    @Test
    public void testGetPollingPeriod() {
        // Given

        // When
        long pollingPeriod = this.verificationMode.getPollingPeriod();

        // Then
        // The polling period should be as configured during initialization
        assertEquals(50, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        // Given

        // When
        long duration = this.verificationMode.getDuration();

        // Then
        // The duration should be as configured during initialization
        assertEquals(500, duration);
    }

    @Test
    public void testGetDelegate() {
        // Given

        // When
        VerificationMode delegate = this.verificationMode.getDelegate();

        // Then
        // The delegate should be the one configured during initialization
        assertEquals(delegate, mock(VerificationMode.class));
    }
}