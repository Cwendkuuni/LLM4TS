package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private VerificationOverTimeImpl verificationOverTimeImpl;

 @Before
 public void setUp() {
 verificationMode = mock(VerificationMode.class);
 verificationOverTimeImpl = new VerificationOverTimeImpl(100L, 500L, verificationMode, true);
 }

 @Test
 public void testVerificationWithSuccess() {
 VerificationData data = Mockito.mock(VerificationData.class);
 verificationOverTimeImpl.verify(data);

 // since returnOnSuccess is set to true, the test should pass here
 verify(verificationMode, times(1)).verify(data);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationWithoutSuccess() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode throws an exception
 doThrow(new MockitoAssertionError("Exception message")).when(verificationMode).verify(data);

 // the test should still fail since the verificationMode threw an exception
 verificationOverTimeImpl.verify(data);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationWithLongPollingPeriodAndFailure() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode throws an exception
 doThrow(new MockitoAssertionError("Exception message")).when(verificationMode).verify(data);

 // since the polling period is long and an exception was thrown, the test should eventually fail
 verificationOverTimeImpl.verify(data);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationWithLongDurationAndFailure() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode throws an exception
 doThrow(new MockitoAssertionError("Exception message")).when(verificationMode).verify(data);

 // since the duration is long, the test should eventually fail
 verificationOverTimeImpl.verify(data);
 }

 @Test
 public void testVerificationWithSuccessAndLongDuration() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode succeeds eventually
 doAnswer(invocationOnMock -> {
 // sleep for some time to simulate that the verificationMode takes some time
 try {
 Thread.sleep(501L);
 } catch (InterruptedException e) {
 // handle interruptions
 }
 return null;
 }).when(verificationMode).verify(data);

 // since returnOnSuccess is set to true, the test should pass once the verificationMode succeeds
 verificationOverTimeImpl.verify(data);

 // verify that the verificationMode was called the expected number of times
 verify(verificationMode, times(1)).verify(data);
 }

 @Test
 public void testVerificationWithFailureAndLongDuration() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode never succeeds
 doThrow(new MockitoAssertionError("Exception message")).when(verificationMode).verify(data);

 // since the duration is long, the test should eventually fail
 verificationOverTimeImpl.verify(data);

 // verify that the verificationMode was never called after the failure
 verifyNoMoreInteractions(verificationMode);
 }

 @Test
 public void testVerificationWithSuccessAndLongPollingPeriod() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode succeeds eventually
 doAnswer(invocationOnMock -> {
 // sleep for some time to simulate that the verificationMode takes some time
 try {
 Thread.sleep(101L);
 } catch (InterruptedException e) {
 // handle interruptions
 }
 return null;
 }).when(verificationMode).verify(data);

 // since returnOnSuccess is set to true, the test should pass once the verificationMode succeeds
 verificationOverTimeImpl.verify(data);

 // verify that the verificationMode was called the expected number of times
 verify(verificationMode, times(1)).verify(data);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationWithFailureAndLongPollingPeriod() {
 VerificationData data = Mockito.mock(VerificationData.class);

 // simulate that the verificationMode never succeeds
 doThrow(new MockitoAssertionError("Exception message")).when(verificationMode).verify(data);

 // since the polling period is long, the test should eventually fail
 verificationOverTimeImpl.verify(data);

 // verify that the verificationMode was never called after the failure
 verifyNoMoreInteractions(verificationMode);
 }

}