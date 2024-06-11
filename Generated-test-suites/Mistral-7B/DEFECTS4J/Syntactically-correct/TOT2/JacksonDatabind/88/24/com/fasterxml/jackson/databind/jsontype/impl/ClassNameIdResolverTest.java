import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver classResolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> valueClass = value.getClass();
        String id = classResolver.idFromValue(value);
        String expectedId = valueClass.getName();
        assertEquals(expectedId, id);
    }
}