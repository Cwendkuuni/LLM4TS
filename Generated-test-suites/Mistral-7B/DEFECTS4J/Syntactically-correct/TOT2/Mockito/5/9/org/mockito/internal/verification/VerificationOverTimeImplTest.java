import static org.mockito.Mockito.*;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);
        VerificationData data = new VerificationData("someMethod", eq("someArgument"), any());

        verificationMode.verify(data);
        verify(delegate).verify(data);
    }
}