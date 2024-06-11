import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.junit.jUpgrade.TestMethodsReturnDefault;
import org.mockito.verification.VerificationModeFactory;
import static org.mockito.Mockito.*; // import static for testing purposes

@TestMethodsReturnDefault
public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationData = mock(VerificationData.class);
        verificationMode = new VerificationOverTimeImpl(1000, 5000, VerificationModeFactory.createNiceAndQuiet(), true);
    }

    @Test
    public void testVerifySuccess() {
        verificationMode.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }

}