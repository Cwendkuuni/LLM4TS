import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doThrow;

public class VerificationOverTimeImplTest {
    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true);
    }

    @Test
    public void testVerifySuccess() {
        VerificationData data = Mockito.mock(VerificationData.class);
        verificationOverTime.verify(data);
        verify(delegate).verify(data);
    }

    @Test
    public void testVerifyFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new AssertionError("Error occurred"))
                .when(delegate)
                .verify(ArgumentMatchers.eq(data));
        AssertionError error = null;
        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            error = e;
        }
        verify(delegate).verify(data);
        verifyNoMoreInteractions(delegate);
        assertEquals("Error occurred", error.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativePollingPeriod() {
        new VerificationOverTimeImpl(-1, 500, delegate, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeDuration() {
        new VerificationOverTimeImpl(100, -1, delegate, true);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullDelegate() {
        new VerificationOverTimeImpl(100, 500, null, true);
    }

    @Test
    public void testVerifyExceptionHandleAssertionError() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new AssertionError("Error occurred"))
                .when(delegate)
                .verify(ArgumentMatchers.eq(data));

        AssertionError error = null;
        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            error = e;
        }

        assertEquals("Error occurred", error.getMessage());
    }

    @Test
    public void testVerifyExceptionHandleArgumentsAreDifferent() {
        VerificationData data1 = Mockito.mock(VerificationData.class);
        VerificationData data2 = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(data1, data2))
                .when(delegate)
                .verify(ArgumentMatchers.eq(data2));

        AssertionError error = null;
        try {
            verificationOverTime.verify(data1);
        } catch (AssertionError e) {
            error = e;
        }

        assertEquals("Expected: <[VerificationData@", error.getMessage().substring(0, 50), "] but: <[VerificationData@", error.getMessage().substring(error.getMessage().length() - 50), "]>", error.getMessage().substring(error.getMessage().length() - 100));
    }
}