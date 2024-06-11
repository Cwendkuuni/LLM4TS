package org.mockito.internal.verification.impl; // adjust the package if needed

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timerMock;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timerMock = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true, timerMock);
    }

    @Test
    public void testDelegateSatisfiedBeforeTimeout() {
        // given
        VerificationData data = new VerificationData(mock(Object.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        // when
        verificationOverTime.verify(data);

        // then
        verify(delegate).verify(ArgumentMatchers.eq(data));
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateNotSatisfiedBeforeTimeout() {
        // given
        VerificationData data = new VerificationData(mock(Object.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        // when
        verificationOverTime.verify(data);

        // then delegate verification mode is not satisfied within timeout
        verify(delegate, never()).verify(ArgumentMatchers.eq(data));

        // throw exception
        Mockito.verify(delegate).verify(ArgumentMatchers.eq(data));
    }

    @Test
    public void testDelegateNotSatisfiedAfterTimeout() {
        // given
        VerificationData data = new VerificationData(mock(Object.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        // when
        verificationOverTime.verify(data);

        // then delegate verification mode is not satisfied within timeout
        verify(delegate, never()).verify(ArgumentMatchers.eq(data));

        // sleep for some time to make sure verificationOverTime is not satisfied yet
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            // handle exception
        }

        // then last error thrown by delegate is thrown
        Mockito.verify(delegate).verify(ArgumentMatchers.eq(data));
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateFailsWithNonRecoverableError() {
        // given
        VerificationData data = new VerificationData(mock(Object.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        VerificationMode failedDelegate = mock(VerificationMode.class);
        when(failedDelegate.verify(data)).thenThrow(new MockitoAssertionError("Failed verification with non-recoverable error"));

        // when
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, failedDelegate, false, timerMock);
        verificationOverTime.verify(data);

        // then
        verify(failedDelegate).verify(ArgumentMatchers.eq(data));

        // throw exception
        Mockito.verify(failedDelegate).verify(ArgumentMatchers.eq(data));
    }

    @Test
    public void testDelegateSatisfiedAfterTimeoutWithReturnOnSuccess() {
        // given
        VerificationData data = new VerificationData(mock(Object.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        // when
        verificationOverTime.verify(data);

        // then delegate verification mode is satisfied after timeout and verificationOverTime is satisfied
        verify(delegate, times(1)).verify(ArgumentMatchers.eq(data));
        verify(delegate, times(1)).verifyNoMoreInteractions();
    }

    @Test
    public void testDelegateNotSatisfiedAfterTimeoutWithReturnOnSuccess() {
        // given
        VerificationData data = new VerificationData(mock(Object.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        // when
        verificationOverTime.verify(data);

        // then delegate verification mode is not satisfied after timeout and verificationOverTime is not satisfied
        verify(delegate, never()).verify(ArgumentMatchers.eq(data));
        verify(delegate, never()).verifyNoMoreInteractions();
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(delegate);
        Mockito.verifyNoMoreInteractions(timerMock);
    }
}