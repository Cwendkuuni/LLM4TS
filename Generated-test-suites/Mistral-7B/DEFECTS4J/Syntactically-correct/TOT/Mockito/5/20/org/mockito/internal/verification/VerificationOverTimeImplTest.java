package org.mockito.internal.verification.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Stubber stubber;
    private VerificationData verificationData;
    private Timer timerSpy;

    @Before
    public void setUp() {
        verificationMode = mock(VerificationMode.class);
        stubber = Mockito.mock(Stubber.class);
        verificationData = new VerificationData(stubber, ArgumentMatchers.any());
        timerSpy = mock(Timer.class);
        given(timerSpy.isCounting()).willReturn(true);
        given(timerSpy.duration()).willReturn(100L);

        verificationMode = new VerificationOverTimeImpl(500, 1500, verificationMode, false, timerSpy);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerifyThrowsMockitoAssertionErrorBeforeTimeout() {
        // given
        given(verificationMode.getDelegate()).willReturn(new VerificationMode() {
            @Override
            public void verify(VerificationData data) {
                throw new MockitoAssertionError("Expected exception");
            }
        });

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerifyThrowsMockitoAssertionErrorAfterTimeout() {
        // given
        given(verificationMode.getDelegate()).willReturn(new VerificationMode() {
            @Override
            public void verify(VerificationData data) {
                throw new MockitoAssertionError("Expected exception");
            }
        });

        given(timerSpy.duration()).willReturn(50L);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
        verifyZeroInteractions(verificationMode);
        verify(timerSpy).stop();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // ignore
        }

        given(timerSpy.isCounting()).willReturn(false);
        given(timerSpy.duration()).willReturn(1500L);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testDelegateVerifySucceedsBeforeTimeout() {
        // given
        given(verificationMode.getDelegate()).will(invocation -> {
            InvocationOnMock invocation = (InvocationOnMock) invocation.getArguments()[0];
            invocation.answer(null);
        });

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testDelegateVerifySucceedsAfterTimeout() {
        // given
        given(verificationMode.getDelegate()).will(invocation -> {
            InvocationOnMock invocation = (InvocationOnMock) invocation.getArguments()[0];
            invocation.answer(null);
        });

        given(timerSpy.duration()).willReturn(1501L);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testDelegateVerifyReturnsImmediatelyOnceSucceeds() {
        // given
        given(verificationMode.getDelegate()).will(invocation -> {
            InvocationOnMock invocation = (InvocationOnMock) invocation.getArguments()[0];
            invocation.answer(null);
        });

        // when
        verificationMode = new VerificationOverTimeImpl(1, 1, verificationMode, true, timerSpy);
        verificationMode.verify(verificationData);

        // then
        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void testDelegateVerifyDoesNotReturnUntilSucceedsAndFullDurationPassed() {
        // given
        given(verificationMode.getDelegate()).will(invocation -> {
            InvocationOnMock invocation = (InvocationOnMock) invocation.getArguments()[0];
            invocation.answer(null);
        });

        // when
        verificationMode = new VerificationOverTimeImpl(1, 1, verificationMode, false, timerSpy);
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerifyThrowsArgumentAreDifferentExceptionBeforeTimeout() {
        // given
        given(verificationMode.getDelegate()).willThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent());

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerifyThrowsArgumentAreDifferentExceptionAfterTimeout() {
        // given
        given(verificationMode.getDelegate()).willThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent());

        given(timerSpy.duration()).willReturn(50L);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
        verifyZeroInteractions(verificationMode);
        verify(timerSpy).stop();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // ignore
        }

        given(timerSpy.isCounting()).willReturn(false);
        given(timerSpy.duration()).willReturn(1500L);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testDelegateVerifyDoesNotThrowArgumentAreDifferentExceptionBeforeTimeout() {
        // given
        given(verificationMode.getDelegate()).willAnswer(invocation -> null);

        // when
        verificationMode.verify(verificationData);

        // then
        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void testDelegateVerifyDoesNotThrowArgumentAreDifferentExceptionAfterTimeout() {
        // given
        given(verificationMode.getDelegate()).willAnswer(invocation -> null);

        given(timerSpy.duration()).willReturn(1501L);

        // when
        verificationMode.verify(verificationData);

        // then
        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void testCanRecoverFromFailure() {
        // given
        VerificationMode verificationMode = new VerificationMode() {
            @Override
            public void verify(VerificationData data) {
                throw new RuntimeException("Unexpected exception");
            }
        };

        // when
        boolean canRecover = verificationOverTimeImpl.canRecoverFromFailure(verificationMode);

        // then
        assertFalse(canRecover);
    }

    @Test
    public void testCanRecoverFromFailureForVerificationModeWhichCanRecover() {
        // given
        VerificationMode verificationMode = mock(VerificationMode.class);
        given(verificationMode.canRecoverFromFailure(any(VerificationMode.class))).willReturn(true);

        // when
        boolean canRecover = verificationOverTimeImpl.canRecoverFromFailure(verificationMode);

        // then
        assertTrue(canRecover);
    }

    private VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(500, 1500, verificationMode, false, timerSpy);
}