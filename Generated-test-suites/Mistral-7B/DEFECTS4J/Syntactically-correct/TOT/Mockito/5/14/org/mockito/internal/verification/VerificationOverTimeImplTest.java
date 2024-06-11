import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private VerificationData data;

    @Mock
    private Timer timer;

    @Captor
    private ArgumentCaptor<VerificationData> dataCaptor;

    @InjectMocks
    private VerificationOverTimeImpl verificationMode;

    @Before
    public void setup() {
        when(timer.isCounting()).thenReturn(true);
        when(timer.duration()).thenReturn(0L);
        when(timer.start()).thenCallRealMethod();
        when(timer.stop()).thenCallRealMethod();
    }

    @Test
    public void testDelegateVerifiedImmediately() {
        // Given
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        verificationMode = new VerificationOverTimeImpl(1L, 10L, delegate, true);

        // When
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test
    public void testDelegateVerificationFails() {
        // Given
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Expected exception"))
                .when(delegate)
                .verify(data);

        verificationMode = new VerificationOverTimeImpl(1L, 10L, delegate, false);

        // When
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(verificationMode);

        // When (timeout)
        doCallRealMethod().when(timer).sleep(10L);
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
        verify(verificationMode).handleVerifyException(Mockito.any(AssertionError.class));
    }

    @Test
    public void testDelegateVerificationPassesAfterDelay() {
        // Given
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        when(delegate.verify(data)).thenCallRealMethod();
        when(timer.isCounting()).thenReturn(false);

        verificationMode = new VerificationOverTimeImpl(1L, 10L, delegate, false);

        // When
        verificationMode.verify(data);

        // Then
        verifyNoMoreInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(dataCaptor.capture(data));

        // Then
        verify(delegate).verify(dataCaptor.getValue());
        verifyZeroInteractions(verificationMode);

        // When (timeout)
        doCallRealMethod().when(timer).sleep(9L);
        verificationMode.verify(dataCaptor.getValue());

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(dataCaptor.getValue());

        // Then
        verify(delegate).verify(dataCaptor.getValue());
        verify(verificationMode).handleVerifyException(Mockito.any(AssertionError.class));
    }

    @Test
    public void testDelegateVerificationFailsAfterDelay() {
        // Given
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Expected exception"))
                .when(delegate)
                .verify(data);

        verificationMode = new VerificationOverTimeImpl(1L, 10L, delegate, false);

        // When
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (timeout)
        doCallRealMethod().when(timer).sleep(9L);
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
        verify(verificationMode).handleVerifyException(Mockito.any(AssertionError.class));
    }

    @Test
    public void testDelegateVerificationRecoverable() {
        // Given
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        doAnswer(invocationOnMock -> {
            throw new MockitoAssertionError("Expected exception");
            return null;
        })
                .when(delegate)
                .verify(data);

        verificationMode = new VerificationOverTimeImpl(1L, 10L, delegate, true);

        // When
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test
    public void testDelegateVerificationNonRecoverable() {
        // Given
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        doAnswer(invocationOnMock -> {
            throw new AssertionError("Unexpected exception");
            return null;
        })
                .when(delegate)
                .verify(data);

        verificationMode = new VerificationOverTimeImpl(1L, 10L, delegate, false);

        // When
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (timeout)
        doCallRealMethod().when(timer).sleep(9L);
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);

        // When (polling period)
        doCallRealMethod().when(timer).sleep(1L);
        verificationMode.verify(data);

        // Then
        verifyZeroInteractions(delegate);
        verifyZeroInteractions(verificationMode);
    }

    private InvocationOnMock invocationOnMock() {
        return Mockito.mock(InvocationOnMock.class);
    }
}