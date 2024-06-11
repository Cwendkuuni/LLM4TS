package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.verification.api.VerificationData.verificationData;
import static org.mockito.verification.VerificationModeFactory.times;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.api.VerificationModeFactory;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.Waiter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class VerificationOverTimeImplTest {
    private static final long POLLING_PERIOD_MILLIS = 100;
    private static final long DURATION_MILLIS = 1000;
    private static final long DELAY_MILLIS = 500;
    private static final long TIMEOUT_MILLIS = 1500;
    private static final int INVOCATION_TIMES = 3;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private VerificationMode delegate;
    private VerificationMode verificationMode;
    private Timer timer;

    @Before
    public void setUp() {
        delegate = times(INVOCATION_TIMES);
        verificationMode = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, delegate, false);
        timer = mockitoRule.mock(Timer.class);
    }

    @Test
    public void testVerifyWithTimeoutSuccess() {
        VerificationData data = verificationData(mock(InvocationOnMock.class));

        Answer<Void> answer = invocation -> {
            // simulate success
            return null;
        };

        mockitoRule.injectMocks(verificationMode);
        mockitoRule.injectMocks(timer);

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(verificationMode, DELAY_MILLIS);

        delegate.answer(answer);

        verificationMode.verify(data);
    }

    @Test
    public void testVerifyWithTimeoutFailure() {
        VerificationData data = verificationData(mock(InvocationOnMock.class));

        Answer<Void> answer = invocation -> {
            throw new RuntimeException("Failed to verify");
        };

        mockitoRule.injectMocks(verificationMode);
        mockitoRule.injectMocks(timer);

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(verificationMode, DELAY_MILLIS);

        delegate.answer(answer);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationMode.verify(data);
        });
    }

    @Test
    public void testVerifyWithTimeoutRecoverableFailure() {
        VerificationData data = verificationData(mock(InvocationOnMock.class));

        Answer<Void> answer = invocation -> {
            throw new IllegalStateException("Failed to verify");
        };

        mockitoRule.injectMocks(verificationMode);
        mockitoRule.injectMocks(timer);

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(verificationMode, DELAY_MILLIS);

        delegate.answer(answer);

        waitFor(verificationMode);

        assertNotNull(verificationMode.getDelegate());
        assertFalse(verificationMode.isSatisfied());
    }

    @Test
    public void testVerifyWithTimeoutNonRecoverableFailure() {
        VerificationData data = verificationData(mock(InvocationOnMock.class));

        Answer<Void> answer = invocation -> {
            throw new AssertionError("Failed to verify");
        };

        mockitoRule.injectMocks(verificationMode);
        mockitoRule.injectMocks(timer);

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(verificationMode, DELAY_MILLIS);

        delegate.answer(answer);

        waitFor(verificationMode);

        assertInstanceOf(AssertionError.class, verificationMode.getLastErrorThrown());
    }

    @Test
    public void testVerifyWithTimeoutTimeout() {
        VerificationData data = verificationData(mock(InvocationOnMock.class));

        Answer<Void> answer = invocation -> {
            // do not throw an exception or return null
        };

        mockitoRule.injectMocks(verificationMode);
        mockitoRule.injectMocks(timer);

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(verificationMode, DELAY_MILLIS);

        delegate.answer(answer);

        waitFor(verificationMode, TIMEOUT_MILLIS);

        assertInstanceOf(MockitoAssertionError.class, verificationMode.getLastErrorThrown());
    }

    @Test
    public void testGetPollingPeriod() {
        assertEquals(POLLING_PERIOD_MILLIS, verificationMode.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        assertEquals(DURATION_MILLIS, verificationMode.getDuration());
    }

    @Test
    public void testGetDelegate() {
        assertEquals(delegate, verificationMode.getDelegate());
    }

    private void waitFor(VerificationOverTimeImpl verificationMode) {
        waitFor(verificationMode, Long.MAX_VALUE);
    }

    private void waitFor(VerificationOverTimeImpl verificationMode, long timeout) {
        Waiter waiter = mockitoRule.mock(Waiter.class);

        doAnswer(invocation -> {
            Thread.sleep((long) ((Object) invocation.getArguments()[0]).longValue());
            return null;
        }).when(waiter).wait(anyLong());

        mockitoRule.injectMocks(verificationMode);
        mockitoRule.injectMocks(timer);
        mockitoRule.injectMocks(waiter);

        verificationMode.verify(verificationData(mock(InvocationOnMock.class)));

        mockitoRule.verify(waiter).wait(eq(POLLING_PERIOD_MILLIS));

        if (timeout != Long.MAX_VALUE) {
            timer.stop();
        }
    }
}