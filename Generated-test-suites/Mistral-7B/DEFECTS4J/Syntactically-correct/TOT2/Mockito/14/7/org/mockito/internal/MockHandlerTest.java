import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private Object mock;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        mock = mock(Object.class);
    }

    @Test
    public void testHandleMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockHandler.handle(invocation)).thenReturn(42);
        int result = mockHandler.handle(invocation);
        assertEquals(42, result);
        verify(mockHandler).handle(invocation);
    }

    @Test
    public void testHandleVoidMethod() {
        MockHandler voidMockHandler = new MockHandler();
        doNothing().when(mock).someMethod();
        voidMockHandler.voidMethodStubbable(mock);
        voidMockHandler.handle(argThat(invocation -> invocation.getMethod().getName().equals("someMethod")));
        verify(mock).someMethod();
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleMultipleInvocations() {
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(mockHandler.handle(invocation1)).thenReturn(1);
        when(mockHandler.handle(invocation2)).thenReturn(2);
        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);
        fail("Expected an exception");
    }

    @Test
    public void testHandleDifferentInvocationTypes() {
        Object object = new Object();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("newInstance", new Class[0]));
        when(mockHandler.handle(invocation)).thenAnswer((Answer<Object>) invocation1 -> Object.newInstance());
        Object result = mockHandler.handle(invocation);
        assertEquals(result.getClass(), Object.class);
    }

    @Test(expected = RuntimeException.class)
    public void testHandleExceptions() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockHandler.handle(invocation)).thenThrow(new RuntimeException("Test"));
        mockHandler.handle(invocation);
        fail("Expected exception");
    }
}