package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private Timer timer;
    private VerificationOverTimeImpl verificationMode;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationMode = new VerificationOverTimeImpl(10L, 100L, delegate, false, timer);
    }

    @Test
    public void testVerify_delegateIsSatisfiedBeforeTimeout_returnOnSuccessTrue() {
        VerificationData data = mock(VerificationData.class);

        doAnswer(invocation -> {
            // delegate verification is mocked to return immediately
            return null;
        }).when(delegate).verify(data);

        verificationMode.verify(data);

        assertTrue(verificationMode.isSatisfied());
    }

    @Test
    public void testVerify_delegateIsSatisfiedBeforeTimeout_returnOnSuccessFalse() {
        VerificationData data = mock(VerificationData.class);

        doAnswer(invocation -> null).when(delegate).verify(data);

        verificationMode.verify(data);

        assertFalse(verificationMode.isSatisfied());

        // delegate verification should have been called only once
        verify(delegate, times(1)).verify(data);

        // no other verification should have been called
        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void testVerify_delegateIsNotSatisfiedBeforeTimeout_returnOnSuccessFalse() {
        VerificationData data = mock(VerificationData.class);

        doAnswer(invocation -> {
            throw new MockitoAssertionError("Delegate verification failed");
        }).when(delegate).verify(data);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationMode.verify(data);
        });

        // delegate verification should have been called repeatedly until timeout
        verify(delegate, times(verificationMode.getMaxRetries())).verify(data);

        // no other verification should have been called
        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void testVerify_delegateIsNotSatisfiedBeforeTimeout_returnOnSuccessTrue() {
        VerificationData data = mock(VerificationData.class);

        doAnswer(invocation -> {
            throw new MockitoAssertionError("Delegate verification failed");
        }).when(delegate).verify(data);

        verificationMode = new VerificationOverTimeImpl(10L, 100L, delegate, true, timer);

        verificationMode.verify(data);

        assertTrue(verificationMode.isSatisfied());
    }

    @Test
    public void testVerify_delegateIsNotSatisfiedAfterTimeout() {
        VerificationData data = mock(VerificationData.class);

        doAnswer(invocation -> {
            throw new MockitoAssertionError("Delegate verification failed");
        }).when(delegate).verify(data);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationMode.verify(data);
        });

        // delegate verification should have been called repeatedly until timeout
        verify(delegate, times(verificationMode.getMaxRetries())).verify(data);

        // after timeout, last error thrown by delegate is re-thrown
        MockitoAssertionError thrown = assertThrows(MockitoAssertionError.class, () -> {
            verificationMode.verify(data);
        });

        assertEquals("Delegate verification failed", thrown.getMessage());
    }

    @Test
    public void testVerify_delegateIsSatisfiedAfterTimeout() {
        VerificationData data = mock(VerificationData.class);

        doAnswer(invocation -> {
            // delegate verification is mocked to satisfy after timeout
            if (timer.isCounting()) {
                throw new MockitoAssertionError("Delegate verification not yet satisfied");
            }
            return null;
        }).when(delegate).verify(data);

        // initial verification fails
        assertThrows(MockitoAssertionError.class, () -> {
            verificationMode.verify(data);
        });

        // after timeout, delegate is satisfied, verification succeeds
        verificationMode.verify(data);

        assertTrue(verificationMode.isSatisfied());
    }
}