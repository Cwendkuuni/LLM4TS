
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<Object>(new MockSettingsImpl());
    }

    @Test
    public void testHandle_MethodCall_ReturnsNull() throws Throwable {
        // Given
        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        // When
        Object result = mockHandler.handle(invocation);

        // Then
        verify(invocationContainer).setMethodForStubbing(argumentMatcher);
        verify(invocationContainer).setInvocationForPotentialStubbing(argumentMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(argumentMatcher);
        assertNull(result);
    }

    @Test
    public void testHandle_NullInvocation_ThrowsNullPointerException() throws Throwable {
        // Given
        Invocation invocation = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_ExceptionInHandleMethod_PropagatesException() throws Throwable {
        // Given
        Invocation invocation = mock(Invocation.class);
        RuntimeException exception = mock(RuntimeException.class);

        when(mockHandler.handle(invocation)).thenThrow(exception);

        // When & Then
        assertThrows(RuntimeException.class, () -> mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_StubVoidMethod() throws Throwable {
        // Given
        Invocation invocation = mock(Invocation.class);

        mockHandler.voidMethodStubbable(mock(Object.class));

        // When & Then
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_MultipleVerifications_ThrowsMockitoException() throws Throwable {
        // Given
        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);

        mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(argumentMatcher);

        // When & Then (first verification)
        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(argumentMatcher);

        // When & Then (second verification)
        assertThrows(org.mockito.exceptions.misusing.MultipleVerificationsException.class, () -> verify(mockHandler.invocationContainerImpl).setMethodForStubbing(argumentMatcher));
    }

    @Test
    public void testHandle_VerificationWithMismatchedArgumentType_ThrowsArgumentMismatchException() throws Throwable {
        // Given
        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);
        ArgumentMatcher mismatchedArgumentMatcher = mock(ArgumentMatcher.class);

        mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(argumentMatcher);

        // When & Then
        assertThrows(org.mockito.exceptions.misusing.ArgumentMismatchException.class, () -> verify(mockHandler.invocationContainerImpl).setMethodForStubbing(mismatchedArgumentMatcher));
    }
}
