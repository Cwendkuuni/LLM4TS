import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

 private VerificationOverTimeImpl verificationMode;
 private VerificationData verificationData;
 private VerificationMode delegate;

 @Before
 public void setup() {
 delegate = mock(VerificationWithTimeout.class, Strictness.LENIENT);
 verificationData = mock(VerificationData.class);
 verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, true);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateIsNotSatisfiedBeforeTimeout() {
 // Given
 Mockito.when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate should not be satisfied"));

 // When & Then
 verificationMode.verify(verificationData);
 }

 @Test
 public void testDelegateIsSatisfiedBeforeTimeoutAndReturnOnSuccessIsTrue() {
 // Given
 Mockito.when(delegate.verify(verificationData)).thenReturn(null);

 // When & Then
 verificationMode.verify(verificationData);

 // Then delegate was called with the correct argument
 verify(delegate).verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateIsNotSatisfiedAfterTimeout() {
 // Given
 Mockito.when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate should not be satisfied"));

 // When
 verificationMode.verify(verificationData);

 // Wait for the timeout to elapse
 try {
 Thread.sleep(VerificationOverTimeImpl.class.getField("durationMillis").getLong(verificationMode) + 100L);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }

 // Then delegate was not called and an exception is thrown
 verify(delegate, Mockito.never()).verify(verificationData);
 Mockito.verifyZeroInteractions(delegate);
 }

 @Test
 public void testDelegateIsSatisfiedAfterTimeoutAndReturnOnSuccessIsFalse() {
 // Given
 Mockito.when(delegate.verify(verificationData)).thenReturn(null);

 // When
 verificationMode.verify(verificationData);

 // Wait for the timeout to elapse
 try {
 Thread.sleep(VerificationOverTimeImpl.class.getField("durationMillis").getLong(verificationMode) + 100L);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }

 // Then delegate was called and an exception is not thrown
 verify(delegate).verify(verificationData);
 }
}