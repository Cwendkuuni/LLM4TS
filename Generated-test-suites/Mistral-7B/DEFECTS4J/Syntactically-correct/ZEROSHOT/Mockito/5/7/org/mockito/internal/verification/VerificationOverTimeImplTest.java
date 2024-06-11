package org.mockito.internal.verification.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyThrow;
import static org.mockito.Mockito.when;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.junit.MockitoTest;
import org.mockito.quality.Strictness;
import org.mockito.quality.VerificationModeRule;

import java.util.concurrent.TimeUnit;

@MockitoTest(strictness = Strictness.LENIENT)
@MockitoJUnitRunner
public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        delegate = mock(VerificationWithTimeout.class);
        verificationOverTime = new VerificationOverTimeImpl(1L, 100L, delegate, false, new Timer(100L));
    }

    @Test
    public void testDelegateIsSatisfiedImmediately() {
        // Given
        given(delegate.verify(any(VerificationData.class))).thenReturn(null);

        // When
        verificationOverTime.verify(mock(VerificationData.class));

        // Then
        verify(delegate).verify(any(VerificationData.class));
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testDelegateIsNotSatisfiedImmediately() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenThrow(new MockitoAssertionError("Expected exception"));
        given(delegate.verify(any(VerificationData.class))).thenReturn(null);

        // When
        verificationOverTime.verify(mock(VerificationData.class));

        // Then
        verify(delegate).verify(any(VerificationData.class));
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testDelegateIsSatisfiedBeforeTimeout() {
        // Given
        given(delegate.verify(any(VerificationData.class))).thenReturn(null);

        // When
        verificationOverTime.verify(mock(VerificationData.class));

        // Then
        verify(delegate).verify(any(VerificationData.class));
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testDelegateIsNotSatisfiedBeforeTimeout() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenThrow(new MockitoAssertionError("Expected exception"));

        // When
        try {
            verificationOverTime.verify(mock(VerificationData.class));
        } catch (MockitoAssertionError e) {
            // Then
            assertEquals("Expected exception", e.getMessage());
            verify(delegate, times(VerificationWithTimeout.MAX_RETRIES)).verify(any(VerificationData.class));
            verifyNoMoreInteractions(delegate);
        }
    }

    @Test
    public void testDelegateIsSatisfiedAndReturnOnSuccess() {
        // Given
        given(delegate.verify(any(VerificationData.class))).thenReturn(null);

        // When
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 100L, delegate, true, new Timer(100L));
        verificationOverTime.verify(mock(VerificationData.class));

        // Then
        verify(delegate).verify(any(VerificationData.class));
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testDelegateIsNotSatisfiedAndReturnOnSuccess() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenThrow(new MockitoAssertionError("Expected exception"));
        given(delegate.verify(any(VerificationData.class))).thenReturn(null);

        // When
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 100L, delegate, true, new Timer(100L));

        // Then
        verify(delegate, never()).verify(any(VerificationData.class));
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testDelegateFailsWithArgumentsAreDifferentException() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Expected", "Actual"));

        // When & Then
        verifyThrow(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class)
                .given(verificationOverTime::verify)
                .withArgument(0, any(VerificationData.class));
    }

    @Test
    public void testDelegateFailsWithArgumentsAreDifferentExceptionAndReturnOnSuccess() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Expected", "Actual"));

        // When & Then
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 100L, delegate, true, new Timer(100L));
        verifyThrow(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class)
                .given(verificationOverTime::verify)
                .withArgument(0, any(VerificationData.class));
    }

    @Test
    public void testTimerIsStoppedOnceDelegateIsSatisfied() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenReturn(null).thenThrow(new MockitoAssertionError("Expected exception"));

        // When
        verificationOverTime.verify(mock(VerificationData.class));

        // Then
        verify(delegate).verify(any(VerificationData.class));
        verify(verificationOverTime.timer).stop();
    }

    @Test
    public void testTimerIsNotStoppedOnceDelegateIsNotSatisfied() {
        // Given
        given(delegate.verify(any(VerificationData.class)))
                .thenThrow(new MockitoAssertionError("Expected exception"));

        // When
        try {
            verificationOverTime.verify(mock(VerificationData.class));
        } catch (MockitoAssertionError e) {
            // Then
            verify(delegate).verify(any(VerificationData.class));
            verify(verificationOverTime.timer).isCounting();
        }
    }

    @Test
    public void testGetPollingPeriod() {
        // When
        long pollingPeriod = verificationOverTime.getPollingPeriod();

        // Then
        assertEquals(1L, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        // When
        long duration = verificationOverTime.getDuration();

        // Then
        assertEquals(100L, duration);
    }

    @Test
    public void testGetDelegate() {
        // When
        VerificationMode verificationMode = verificationOverTime.getDelegate();

        // Then
        assertEquals(delegate, verificationMode);
    }

    @Rule
    public VerificationModeRule verificationModeRule = new VerificationModeRule();
}