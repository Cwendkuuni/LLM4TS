package org.mockito.internal.verification.impl; // replace with the actual package if necessary

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timerSpy;

    @Before
    public void setUp() {
        this.verificationMode = Mockito.spy(new VerificationOverTimeImpl(100L, 500L, Mockito.mock(VerificationMode.class), true));
        this.timerSpy = Mockito.spy(new Timer(500L));
        Mockito.doReturn(this.timerSpy).when(verificationMode).getTimer();
    }

    @Test
    public void testVerificationWithDelegateSatisfiedBeforeTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);
        // Given
        Mockito.doAnswer(invocation -> {
            // delegate verification mode is assumed to be satisfied here
            return null;
        }).when(verificationMode.getDelegate()).verify(ArgumentMatchers.eq(data));

        // When
        this.verificationMode.verify(data);

        // Then
        Assert.assertTrue("Verify should have returned successfully", this.verificationMode.isSatisfied());
    }

    @Test
    public void testVerificationWithDelegateSatisfiedAfterTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);
        // Given
        Mockito.doThrow(new MockitoAssertionError("Delegate verification error"))
                .when(verificationMode.getDelegate())
                .verify(ArgumentMatchers.eq(data));

        // When
        this.verificationMode.verify(data);

        // Then
        // verify that timer waits for the full duration before throwing exception
        this.timerSpy.assertWaitCalledWith(500L);
        Assert.assertFalse("Verify should not have been satisfied before full duration", this.verificationMode.isSatisfied());
        Assert.assertThrows(MockitoAssertionError.class, () -> this.verificationMode.verify(data));
    }

    @Test
    public void testVerificationWithDelegateUnsatisfiedBeforeTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);
        // Given
        Mockito.doThrow(new MockitoAssertionError("Delegate verification error"))
                .when(verificationMode.getDelegate())
                .verify(ArgumentMatchers.eq(data));

        // When
        this.verificationMode.verify(data);

        // Then
        // verify that timer waits for the polling period before checking again
        this.timerSpy.assertWaitCalledWith(100L);
        Assert.assertFalse("Verify should not have been satisfied before full duration", this.verificationMode.isSatisfied());
    }

    @Test
    public void testVerificationWithDelegateUnsatisfiedAfterTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);
        // Given
        Mockito.doThrow(new MockitoAssertionError("Delegate verification error"))
                .when(verificationMode.getDelegate())
                .verify(ArgumentMatchers.eq(data));

        // When
        this.verificationMode.verify(data);

        // Then
        // verify that timer waits for the full duration before throwing exception
        this.timerSpy.assertWaitCalledWith(500L);
        Assert.assertFalse("Verify should not have been satisfied before full duration", this.verificationMode.isSatisfied());
        Assert.assertThrows(MockitoAssertionError.class, () -> this.verificationMode.verify(data));
    }
}