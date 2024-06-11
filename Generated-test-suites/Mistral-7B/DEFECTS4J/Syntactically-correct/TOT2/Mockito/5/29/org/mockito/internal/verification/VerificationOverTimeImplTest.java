import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.VerificationDataBuilder;
import org.mockito.internal.verification.api.Timer;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTimeImpl;
    private Timer timer;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = new Timer(1000);
        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, delegate, true, timer);
    }

    @Test
    public void testVerificationOverTimeImplConstructor() {
        Assert.assertEquals(100, verificationOverTimeImpl.getPollingPeriod());
        Assert.assertEquals(500, verificationOverTimeImpl.getDuration());
        Assert.assertNotNull(verificationOverTimeImpl.getDelegate());
    }
}