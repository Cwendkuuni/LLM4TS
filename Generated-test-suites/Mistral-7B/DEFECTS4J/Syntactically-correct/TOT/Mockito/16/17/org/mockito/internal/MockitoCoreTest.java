import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Mock
    private Object mock;

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMockCreation() {
        Class<?> mockClass = Mockito.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettingsImpl mockSettingsWithName = new MockSettingsImpl().named("TestMock");

        Object mockObject = mockitoCore.mock(mockClass, mockSettings, false);
        Object namedMockObject = mockitoCore.mock(mockClass, mockSettingsWithName, false);

        assertEquals(mockClass, mockitoCore.mock(mockClass, mockSettings, false).getClass());
        assertEquals(mockClass, mockitoCore.mock(mockClass, mockSettingsWithName, false).getClass());

        verify(mockitoCore).mockingProgressValidateState();
        verify(mockitoCore).mockingProgressReset();
        verify(mockitoCore).mockUtilCreateMock(eq(mockClass), eq(mockSettings));

        assertEquals(mockObject, mockitoCore.mock(mockClass, mockSettings, false));
        assertEquals(namedMockObject, mockitoCore.mock(mockClass, mockSettingsWithName, false));
        assertEquals(mockObject, mockitoCore.mock(mockClass, mockSettingsWithName, false));
    }

    @Test
    public void testStubbing() {
        when(mock.method()).thenReturn("testValue");

        Object result = mockitoCore.verify(mock, eq(VerificationMode.RETURN_VALUES));
        assertEquals("testValue", result);

        verify(mock).method();

        Answer<Integer> answer = invocation -> 42;
        StubberImpl stubber = (StubberImpl) mockitoCore.doAnswer(answer);

        when(mock.method()).thenAnswer(answer);

        Integer resultAnswer = mockitoCore.verify(mock, eq(VerificationMode.RETURN_ANSWERS)).method();
        assertEquals(42, resultAnswer.intValue());

        Invocation lastInvocation = mockitoCore.getLastInvocation();
        ArgumentMatchers.MethodRef<Object, Integer> methodRef = ArgumentMatchers.method(Object.class, "method");
        verify(mock).method(Arguments.any());
        verify(mock).method(methodRef.getMock());
        verify(mock).method(methodRef.getMock(), ArgumentMatchers.anyInt());
        verify(mock).method(methodRef.getMock(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        verify(mock, never()).method(ArgumentMatchers.isA(Double.class));
    }

    @Test(expected = AssertionError.class)
    public void testVerificationError() {
        mockitoCore.verify(null, ArgumentMatchers.eq(1));
    }

    @Test(expected = NotAMockException.class)
    public void testNotAMockError() {
        Object notAMock = new Object();
        mockitoCore.verify(notAMock, ArgumentMatchers.eq(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockingProgressError() {
        Object[] mocks = new Object[0];
        mockitoCore.verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mockitoCore.mock(Object.class);
        Object mock2 = mockitoCore.mock(Object.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        inOrder.verify(mock1).method1();
        inOrder.verify(mock1).method2();
        inOrder.verify(mock2).method3();

        inOrder.verify(mock1).method1();
        inOrder.verify(mock1).method2();
        inOrder.verify(mock2).method3();

        inOrder.verify(mock1).method1();
        inOrder.verify(mock2).method3();

        inOrder.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testReset() {
        Object mock = mockitoCore.mock(Object.class);

        when(mock.method()).thenReturn("testValue");
        verify(mock).method();

        mockitoCore.reset();

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testStubbingVoid() {
        Object mock = mockitoCore.mock(Object.class);

        mockitoCore.stubVoid(mock).method();

        verify(mockitoCore.getMockHandler(mock)).voidMethodStubbable(eq(mock)).method();
    }

    @Test(expected = AssertionError.class)
    public void testMockitoUsageError() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testDeprecatedStubbing() {
        Object mock = mockitoCore.mock(Object.class);
        DeprecatedOngoingStubbing<Object> deprecatedStubbing = mockitoCore.stub(mock);

        deprecatedStubbing.thenAnswer(invocation -> "testValue");
        String result = mockitoCore.verify(mock, eq(VerificationMode.RETURN_VALUES)).method();
        assertEquals("testValue", result);
    }
}