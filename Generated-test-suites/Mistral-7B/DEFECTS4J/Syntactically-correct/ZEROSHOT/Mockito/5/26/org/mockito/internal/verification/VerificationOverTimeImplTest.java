package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private VerificationData verificationData;
 private Timer timer;

 @Test
 public void testVerificationOverTimeImplWithSuccessReturn() {
 verificationMode = new VerificationOverTimeImpl(100L, 500L, new VerificationWithTimeout(200L), true);
 verificationData = Mockito.mock(VerificationData.class);
 timer = Mockito.spy(new Timer(500L));

 verificationMode.verify(verificationData);
 Mockito.verify(timer).stop();
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationOverTimeImplWithoutSuccessReturn() {
 verificationMode = new VerificationOverTimeImpl(100L, 500L, new VerificationWithTimeout(200L), false);
 verificationData = Mockito.mock(VerificationData.class);
 timer = Mockito.spy(new Timer(500L));

 Mockito.doThrow(new MockitoAssertionError("Verification failed"))
 .when(verificationData).getInvocations();

 verificationMode.verify(verificationData);
 Mockito.verify(timer).stop();
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationOverTimeImplWithFailureModeThatCantRecover() {
 verificationMode = new VerificationOverTimeImpl(100L, 500L, new AtMost(1), false);
 verificationData = Mockito.mock(VerificationData.class);
 timer = Mockito.spy(new Timer(500L));

 Mockito.doThrow(new MockitoAssertionError("Verification failed"))
 .when(verificationData).getInvocations();

 verificationMode.verify(verificationData);
 }

 @Test
 public void testGetPollingPeriod() {
 verificationMode = new VerificationOverTimeImpl(100L, 500L, new VerificationWithTimeout(200L), true);
 Assert.assertEquals(100L, verificationMode.getPollingPeriod());
 }

 @Test
 public void testGetDuration() {
 verificationMode = new VerificationOverTimeImpl(100L, 500L, new VerificationWithTimeout(200L), true);
 Assert.assertEquals(500L, verificationMode.getDuration());
 }

 @Test
 public void testGetDelegate() {
 verificationMode = new VerificationOverTimeImpl(100L, 500L, new VerificationWithTimeout(200L), true);
 Assert.assertSame(VerificationWithTimeout.class, verificationMode.getDelegate().getClass());
 }

}