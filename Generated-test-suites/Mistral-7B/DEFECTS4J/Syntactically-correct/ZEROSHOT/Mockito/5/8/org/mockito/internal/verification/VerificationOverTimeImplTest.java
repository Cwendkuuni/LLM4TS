package org.mockito.internal.verification.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.api.Invocations.invocation;
import static org.mockito.internal.verification.api.VerificationData.verificationData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.Invocations;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationModeWithTimeout;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.api.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.NoMoreInteractions;
import org.mockito.internal.verification.api.AtMost;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationWithTimeout;
    private VerificationMode verificationAfterDelay;
    private VerificationMode verificationNoInteractions;
    private VerificationMode verificationAtMost;
    private VerificationMode verificationNever;

    private Object mockObject;
    private long pollingPeriodMillis;
    private long durationMillis;

    @Before
    public void setUp() {
        mockObject = mock(Object.class);
        pollingPeriodMillis = 100;
        durationMillis = 500;

        verificationWithTimeout = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, mock(VerificationMode.class), true);
        verificationAfterDelay = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, new VerificationWithTimeout(pollingPeriodMillis, 1, mock(VerificationMode.class), false), false);
        verificationNoInteractions = new VerificationModeWithNoInteractions(pollingPeriodMillis, mock(VerificationMode.class));
        verificationAtMost = new AtMost(1, mock(VerificationMode.class));
        verificationNever = new NoMoreInteractions();

        when(mockObject).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void shouldSatisfyVerificationWithTimeout() {
        // given
        VerificationData data = verificationData(mockObject, "arg");

        // when
        verificationWithTimeout.verify(data);

        // then
        verify(mockObject, times(1)).equals("arg");
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowExceptionWhenVerificationWithTimeoutFails() {
        // given
        VerificationData data = verificationData(mockObject, ArgumentMatchers.not("arg"));

        // when
        verificationWithTimeout.verify(data);

        // then
        // this should throw an exception
    }

    @Test
    public void shouldWaitAndReturnSuccessWhenVerificationAfterDelaySucceeds() throws Exception {
        // given
        VerificationData data = verificationData(mockObject, ArgumentMatchers.eq("arg"));

        // when
        VerificationMode verificationMode = verificationAfterDelay;
        verificationMode.verify(data);

        // then
        verify(mockObject, times(1)).equals("arg");
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowExceptionWhenVerificationAfterDelayFails() throws Exception {
        // given
        VerificationData data = verificationData(mockObject, ArgumentMatchers.not("arg"));

        // when
        VerificationMode verificationMode = verificationAfterDelay;
        verificationMode.verify(data);

        // then
        // this should throw an exception
    }

    @Test
    public void shouldNotVerifyAnyInteractionWhenVerificationNoInteractions() {
        // given
        VerificationData data = verificationData(mockObject, ArgumentMatchers.any());

        // when
        verificationNoInteractions.verify(data);

        // then
        verifyZeroInteractions(mockObject);
    }

    @Test(expected = Assertions.AssertionError.class)
    public void shouldThrowExceptionWhenVerificationAtMostExceedsThreshold() {
        // given
        VerificationData data = verificationData(mockObject, ArgumentMatchers.anyInt());

        when(mockObject.hashCode()).thenReturn(42);

        // when
        verificationAtMost.verify(data);

        // then
        verify(mockObject, times(2)).hashCode();

        // this should throw an exception
    }

    @Test
    public void shouldNotVerifyAnyInteractionWhenVerificationNever() {
        // given
        VerificationData data = verificationData(mockObject, ArgumentMatchers.any());

        // when
        verificationNever.verify(data);

        // then
        verifyZeroInteractions(mockObject);
    }

    @After
    public void tearDown() {
        timer.reset();
    }
}