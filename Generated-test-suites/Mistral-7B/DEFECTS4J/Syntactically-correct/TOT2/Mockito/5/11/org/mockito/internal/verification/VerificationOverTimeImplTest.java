import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;
import org.mockito.verification.VerifiedCollector;

import static org.mockito.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationModeMock;
    private Timer timerMock;
    private VerificationOverTimeImpl verificationOverTime;
    private VerifiedCollector<InvocationOnMock> verificationCollector;

    @Before
    public void setup() {
        verificationModeMock = mock(VerificationMode.class);
        timerMock = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationModeMock, false, timerMock);
        verificationCollector = Mockito.mock(VerifiedCollector.class);
    }

    @Test
    public void testVerify() {
        VerificationData verificationData = Mockito.mock(VerificationData.class);
        given(verificationModeMock.verify(ArgumentMatchers.eq(verificationData))).thenReturn(verificationCollector);

        verificationOverTime.verify(verificationData);
        verify(verificationModeMock).verify(verificationData);
        verify(verificationCollector).thenVerify(times(1));
    }
}