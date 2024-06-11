package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private VerificationOverTimeImpl verificationOverTime;
 private Timer timer;

 @Before
 public void setUp() {
 verificationMode = mock(VerificationWithTimeout.class);
 timer = new Timer(1000);
 verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationMode, false, timer);
 }

 @Test
 public void testVerify_delegateSuccessImmediately_shouldReturnSuccess() throws InterruptedException {
 // Given
 verify(verificationMode).verify(ArgumentMatchers.any());

 // When
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));

 // Then
 assertTrue(verificationOverTime.isSatisfied());
 }

 @Test
 public void testVerify_delegateSuccessAfterTimeout_shouldThrowException() throws InterruptedException {
 // Given
 timer.setTimeRemaining(950);

 // When & Then
 try {
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));
 fail("Expected exception not thrown.");
 } catch (MockitoAssertionError e) {
 // Then
 assertNull(e.getMessage());
 }
 }

 @Test
 public void testVerify_delegateSuccessAfterTimeoutButAllowRecovery_shouldReturnSuccess() throws InterruptedException {
 // Given
 timer.setTimeRemaining(950);

 // When
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));

 // Sleep for polling period and retry
 sleep(verificationOverTime.getPollingPeriod());

 // Then
 assertTrue(verificationOverTime.isSatisfied());
 }

 @Test
 public void testVerify_delegateFailureImmediately_shouldThrowException() {
 // Given
 doThrow(new MockitoAssertionError("Mocking failure")).when(verificationMode).verify(ArgumentMatchers.any());

 // When & Then
 try {
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));
 fail("Expected exception not thrown.");
 } catch (MockitoAssertionError e) {
 // Then
 assertEquals("Mocking failure", e.getMessage());
 }
 }

 @Test
 public void testVerify_delegateFailureImmediately_shouldNotSatisfy() {
 // Given
 doThrow(new MockitoAssertionError("Mocking failure")).when(verificationMode).verify(ArgumentMatchers.any());

 // When
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));

 // Then
 assertFalse(verificationOverTime.isSatisfied());
 }

 @Test
 public void testVerify_delegateFailureAfterTimeout_shouldThrowException() throws InterruptedException {
 // Given
 timer.setTimeRemaining(950);
 doThrow(new MockitoAssertionError("Mocking failure")).when(verificationMode).verify(ArgumentMatchers.any());

 // When & Then
 try {
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));
 fail("Expected exception not thrown.");
 } catch (MockitoAssertionError e) {
 // Then
 assertNull(e.getMessage());
 }

 // Sleep for timeout
 Thread.sleep(verificationOverTime.getDuration());

 // When & Then
 try {
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));
 fail("Expected exception not thrown.");
 } catch (MockitoAssertionError e) {
 // Then
 assertEquals("Mocking failure", e.getMessage());
 }
 }

 @Test
 public void testVerify_delegateNeverRecoversFromFailure_shouldThrowException() throws InterruptedException {
 // Given
 doThrow(new MockitoAssertionError("Mocking failure")) // throws and never recovers from failure
 .when(verificationMode)
 .verify(ArgumentMatchers.any());

 // When & Then
 try {
 verificationOverTime.verify(new VerificationData(VerificationData.Type.CALL, null, null));
 fail("Expected exception not thrown.");
 } catch (MockitoAssertionError e) {
 // Then
 assertEquals("Mocking failure", e.getMessage());
 }

 // Sleep for timeout
 Thread.sleep(verificationOverTime.getDuration());

 // Then
 verifyNoInteractions(verificationMode);
 }

 @Test
 public void testGetPollingPeriod_shouldReturnExpectedValue() {
 // Then
 assertEquals(100, verificationOverTime.getPollingPeriod());
 }

 @Test
 public void testGetDuration_shouldReturnExpectedValue() {
 // Then
 assertEquals(1000, verificationOverTime.getDuration());
 }

 @Test
 public void testGetDelegate_shouldReturnExpectedValue() {
 // Then
 assertEquals(verificationMode, verificationOverTime.getDelegate());
 }
}