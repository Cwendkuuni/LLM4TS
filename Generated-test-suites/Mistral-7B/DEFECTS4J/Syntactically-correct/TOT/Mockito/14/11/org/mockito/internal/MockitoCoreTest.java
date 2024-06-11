import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.misusing.NotAMockException;

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;

public class MockitoCoreTest {

    @Mock
    private Object mockObject;

    @Mock
    private List mockList;

    @Captor
    private ArgumentCaptor<Object> captor;

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMockCreation() {
        Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
        verify(mockitoCore).mockingStarted(mockedObject, Object.class, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testMockCreationWithNullSettings() {
        mockitoCore.mock(Object.class, null);
    }

    @Test
    public void testStubbing() {
        when(mockObject).thenReturn("Hello World");
        String result = (String) mockitoCore.verify(mockObject);
        verify(mockObject).toString();
        reset(mockObject);
        when(mockObject).thenAnswer(invocation -> "Goodbye World");
        result = (String) mockitoCore.verify(mockObject);
        verify(mockObject).toString();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNullObject() {
        mockitoCore.verify(null, Mockito.verifyNoMoreInteractions());
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNonMock() {
        Object nonMock = new Object();
        mockitoCore.verify(nonMock, Mockito.verifyNoMoreInteractions());
    }

    @Test
    public void testResetMocks() {
        when(mockObject).thenReturn("Hello World");
        String result = (String) mockitoCore.verify(mockObject);
        verify(mockObject).toString();
        reset(mockObject);
        when(mockObject).thenReturn("Goodbye World");
        result = (String) mockitoCore.verify(mockObject);
        verify(mockObject).toString();
    }

    @Test
    public void testVerificationMode() {
        when(mockObject).thenReturn("Hello World");
        mockitoCore.verify(mockObject, Mockito.verifyNoMoreInteractions());
        mockitoCore.verify(mockObject, Mockito.verify(Mockito.eq("Hello World")));
        mockitoCore.verify(mockObject, Mockito.verifyNoInteractions());
    }

    @Test
    public void testInOrderVerification() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        when(mock1.toString()).thenReturn("Mock 1");
        when(mock2.toString()).thenReturn("Mock 2");

        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();

        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testDeprecatedStubbing() {
        when(mockObject).thenStub(new Stub() {
            @Override
            public void doStub() {
                mockitoCore.when(mockObject).thenReturn("Hello World");
            }
        });
        String result = (String) mockitoCore.verify(mockObject);
        verify(mockObject).toString();
    }

    @Test(expected = NotAMockException.class)
    public void testStubbingWithNonMock() {
        mockitoCore.stubbing(new Object()).thenReturn("Hello World");
    }

    @Test
    public void testOngoingStubbing() {
        IOngoingStubbing ongoingStubbing = mockitoCore.stub();
        ongoingStubbing.thenReturn("Hello World");
        String result = (String) mockitoCore.verify(mockObject, Mockito.argThat(arg -> arg.equals("Hello World")));
        verify(mockObject).toString();
    }

    @Test
    public void testVerificationData() {
        when(mockObject).thenReturn("Hello World");
        List<Object> arguments = Arrays.asList("Argument 1", "Argument 2");
        mockitoCore.verify(mockObject, Mockito.withSettings().displayName("Test Verification Data").argThat(arg -> arg.equals("Argument 1"))).thenReturn("Verified");
        Object result = mockitoCore.verify(mockObject, Mockito.withSettings().displayName("Test Verification Data").argThat(arg -> arg.equals("Argument 2"))).get(0);

        InOrder order = mockitoCore.inOrder(mockObject);
        order.verify(mockObject).toString();
        order.verify(mockObject).toString();
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testAnswer() {
        when(mockObject).thenAnswer(invocation -> "Hello World");
        String result = (String) mockitoCore.verify(mockObject);
        verify(mockObject).toString();
    }

    @Test
    public void testVoidMethodStubbable() {
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mockObject);
        voidMethodStubbable.answer("Hello World");
        mockitoCore.verify(mockObject).voidMethod();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        when(mockObject).thenReturn(42);
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        verify(mockObject).toString();
        assertEquals(42, lastInvocation.getArgument(0));
    }
}