import org.junit.Test;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stub.Stubber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        Stubber stubber = mock(Stubber.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationData verificationData = mock(VerificationData.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationMode, true);

        verificationOverTime.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }
}