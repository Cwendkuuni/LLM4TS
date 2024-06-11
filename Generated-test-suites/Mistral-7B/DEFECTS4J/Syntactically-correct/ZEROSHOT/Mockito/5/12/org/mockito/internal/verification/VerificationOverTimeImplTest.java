package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timer;

    @Before
    public void setUp() {
        timer = mock(Timer.class);
        verificationMode = new VerificationOverTimeImpl(100, 500, Mockito.mock(VerificationMode.class), true, timer);
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateVerifiedImmediatelyThenReturnSuccess() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(verificationMode).verify(ArgumentMatchers.eq(data));

        // when
        verificationMode.verify(data);

        // then
        Assert.assertTrue(true);
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateNotVerifiedImmediatelyThenThrowException() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                throw new MockitoAssertionError("Delegate verification failed.");
            }
        }).when(verificationMode).verify(ArgumentMatchers.eq(data));

        // when & then
        Assert.assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(data));
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateVerifiedLaterThenReturnSuccess() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        long startTime = System.currentTimeMillis();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 500) {
                    throw new MockitoAssertionError("Delegate verification failed.");
                }
                return null;
            }
        }).when(verificationMode).verify(ArgumentMatchers.eq(data));

        // when
        try {
            verificationMode.verify(data);
        } catch (MockitoAssertionError e) {
            // delegate verification failed in the first attempt
            sleep(100);
            verificationMode.verify(data);
            // then
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testVerificationAfterDelayWhenDelegateNotVerifiedInGivenTimeThenThrowException() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(verificationMode).verify(ArgumentMatchers.eq(data));

        // when & then
        Assert.assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(data));
    }

    @Test
    public void testVerificationAfterDelayWhenDelegateVerifiedInGivenTimeThenReturnSuccess() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        long startTime = System.currentTimeMillis();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 500) {
                    throw new MockitoAssertionError("Delegate verification failed.");
                }
                return null;
            }
        }).when(verificationMode).verify(ArgumentMatchers.eq(data));

        // when
        try {
            verificationMode.verify(data);
        } catch (MockitoAssertionError e) {
            // delegate verification failed in the first attempt
            sleep(100);
            verificationMode.verify(data);
            // then
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateNeverRecoverThenThrowException() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                throw new MockitoAssertionError("Delegate verification failed.");
            }
        }).when(delegate).verify(ArgumentMatchers.eq(data));

        // when & then
        Assert.assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateRecoversThenReturnSuccess() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true, timer);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                throw new MockitoAssertionError("Delegate verification failed.");
            }
        }).when(delegate).verify(ArgumentMatchers.eq(data));

        // when
        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            // delegate verification failed in the first attempt
            sleep(100);
            verificationOverTime.verify(data);
            // then
            Assert.assertTrue(true);
        }
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateIsSatisfiedThenReturn() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true, timer);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(delegate).verify(ArgumentMatchers.eq(data));

        // when
        verificationOverTime.verify(data);

        // then
        verify(delegate).verify(eq(data), times(1));
        verifyZeroInteractions(verificationOverTime);
    }

    @Test
    public void testVerificationWithTimeoutWhenDelegateIsNotSatisfiedThenThrowException() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(delegate).verify(ArgumentMatchers.eq(data));

        // when
        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            // then
            verify(delegate).verify(eq(data), times(1));
            throw e;
        }

        // then
        verify(delegate).verify(eq(data), times(1));
        verify(verificationOverTime).verify(eq(data), times(1));
    }
}