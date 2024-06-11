package org.mockito.internal.verification.impl; // replace with the correct package if needed

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*; // for given() and then()

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private Stubber stub;
    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setup() {
        stub = mockitoContext().stubber();
        delegate = stub.verify(ArgumentMatchers.any(VerificationData.class));
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);
    }

    @Test
    public void testDelegateIsSatisfiedBeforeTimeout() {
        // given

        // when
        verificationOverTime.verify(createVerificationData());

        // then
        // as the returnOnSuccess is true, the test will pass here
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateIsNotSatisfiedBeforeTimeout() {
        // given

        // when
        verificationOverTime.verify(createVerificationData());

        // then
        // the test will throw an exception here as the delegate is not satisfied and the full duration has passed
    }

    @Test
    public void testDelegateIsSatisfiedAfterTimeout() {
        // given

        // when
        doAnswer(invocation -> {
            try {
                Thread.sleep(600L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }).when(delegate).verify(any(VerificationData.class));

        // when
        verificationOverTime.verify(createVerificationData());

        // then
        // the test will pass here as the delegate is satisfied and the full duration has passed
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateIsNotSatisfiedAfterTimeout() {
        // given

        // when
        doAnswer(invocation -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException();
        }).when(delegate).verify(any(VerificationData.class));

        // when
        verificationOverTime.verify(createVerificationData());

        // then
        // the test will throw an exception here as the delegate is not satisfied and the full duration has passed
    }

    @Test
    public void testDelegateThrowsException() {
        // given
        doAnswer(invocation -> {
            throw new RuntimeException();
        }).when(delegate).verify(any(VerificationData.class));

        // when
        try {
            verificationOverTime.verify(createVerificationData());
        } catch (MockitoAssertionError e) {
            // expected exception
            then(delegate).should().verify(any(VerificationData.class));
            then(delegate).shouldHaveReceived(times(1)).verify(ArgumentMatchers.any(VerificationData.class));
            // mockito cannot know if the delegate will recover from the exception
            // add assertions to check if the delegate does recover as needed
        }
    }

    private VerificationData createVerificationData() {
        return new VerificationData(mock(InvocationOnMock.class), 0L);
    }

    @After
    public void tearDown() {
        stub.verifyNoMoreInteractions();
    }
}