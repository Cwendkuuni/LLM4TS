import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Given
        Field testField = getClass().getDeclaredField("testField");
        Class expectedType = String.class;

        // When
        Class actualType = new GenericMaster().getGenericType(testField);

        // Then
        assertEquals(expectedType, actualType);
    }

    // Add edge cases and exception handling tests here
}