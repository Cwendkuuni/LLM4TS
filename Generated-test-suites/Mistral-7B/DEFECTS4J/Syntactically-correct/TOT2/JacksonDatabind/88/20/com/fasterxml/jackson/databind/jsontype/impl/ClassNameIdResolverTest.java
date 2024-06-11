import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClassNameIdResolverTest {
    @Test
    public void testIdFromValue() {
        // Given
        final ClassNameIdResolver classResolver = new ClassNameIdResolver(null, null);
        final Object value = new Object();

        // When
        final String result = classResolver.idFromValue(value);

        // Then
        assertEquals("Unexpected class name id", value.getClass().getName(), result);
    }
}