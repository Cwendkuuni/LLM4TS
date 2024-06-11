package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.VerificationDataBuilder;
import org.mockito.internal.verification.VerificationModeDelegate;
import org.mockito.internal.verification.VerificationModeWithTimeout;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    private VerificationModeDelegate verificationModeDelegate;
    private VerificationOverTimeImpl verificationOverTime;
    private long pollingPeriodMillis = 10L;
    private long durationMillis = 100L;

    @Before
    public void setUp() {
        verificationModeDelegate = mock(VerificationModeDelegate.class);
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationModeDelegate, false);
    }

    @Test
    public void testVerify_delegateSatisfiedBeforeTimeout_shouldReturnImmediately() {
        // given
        VerificationData verificationData = VerificationDataBuilder.aData();

        // when
        verificationOverTime.verify(verificationData);

        // then
        verify(verificationModeDelegate).verify(verificationData);
    }

    @Test
    public void testVerify_delegateNotSatisfiedBeforeTimeout_shouldThrowError() {
        // given
        ArgumentCaptor<AssertionError> assertionErrorCaptor = ArgumentCaptor.forClass(AssertionError.class);
        VerificationData verificationData = VerificationDataBuilder.aData();

        // when & then
        Assert.assertThatThrownBy(() -> verificationOverTime.verify(verificationData))
                .isInstanceOf(AssertionError.class)
                .satisfies(assertionErrorCaptor.capture());

        // then
        Assert.assertThat(verificationModeDelegate).never().verify(verificationData);
        Assert.assertThat(verificationModeDelegate).never().verifyNoMoreInteractions(mock(Object.class));
        Assert.assertThat(verificationModeDelegate).never().verify(argThat(isA(VerificationData.class)));
        Assert.assertThat(verificationModeDelegate).never().verifyNoMoreInteractions(verificationModeDelegate);

        Assert.assertEquals("Expected invocation on this mock at least once but was not invoked", assertionErrorCaptor.getValue().getMessage());
    }

    @Test
    public void testVerify_delegateSatisfiedAfterTimeout_shouldThrowError() {
        // given
        VerificationData verificationData = VerificationDataBuilder.aData();
        doThrow(new MockitoAssertionError("Verification failed: expected: at least 1 time, but was 0 times: " +
                "mock<Object> # 0 VerificationOverTimeImplTest.testVerify_delegateSatisfiedAfterTimeout()"))
                .when(verificationModeDelegate)
                .verify(verificationData);

        // when & then
        Assert.assertThatThrownBy(() -> verificationOverTime.verify(verificationData))
                .isInstanceOf(MockitoAssertionError.class);

        // then
        verify(verificationModeDelegate, times(1)).verify(verificationData);
    }

    @Test
    public void testVerify_delegateNotSatisfiedAfterTimeout_shouldThrowDelegateError() {
        // given
        ArgumentCaptor<AssertionError> assertionErrorCaptor = ArgumentCaptor.forClass(AssertionError.class);
        VerificationData verificationData = VerificationDataBuilder.aData();

        // when & then
        Assert.assertThatThrownBy(() -> verificationOverTime.verify(verificationData))
                .isInstanceOf(AssertionError.class)
                .satisfies(assertionErrorCaptor.capture());

        // then
        verify(verificationModeDelegate).never().verify(verificationData);
        verify(verificationModeDelegate).never().verifyNoMoreInteractions(mock(Object.class));
        verify(verificationModeDelegate).never().verify(argThat(isA(VerificationData.class)));
        verify(verificationModeDelegate).never().verifyNoMoreInteractions(verificationModeDelegate);

        Assert.assertEquals("Verification failed: expected: at least 1 time, but was 0 times: " +
                "mock<Object> # 0 VerificationOverTimeImplTest.testVerify_delegateNotSatisfiedAfterTimeout()",
                assertionErrorCaptor.getValue().getMessage());
    }

    @Test
    public void testCanRecoverFromFailure_shouldReturnTrueIfDelegateIsNotAtMostOrNoMoreInteractions() {
        // then
        Assert.assertTrue(verificationOverTime.canRecoverFromFailure(verificationModeDelegate));
    }

    @Test
    public void testCanRecoverFromFailure_shouldReturnFalseIfDelegateIsAtMostOrNoMoreInteractions() {
        // given
        VerificationMode withTimeout = new VerificationWithTimeout(1, TimeUnit.SECONDS);
        VerificationMode noMoreInteractions = new VerificationMode() {
            @Override
            public void verify(VerificationData data) {
                verifyNoMoreInteractions(mock(Object.class));
            }
        };

        // then
        Assert.assertFalse(verificationOverTime.canRecoverFromFailure(withTimeout));
        Assert.assertFalse(verificationOverTime.canRecoverFromFailure(noMoreInteractions));
    }

    @Test
    public void testGetPollingPeriod_shouldReturnPollingPeriod() {
        // then
        Assert.assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration_shouldReturnDuration() {
        // then
        Assert.assertEquals(durationMillis, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate_shouldReturnDelegate() {
        // then
        Assert.assertEquals(verificationModeDelegate, verificationOverTime.getDelegate());
    }

    private static ArgumentMatcher<Object> isA(Class<? extends Object> type) {
        return new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return type.isInstance(argument);
            }

            @Override
            public String description() {
                return String.format("an instance of %s", type.getName());
            }
        };
    }
}