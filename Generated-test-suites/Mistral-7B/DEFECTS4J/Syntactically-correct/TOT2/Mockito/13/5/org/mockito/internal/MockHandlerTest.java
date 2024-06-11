import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {
    // ...

    @Before
    public void setUp() {
        mockHandler = new MockHandler(new MockSettingsImpl());
    }

    @Test
    public void testHandle_BasicFunctionality() {
        T mock = Mockito.mock(T.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer answer = Mockito.answer(invocation);

        when(mockHandler.handle(invocation)).thenAnswer(answer);

        Object result = mockHandler.handle(invocation);

        verify(answer).answer(invocation);
        verifyNoMoreInteractions(mockHandler);
        assertEquals(result, null);
    }
}