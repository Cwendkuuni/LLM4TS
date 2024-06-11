import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;
    private Timer timer;

    @Before
    public void before() {
        verificationMode = mock(VerificationMode.class);
        verificationData = mock(VerificationData.class);
        timer = mock(Timer.class);

        VerificationModeFactory verificationModeFactory = mock(VerificationModeFactory.class);
        when(verificationModeFactory.createVerificationMode(anyLong(), anyLong(), ArgumentMatchers.eq(verificationMode), ArgumentMatchers.eq(true)))
                .thenReturn(new VerificationOverTimeImpl(1, 500, verificationMode, true, timer));

        given(timer.duration()).willReturn(50L);
        given(timer.isCounting()).willReturn(true);
        given(timer.start()).willAnswer(invocation -> {});
        given(timer.stop()).willAnswer(invocation -> {});
        given(timer.isExpired()).willReturn(false);
        given(timer.elapsed()).willReturn(0L);
        given(timer.sleep(anyLong())).willDoNothing();
    }

    @Test
    public void testVerifySuccessWithinDuration() {
        // given

        // when
        doNothing().when(verificationMode).verify(verificationData);
        verificationMode.verify(verificationData);

        // then
        verifyNoMoreInteractions(verificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithinDuration() {
        // given
        willThrow(new MockitoAssertionError("Expected invocation on mock not found"))
                .given(verificationMode).verify(verificationData);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifySuccessAfterDuration() {
        // given
        willDoNothing().given(verificationMode).verify(verificationData);

        // when
        doCallRealMethod().when(verificationMode).verify(verificationData);
        verificationMode.verify(verificationData);

        // then
        verify(timer).sleep(ArgumentMatchers.eq(1L));
        verifyNoMoreInteractions(verificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureAfterDuration() {
        // given
        willThrow(new MockitoAssertionError("Expected invocation on mock not found"))
                .given(verificationMode).verify(verificationData);

        // when
        doCallRealMethod().when(verificationMode).verify(verificationData);
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
        verify(timer).sleep(ArgumentMatchers.eq(1L));
        verify(timer).stop();
        verifyThrows(MockitoAssertionError.class, verificationMode::verify, verificationData);
    }

    @Test
    public void testVerifyWithTimeoutException() {
        // given
        given(verificationMode.canRecoverFromFailure(ArgumentMatchers.eq(verificationMode)))
                .willReturn(false);
        willThrow(new MockitoAssertionError("Expected invocation on mock not found"))
                .given(verificationMode).verify(verificationData);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
        verify(timer).sleep(ArgumentMatchers.eq(1L));
        verify(timer).stop();
        verifyThrows(MockitoAssertionError.class, verificationMode::verify, verificationData);
    }

    @Test
    public void testVerifyWithNoMoreInteractionsException() {
        // given
        given(verificationMode.canRecoverFromFailure(ArgumentMatchers.eq(verificationMode)))
                .willReturn(false);
        willThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(""))
                .given(verificationMode).verify(verificationData);

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationMode).verify(verificationData);
        verify(timer).sleep(ArgumentMatchers.eq(1L));
        verify(timer).stop();
        verifyThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, verificationMode::verify, verificationData);
    }
}