package org.mockito.internal.verification.impl.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    @Mock
    private VerificationMode verificationModeMock;

    @InjectMocks
    private VerificationOverTimeImpl verificationOverTimeImpl;

    private VerificationData verificationData;

    @Before
    public void setup() {
        verificationData = new VerificationData(1, ArgumentMatchers.any());
    }

    @Test
    public void testDelegateVerificationModeIsSatisfiedBeforeTimeout() {
        when(verificationModeMock.verify(verificationData)).thenReturn(null);

        verificationOverTimeImpl.verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationModeIsNotSatisfiedBeforeTimeout() {
        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, false);

        when(verificationModeMock.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification mode failed"));

        verificationOverTimeImpl.verify(verificationData);
    }

    @Test
    public void testDelegateVerificationModeIsSatisfiedAfterTimeout() {
        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, false);

        when(verificationModeMock.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification mode failed"));

        Thread.yield(); // yield to other threads, allowing the verification to continue

        verificationOverTimeImpl.verify(verificationData); // this should now pass
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationModeIsNotSatisfiedAfterTimeout() {
        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, false);

        when(verificationModeMock.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification mode failed"));

        Thread.yield(); // yield to other threads, allowing the verification to continue

        Thread.yield(); // yield to other threads, allowing the verification to continue

        verificationOverTimeImpl.verify(verificationData); // this should now throw an error
    }

    @Test
    public void testDelegateVerificationModeIsSatisfiedAndReturnOnSuccessIsTrue() {
        when(verificationModeMock.verify(verificationData)).thenReturn(null);

        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, true);

        verificationOverTimeImpl.verify(verificationData);
    }

    @Test
    public void testDelegateVerificationModeIsNotSatisfiedAndReturnOnSuccessIsFalse() {
        when(verificationModeMock.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification mode failed"));

        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, false);

        assertFalse(verificationOverTimeImpl.verify(verificationData).isSatisfied());
    }

    @Test
    public void testDelegateVerificationModeIsNeverSatisfied() {
        when(verificationModeMock.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification mode failed"));

        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, false);

        assertFalse(verificationOverTimeImpl.verify(verificationData).isSatisfied());
    }

    @Test
    public void testDelegateVerificationModeIsSatisfiedAfterDelay() {
        when(verificationModeMock.verify(verificationData)).thenReturn(null);

        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 1000, verificationModeMock, false);

        Thread.yield(); // yield to other threads, allowing the verification to continue

        verify(verificationModeMock).verify(verificationData); // this should now pass
    }

    @Test
    public void testDelegateVerificationModeIsNotSatisfiedAfterDelay() {
        when(verificationModeMock.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification mode failed"));

        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 1000, verificationModeMock, false);

        Thread.yield(); // yield to other threads,
        Thread.yield(); // allowing the verification to continue

        verifyNoMoreInteractions(verificationModeMock); // this should now pass
    }

    @Test
    public void testDelegateVerificationModeThrowsArgumentsAreDifferentException() {
        when(verificationModeMock.verify(verificationData)).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Expected", "Actual"));

        try {
            verificationOverTimeImpl.verify(verificationData);
            fail("Expected an AssertionError but got null");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testDelegateVerificationModeThrowsMockitoException() {
        when(verificationModeMock.verify(verificationData)).thenThrow(new org.mockito.exceptions.base.MockitoException("Delegate verification mode threw an exception"));

        verificationOverTimeImpl.verify(verificationData);
    }
}