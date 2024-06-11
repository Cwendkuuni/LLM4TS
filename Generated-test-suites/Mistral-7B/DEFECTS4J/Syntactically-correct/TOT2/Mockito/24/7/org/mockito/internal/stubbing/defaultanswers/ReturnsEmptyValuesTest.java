import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtensions;
import static org.mockito.Mockito.*;

class ReturnsEmptyValuesTest {
    private static Object mock;
    private static InvocationOnMock invocation;

    @BeforeAll
    public static void setup() {
        mock = MockitoExtensions.mocked(Object.class);
        invocation = mockMethodCall(invocation(), any());
    }

    @Test
    public void testAnswer() {
        when(mock.answer(invocation)).thenAnswer(new ReturnsEmptyValues());
        Object result = mock.answer(invocation);

        // Test for toString() method
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);

        // Test for compareTo() method
        Object other = new Object();
        assertEquals(1, mock.answer(invocation).compareTo(other));
    }
}