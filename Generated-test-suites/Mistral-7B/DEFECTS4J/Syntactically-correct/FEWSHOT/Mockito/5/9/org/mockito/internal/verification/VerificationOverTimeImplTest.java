package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationMode verificationOverTime;
    private Timer timer;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);
    }

    @Test
    public void testDelegateIsSatisfiedImmediately() {
        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
        verify(delegate).verify(data);
    }

    @Test
    public void testDelegateIsSatisfiedWithinDuration() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenReturn(data);

        verificationOverTime.verify(dataCaptor.capture());

        verify(delegate).verify(dataCaptor.getValue());
        assertTrue(timer.isCounting());
        timer.stop();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testDelegateIsNotSatisfiedWithinDuration() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenThrow(new MockitoAssertionError("Delegate is not satisfied"));

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(dataCaptor.capture()));
    }

    @Test
    public void testDelegateIsSatisfiedAndReturnOnSuccess() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenReturn(dataCaptor.capture());

        verificationOverTime.verify(dataCaptor.capture());

        verify(delegate).verify(dataCaptor.getValue());
        assertNull(verificationOverTime.getDelegate().verifyCalls());
    }

    @Test
    public void testDelegateIsNotSatisfiedAndReturnOnFailure() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenThrow(new MockitoAssertionError("Delegate is not satisfied"));

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(dataCaptor.capture()));

        verifyNoMoreInteractions(verificationOverTime.getDelegate());
    }

    @Test
    public void testVerificationFailuresAreThrown() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenThrow(new MockitoAssertionError("Delegate is not satisfied"));

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(dataCaptor.capture()));

        verify(delegate).verify(dataCaptor.getValue());
    }

    @Test
    public void testVerificationTimeoutIsThrown() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);

        when(delegate.verify(any()))
            .thenThrow(new MockitoAssertionError("Delegate is not satisfied"))
            .thenCallRealMethod();

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(dataCaptor.capture()));
    }

    @Test
    public void testIsCountingIsTrueDuringVerification() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenReturn(dataCaptor.capture());

        verificationOverTime.verify(dataCaptor.capture());

        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingIsFalseAfterVerification() {
        ArgumentCaptor<VerificationData> dataCaptor = ArgumentCaptor.forClass(VerificationData.class);
        when(delegate.verify(any())).thenReturn(dataCaptor.capture());

        verificationOverTime.verify(dataCaptor.capture());

        timer.stop();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testGetPollingPeriod() {
        assertEquals(100L, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        assertEquals(500L, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        assertEquals(delegate, verificationOverTime.getDelegate());
    }
}