import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.invoke;

public class ReturnsEmptyValuesTest {

    @Test
    void testAnswer_toString() {
        Object mock = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String result = (String) returnsEmptyValues.answer(new InvocationOnMock(mock, null, "toString", new Object[0]));
        verify(mock).toString();
        assertNotNull(result);
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
    }
}