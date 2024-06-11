import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

 private VerificationOverTimeImpl verificationMode;
 private VerificationData verificationData;
 private Timer timerSpy;

 @Before
 public void setup() {
 verificationData = Mockito.spy(new VerificationData());
 timerSpy = mock(Timer.class);
 verificationMode = new VerificationOverTimeImpl(100L, 500L, Mockito.mock(VerificationMode.class), true, timerSpy);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateIsNotSatisfiedBeforeTimeout() {
 when(verificationMode.delegate.verify(ArgumentMatchers.eq(verificationData)))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 throw new MockitoAssertionError("Delegate verification failed.");
 }
 });

 verificationMode.verify(verificationData);
 }

 @Test
 public void testDelegateIsSatisfiedBeforeTimeoutAndReturnOnSuccess() {
 when(verificationMode.delegate.verify(ArgumentMatchers.eq(verificationData)))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });

 verificationMode.verify(verificationData);
 }

 @Test
 public void testDelegateIsSatisfiedAfterTimeout() {
 when(verificationMode.delegate.verify(ArgumentMatchers.eq(verificationData)))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 Thread.sleep(600L);
 return null;
 }
 });

 timerSpy.start();
 verificationMode.verify(verificationData);
 verify(verificationMode.delegate).verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateIsNotSatisfiedAfterTimeout() {
 when(verificationMode.delegate.verify(ArgumentMatchers.eq(verificationData)))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 throw new MockitoAssertionError("Delegate verification failed.");
 }
 });

 timerSpy.start();
 verificationMode.verify(verificationData);
 verify(verificationMode.delegate).verify(ArgumentMatchers.eq(verificationData));
 verifyZeroInteractions(verificationMode.delegate);
 }

 @Test
 public void testDelegateRecoversFromFailure() {
 when(verificationMode.delegate.verify(ArgumentMatchers.eq(verificationData)))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 throw new MockitoAssertionError("Delegate verification failed.");
 }
 });

 verificationMode.verify(verificationData);
 verify(verificationMode.delegate).verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateDoesNotRecoverFromFailure() {
 when(verificationMode.delegate.verify(ArgumentMatchers.eq(verificationData)))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 throw new MockitoAssertionError("Delegate verification failed.");
 }
 });

 verificationMode.verify(verificationData);
 verify(verificationMode.delegate).verify(verificationData);
 verifyNoMoreInteractions(verificationMode.delegate);
 }

 @Test
 public void testGetters() {
 assertEquals(100L, verificationMode.getPollingPeriod());
 assertEquals(500L, verificationMode.getDuration());
 assertNotNull(verificationMode.getDelegate());
 }
}