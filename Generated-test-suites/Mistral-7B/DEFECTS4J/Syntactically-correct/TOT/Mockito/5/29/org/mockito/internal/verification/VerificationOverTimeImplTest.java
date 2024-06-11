import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import static org.junit.Assert.assertThrows;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationWithTimeout;
 private VerificationMode verificationAfterDelay;
 private VerificationOverTimeImpl verificationOverTime;

 @Before
 public void setup() {
 verificationWithTimeout = Mockito.mock(VerificationMode.class, VerificationModeFactory.withAnswers(VerificationModeFactory.VERIFICATION_WITH_TIMEOUT));
 verificationAfterDelay = Mockito.mock(VerificationMode.class, VerificationModeFactory.withAnswers(VerificationModeFactory.VERIFICATION_AFTER_DELAY));
 verificationOverTime = new VerificationOverTimeImpl(100, 500, verificationWithTimeout, false);
 }

 @Test
 public void testVerifyWithTimeoutSuccess() {
 // Given: a verification with a timeout
 // When: the delegate is satisfied within the timeout
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
 verificationWithTimeout.verify(data);

 // Then: the delegate verification should be satisfied and the VerificationOverTime should also be satisfied
 verificationOverTime.verify(data);
 }

 @Test
 public void testVerifyWithTimeoutFailure() {
 // Given: a verification with a timeout
 // When: the delegate is not satisfied within the timeout
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
 Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied")).when(verificationWithTimeout).verify(data);

 // Then: an exception should be thrown by the VerificationOverTime
 assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
 }

 @Test
 public void testVerifyAfterDelaySuccess() {
 // Given: a verification with a delay
 // When: the delegate is satisfied after the delay
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
 Mockito.doReturn(() -> {}).when(verificationWithTimeout).verify(data);

 // Then: the delegate verification should be satisfied and the VerificationOverTime should also be satisfied
 verificationOverTime.verify(data);
 }

 @Test
 public void testVerifyAfterDelayFailure() {
 // Given: a verification with a delay
 // When: the delegate is not satisfied after the delay
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
 Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied")).when(verificationWithTimeout).verify(data);

 // Then: an exception should be thrown by the VerificationOverTime
 assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
 }

 @Test
 public void testVerifyWithTimeoutAndDelaySuccess() {
 // Given: a verification with both a timeout and a delay
 // When: the delegate is satisfied within the timeout and after the delay
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
 Mockito.doReturn(() -> {}).when(verificationWithTimeout).verify(data);

 // Then: the delegate verification should be satisfied and the VerificationOverTime should also be satisfied
 verificationOverTime.verify(data);
 }

 @Test
 public void testVerifyWithTimeoutAndDelayFailure() {
 // Given: a verification with both a timeout and a delay
 // When: the delegate is not satisfied within the timeout and after the delay
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
 Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied")).when(verificationWithTimeout).verify(data);

 // Then: an exception should be thrown by the VerificationOverTime
 assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
 }
}