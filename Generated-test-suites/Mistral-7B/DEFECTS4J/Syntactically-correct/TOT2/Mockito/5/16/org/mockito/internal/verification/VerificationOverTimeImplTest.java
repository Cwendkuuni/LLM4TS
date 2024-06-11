package org.mockito.internal.verification.test;

import static org.mockito.Mockito.*;

import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.internal.Timer;
import org.mockito.junit.MockitoJUnitRunner;
import org.testng.annotations.Test;

public class VerificationOverTimeImplTest {
    @Test
    public void testVerificationOverTimeImplConstruction() {
        VerificationMode delegate = mock(VerificationMode.class);
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        // Check that constructor has been called with correct arguments
        verify(timer)._constructor_(durationMillis);
    }

    @Test
    public void testVerify_WithNoSatisfiedInvocations() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 100, delegate, true, mock(Timer.class));

        // Let's simulate that the delegate verification mode never returns
        doThrow(new MockitoAssertionError("Error!")).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test
    public void testVerify_WithArgumentsAreDifferentException() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 100, delegate, true, mock(Timer.class));

        // Let's simulate that the delegate verification mode throws ArgumentsAreDifferentException
        doThrow(new ArgumentsAreDifferentException("Error!")).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test
    public void testVerify_WithEmptyVerificationData() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        when(data.getMock()).thenReturn(mock(Object.class));
        when(data.getMockInvocations()).thenReturn(new Object[0]);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 100, delegate, true, mock(Timer.class));

        verificationOverTimeImpl.verify(data);
    }

    @Test
    public void testVerify_WithNullVerificationData() {
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 100, delegate, true, mock(Timer.class));

        try {
            verificationOverTimeImpl.verify(null);
            fail("Verify should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVerify_WithNegativePollingPeriod() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        long pollingPeriodMillis = -1L;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, 100, delegate, true, mock(Timer.class));

        try {
            verificationOverTimeImpl.verify(data);
            fail("Verify should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testVerify_WithNegativeDuration() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        long durationMillis = -1L;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(10, durationMillis, delegate, true, mock(Timer.class));

        try {
            verificationOverTimeImpl.verify(data);
            fail("Verify should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testVerify_WithNullDelegate() {
        VerificationData data = mock(VerificationData.class);
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        boolean returnOnSuccess = true;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, null, returnOnSuccess, mock(Timer.class));

        try {
            verificationOverTimeImpl.verify(data);
            fail("Verify should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVerify_WithNegativeReturnOnSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, mock(Timer.class));

        verificationOverTimeImpl.verify(data);

        // Verify that the delegate verification mode has been called with the correct arguments
        verify(delegate).verify(data);

        // Verify that the full duration has been waited
        verify(timer).start();
        verify(timer).isCounting();
    }
}