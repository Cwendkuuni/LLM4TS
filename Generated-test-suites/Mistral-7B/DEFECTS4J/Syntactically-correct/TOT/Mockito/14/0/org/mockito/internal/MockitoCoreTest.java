import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private MockSettings mockSettings;
    private Object mock;
    private InOrder inOrder;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        mockSettings = new MockSettingsImpl();
        mock = mockitoCore.mock(String.class, mockSettings);
        inOrder = mockitoCore.inOrder(mock);
    }

    @Test
    public void testMockCreation() {
        Mockito.verify(mockitoCore).mock(String.class, mockSettings);
        Mockito.verifyNoMoreInteractions(mockitoCore);
    }

    @Test
    public void testStubbing() {
        stub(mock).returns("test");
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verify(mock).hashCode();
        String result = (String) mockitoCore.getLastInvocation().getArgument(0);
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verify(mock).hashCode();
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verify(mock).hashCode();
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verifyNoMoreInteractions(mock);

        Object[] mocks = new Object[]{mock};
        mockitoCore.reset(mocks);
        Mockito.verify(mockitoCore).reset(mocks);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNullMock() {
        mockitoCore.verify(null, Mockito.eq(VerificationMode.times(1)));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        mockitoCore.verify(notAMock, Mockito.eq(VerificationMode.times(1)));
    }

    @Test
    public void testVerifyInOrder() {
        Object mock1 = mockitoCore.mock(String.class, mockSettings);
        Object mock2 = mockitoCore.mock(String.class, mockSettings);
        inOrder.verify(mock1).hashCode();
        inOrder.verify(mock1).equals(mock2);
        inOrder.verify(mock2).hashCode();
        inOrder.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2));

        mockitoCore.verifyNoMoreInteractionsInOrder(new Object[]{mock1, mock2}, inOrder);

        reset(mock1);
        reset(mock2);
        mockitoCore.verifyNoMoreInteractions();
    }

    @Test
    public void testAnswer() {
        Answer answer = invocation -> "answer";
        when(mock).thenAnswer(answer);
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verify(mock).hashCode();
        String result = (String) mockitoCore.getLastInvocation().getReturnValue();
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verify(mock).hashCode();
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verify(mock).hashCode();
        Mockito.verify(mock).toString();
        Mockito.verify(mock).equals(mock);
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVoidStub() {
        mockitoCore.stubVoid(mock);
        mockitoCore.mockingProgress.stubbingStarted();
        mockitoCore.mockingProgress.resetOngoingStubbing();
        mockitoCore.mockitoCore.doAnswer(answer -> {}).stub(mock);
        mockitoCore.mockingProgress.stubbingStarted();
        mockitoCore.mockitoCore.reset(mock);
    }

    @Test
    public void testUsageValidation() {
        mockitoCore.validateMockitoUsage();
    }
}