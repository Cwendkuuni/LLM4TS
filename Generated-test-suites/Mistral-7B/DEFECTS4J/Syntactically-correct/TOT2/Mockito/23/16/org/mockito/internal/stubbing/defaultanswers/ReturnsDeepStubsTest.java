import org.junit.Test;
import static org.mockito.Mockito.*; // for verification

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod_InvocationMatchesStubbedInvocation() throws Throwable {
        // Given
        Object mock = mock(Object.class);
        when(mock.someMethod()).thenReturn("expectedValue");

        // When
        Object actual = ReturnsDeepStubs.getInstance().answer(invocation(mock, "someMethod"));

        // Then
        verify(mock).someMethod();
        assertEquals("expectedValue", actual);
    }

    @Test
    public void testAnswerMethod_InvocationDoesNotMatchAnyStubbedInvocation() throws Throwable {
        // Given
        Object mock = mock(Object.class);

        // When
        Object actual = ReturnsDeepStubs.getInstance().answer(invocation(mock, "nonExistentMethod"));

        // Then
        assertNull(actual);
    }
}