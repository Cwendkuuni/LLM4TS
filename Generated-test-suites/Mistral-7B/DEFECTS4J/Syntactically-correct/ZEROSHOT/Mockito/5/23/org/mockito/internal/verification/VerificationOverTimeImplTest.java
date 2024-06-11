package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.util.Timer;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private Timer timer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 verificationMode = new VerificationOverTimeImpl(100, 500, Mockito.verificationMode(), true);
 timer = new Timer(500);
 }

 @Test
 public void testVerify_DelegateSatisfiedImmediately() {
 VerificationData data = Mockito.mock(VerificationData.class);
 verificationMode.verify(data);
 Mockito.verify(data).verifyNoMoreInteractions();
 }

 @Test(expected = org.mockito.exceptions.base.MockitoAssertionError.class)
 public void testVerify_DelegateNotSatisfiedWithinTimeout() {
 VerificationData data = Mockito.mock(VerificationData.class);
 Mockito.doThrow(new MockitoAssertionError("Test error")).when(data).verifyNoMoreInteractions();
 verificationMode.verify(data);
 }

 @Test
 public void testVerify_DelegateSatisfiedAfterTimeout() throws InterruptedException {
 VerificationData data = Mockito.mock(VerificationData.class);
 Mockito.doReturn(null).when(data).getVerificationErrors();

 Mockito.verify(data).verifyNoMoreInteractions();
 timer.start();
 Thread.sleep(10); // let the timer expire
 verificationMode.verify(data);
 }

 @Test
 public void testVerify_DelegateSatisfiedAfterDelay_ReturnOnSuccess() {
 VerificationData data = Mockito.mock(VerificationData.class);
 Mockito.doReturn(null).when(data).getVerificationErrors();

 verificationMode = new VerificationOverTimeImpl(100, 500, Mockito.verificationMode(), true, timer);
 verificationMode.verify(data);
 Mockito.verify(data).verifyNoMoreInteractions();
 }

 @Test
 public void testVerify_DelegateSatisfiedAfterDelay_NotReturnOnSuccess() throws InterruptedException {
 VerificationData data = Mockito.mock(VerificationData.class);
 Mockito.doReturn(null).when(data).getVerificationErrors();

 verificationMode = new VerificationOverTimeImpl(100, 500, Mockito.verificationMode(), false, timer);
 verificationMode.verify(data);
 Thread.sleep(600); // wait for the full timeout to pass
 Mockito.verify(data).verifyNoMoreInteractions();
 }

 @Test
 public void testCanRecoverFromFailure() {
 Assert.assertTrue(verificationMode.canRecoverFromFailure(Mockito.verificationMode()));
 Assert.assertFalse(verificationMode.canRecoverFromFailure(Mockito.atMost(1)));
 }

}