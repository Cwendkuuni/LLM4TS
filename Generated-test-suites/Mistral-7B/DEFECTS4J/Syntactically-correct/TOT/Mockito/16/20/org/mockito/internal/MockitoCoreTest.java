import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Mock
    private MockingProgress mockingProgress;

    @Mock
    private Reporter reporter;

    @Mock
    private MockUtil mockUtil;

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore() {
            @Override
            Reporter getReporter() {
                return reporter;
            }

            @Override
            MockUtil getMockUtil() {
                return mockUtil;
            }

            @Override
            MockingProgress getMockingProgress() {
                return mockingProgress;
            }
        };
    }

    @Test
    public void testMockCreation() {
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettingsImpl mockSettingsWithName = new MockSettingsImpl().withName("MyMock");

        String mock1 = mockitoCore.mock(stringClass, mockSettings);
        String mock2 = mockitoCore.mock(stringClass, mockSettingsWithName);

        verify(mockUtil).createMock(eq(stringClass), eq(mockSettings));
        verify(mockUtil).createMock(eq(stringClass), eq(mockSettingsWithName));
    }

    @Test
    public void testStubbing() {
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> stubbing = mockitoCore.when(mock);

        stubbing.thenReturn(1);
        stubbing.thenThrow(new RuntimeException("Exception thrown"));

        Object result1 = mockitoCore.verify(mock).someMethod();
        assertEquals(1, result1);

        try {
            mockitoCore.verify(mock).someMethod();
            fail();
        } catch (RuntimeException e) {
            //expected
        }
    }

    @Test(expected = NotAMockException.class)
    public void testVerificationOfNonMock() {
        Object nonMock = new Object();
        mockitoCore.verify(nonMock).someMethod();
    }

    @Test
    public void testReset() {
        Object mock = Mockito.mock(Object.class);
        mockitoCore.when(mock).someMethod().thenReturn(1);

        mockitoCore.reset(mock);

        mockitoCore.verify(mock).someMethod();

        verify(mockUtil, Mockito.never()).resetMock(eq(mock));
    }

    @Test
    public void testVerificationNoMoreInteractions() {
        Object mock = Mockito.mock(Object.class);
        mockitoCore.when(mock).someMethod().thenReturn(1);
        mockitoCore.verify(mock).someMethod();

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mockUtil, Mockito.never()).getMockHandler(eq(mock)).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        mockitoCore.when(mock1).someMethod().thenReturn(1);
        mockitoCore.when(mock2).someMethod().thenReturn(2);

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        verify(mockingProgress).validateState();
    }

    @Test
    public void testStubbingVoid() {
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);

        OngoingStubbing<Object> stubbing = mockitoCore.stubVoid(mock);

        stubbing.thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(Invocation invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(1, args.length);
                assertEquals("Argument", args[0]);
                return null;
            }
        });

        mockHandler.doVoidAnswer(new DoVoidAnswer<Object>() {
            @Override
            public void answer(Invocation invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(1, args.length);
                assertEquals("Argument", args[0]);
            }
        });

        mockitoCore.verify(mock).someVoidMethod("Argument");
    }
}