import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Test
    public void testHandleInvocation() {
        MockHandler<SomeMockClass> mockHandler = mock(MockHandler.class);
        // Arrange
        Invocation invocation = new InvocationOnMock(mock(SomeMockClass.class), new Object[0]);
        Answer answer = argThat(a -> a instanceof String);
        when(mockHandler).thenAnswer(answer);

        // Act
        String result = (String) mockHandler.handle(invocation);

        // Assert
        verify(mockHandler).handle(invocation);
        assertEquals("Expected String", result);
    }
}