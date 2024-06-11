import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

    private MockHandler<DummyClass> mockHandler;
    private DummyClass dummyClass;
    private InvocationOnMock invocationMock;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        dummyClass = mock(DummyClass.class);
        invocationMock = Mockito.mock(InvocationOnMock.class);
    }

    @Test
    public void testHandleMethodWithValidInvocation() {
        when(mockHandler.handle(invocationMock)).thenReturn("MockHandler returned a value");
        String result = mockHandler.handle(invocationMock);
        verify(mockHandler).handle(invocationMock);
        assertEquals("MockHandler returned a value", result);
    }
}