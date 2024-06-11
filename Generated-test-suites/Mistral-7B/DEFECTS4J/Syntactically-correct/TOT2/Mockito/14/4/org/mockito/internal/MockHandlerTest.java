import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWrapper;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<MockitoTestClass> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandle_Stubbing() throws Throwable {
        when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(new Object());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = mockHandler.handle(invocation);
        verify(mockHandler).handle(invocation);
        assertEquals(new Object(), result);
    }

    @Test
    public void testHandle_Verification() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        mockHandler.handle(invocation);
        verify(mockHandler).handle(invocation);
    }

    @Test
    public void testHandle_VoidMethodStubbable() throws Throwable {
        mockHandler.voidMethodStubbable(mock(MockitoTestClass.class));
        verify(mockHandler).voidMethodStubbable(ArgumentMatchers.any(MockitoTestClass.class));
    }

    @Test
    public void testGetMockSettings() {
        assertEquals(MockSettingsImpl.class, mockHandler.getMockSettings().getClass());
    }
}