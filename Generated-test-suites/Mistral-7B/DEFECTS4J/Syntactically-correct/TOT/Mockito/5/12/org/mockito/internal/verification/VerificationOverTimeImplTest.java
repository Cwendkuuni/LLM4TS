package org.mockito.internal.verification.impl; // adjust the package if necessary

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timerSpy;

    @Before
    public void setUp() {
        verificationMode = Mockito.mock(VerificationMode.class);
        timerSpy = Mockito.spy(new Timer(100));
        verificationOverTime = new VerificationOverTimeImpl(50, 200, verificationMode, false, timerSpy);
    }

    @Test
    public void testDelegateVerificationIsSatisfiedBeforeTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given
        Mockito.doAnswer(invocation -> {
            verify(data, verificationMode);
            return null;
        }).when(verificationMode).verify(data);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(verificationMode, times(1)).verify(data);
        verify(timerSpy, never()).throwException();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationIsNotSatisfiedBeforeTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given
        doThrow(new MockitoAssertionError(" unexpected error")).when(verificationMode).verify(data);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(verificationMode, never()).verify(data);
        verify(timerSpy, times(2)).throwException(); // first throw is from handleVerifyException(), second is from verify()
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationIsNotSatisfiedAfterTimeoutWithNonRecoverableFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode nonRecoverableVerificationMode = Mockito.mock(VerificationMode.class);
        Mockito.doThrow(new AssertionError("unexpected error")).when(nonRecoverableVerificationMode).verify(data);

        // Given
        verificationOverTime = new VerificationOverTimeImpl(50, 200, nonRecoverableVerificationMode, false, timerSpy);

        // When
        VerificationData dataToVerify = Mockito.mock(VerificationData.class);

        // Then
        assertExceptionIsThrown(dataToVerify);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationIsNotSatisfiedAfterTimeoutWithRecoverableFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode recoverableVerificationMode = Mockito.mock(VerificationMode.class);

        // Given
        doThrow(new MockitoAssertionError("unexpected error")).when(recoverableVerificationMode).verify(data);

        // When
        verificationOverTime = new VerificationOverTimeImpl(50, 200, recoverableVerificationMode, true, timerSpy);
        VerificationData dataToVerify = Mockito.mock(VerificationData.class);

        // Then
        assertExceptionIsThrown(dataToVerify);
    }

    @Test
    public void testDelegateVerificationIsSatisfiedAfterTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Given
        Mockito.doAnswer(invocation -> {
            verify(data, verificationMode);
            return null;
        }).when(verificationMode).verify(data);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(verificationMode, never()).verify(data); // first call to verify() is ignored
        sleep(250); // sleep for half the timeout to simulate partial timeout
        verify(verificationMode, times(1)).verify(data);
        verify(timerSpy, times(2)).throwException(); // first throw is from handleVerifyException(), second is from verify()
    }

    private void assertExceptionIsThrown(VerificationData data) {
        // Given
        verificationOverTime = new VerificationOverTimeImpl(50, 200, verificationMode, false, timerSpy);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timerSpy, times(1)).throwException();
    }

    private void sleep(long timeInMilliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeInMilliseconds);
        } catch (InterruptedException e) {
            //ignore
        }
    }
}