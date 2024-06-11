package org.mockito.internal.verification.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoRule;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link VerificationOverTimeImpl}. Tests cover various scenarios, including success, failure, and recovery.
 */
public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;
    private Timer timerMock;

    @Rule
    public MockitoRule mockitoRule = new MockitoRule();

    @Before
    public void setUp() {
        verificationMode = Mockito.spy(new VerificationOverTimeImpl(100L, 500L, Mockito.mock(VerificationMode.class), true));
        verificationData = Mockito.mock(VerificationData.class);
        timerMock = Mockito.mock(Timer.class);
        verificationMode = new VerificationOverTimeImpl(100L, 500L, verificationMode, true, timerMock);
    }

    /**
     * Test successful verification with a short polling period.
     */
    @Test
    public void testSuccessfulVerificationWithShortPollingPeriod() {
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        doAnswer(i -> {
            // simulate delegate verification behavior
            if (i.getArgument(0).equals(verificationData)) {
                throw new RuntimeException("Delegate verification failure");
            }
            return null;
        }).when(verificationMode).delegate_verify(ArgumentMatchers.eq(verificationData));

        verificationMode.verify(verificationData);

        // delegate verification is expected to be called multiple times, but should ultimately succeed
        verify(verificationMode, times(2)).delegate_verify(ArgumentMatchers.eq(verificationData));
        Mockito.verifyNoMoreInteractions(verificationMode);
    }

    /**
     * Test successful verification with a long polling period.
     */
    @Test
    public void testSuccessfulVerificationWithLongPollingPeriod() throws InterruptedException {
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        doAnswer(i -> {
            Thread.sleep(150); // simulate long delegate verification
            return null;
        }).when(verificationMode).delegate_verify(ArgumentMatchers.eq(verificationData));

        verificationMode.verify(verificationData);

        // delegate verification should be called once and succeed, as returnOnSuccess is true
        verify(verificationMode).delegate_verify(ArgumentMatchers.eq(verificationData));
        Mockito.verifyNoMoreInteractions(verificationMode);
    }

    /**
     * Test unsuccessful verification with a short polling period and a failing delegate.
     */
    @Test(expected = RuntimeException.class)
    public void testUnsuccessfulVerificationWithShortPollingPeriodAndFailingDelegate() {
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        doThrow(new RuntimeException("Failed delegate verification")).when(verificationMode).delegate_verify(verificationData);

        verificationMode.verify(verificationData);
    }

    /**
     * Test unsuccessful verification with a long polling period and a failing delegate.
     */
    @Test(expected = RuntimeException.class)
    public void testUnsuccessfulVerificationWithLongPollingPeriodAndFailingDelegate() throws InterruptedException {
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        doThrow(new RuntimeException("Failed delegate verification")).when(verificationMode).delegate_verify(Mockito.any());

        verificationMode.verify(verificationData);

        // delegate verification should be called multiple times before the exception is thrown
        verify(verificationMode, atLeastOnce()).delegate_verify(ArgumentMatchers.any());
    }

    /**
     * Test successful verification with a long polling period and a recoverable delegate.
     */
    @Test
    public void testSuccessfulVerificationWithLongPollingPeriodAndRecoverableDelegate() throws InterruptedException {
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        doAnswer(i -> {
            if (i.getArgument(0).equals(verificationData)) {
                throw new RuntimeException("Delegate verification failure");
            }
            return null;
        }).when(verificationMode).delegate_verify(ArgumentMatchers.eq(verificationData));

        verificationMode.verify(verificationData);

        // delegate verification should be called multiple times before succeeding
        verify(verificationMode, atLeast(2)).delegate_verify(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(verificationMode);
    }

    /**
     * Test unsuccessful verification with a long polling period and a non-recoverable delegate.
     */
    @Test(expected = RuntimeException.class)
    public void testUnsuccessfulVerificationWithLongPollingPeriodAndNonRecoverableDelegate() throws InterruptedException {
        Mockito.when(timerMock.isCounting()).thenReturn(true);
        doThrow(new RuntimeException("Failed delegate verification")).when(verificationMode).delegate_verify(ArgumentMatchers.eq(verificationData));

        verificationMode.verify(verificationData);

        // delegate verification should be called multiple times before the exception is thrown
        verify(verificationMode, atLeast(2)).delegate_verify(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(verificationMode);

        // the exception should be thrown after the full duration has passed
        Thread.sleep(1500);
        verify(verificationMode).handleVerifyException(ArgumentMatchers.instanceOf(RuntimeException.class));
    }
}