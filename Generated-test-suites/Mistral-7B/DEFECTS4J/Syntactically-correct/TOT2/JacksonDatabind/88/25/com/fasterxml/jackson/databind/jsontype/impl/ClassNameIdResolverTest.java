import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;

public class ClassNameIdResolverTest {

    private ObjectMapper mapper;
    private TypeFactory typeFactory;

    public ClassNameIdResolverTest() throws IOException {
        this.mapper = new ObjectMapper();
        this.typeFactory = mapper.getTypeFactory();
    }

    @Test
    public void testIdFromValue() throws IOException {
        // Test with a simple Java class
        Class<String> StringClass = String.class;
        Object value = "Hello World";
        String expectedId = StringClass.getName();
        String actualId = new ClassNameIdResolver(null, typeFactory).idFromValue(value);
        assertEquals(expectedId, actualId);

        // Test with an Enum
        Enum e = Enum.valueOf(Enum.class, "TEST");
        Class<?> enumClass = e.getClass();
        expectedId = enumClass.getName();
        actualId = new ClassNameIdResolver(null, typeFactory).idFromValue(e);
        assertEquals(expectedId, actualId);

        // Test with a Collection
        List<String> list = Arrays.asList("Test1", "Test2");
        expectedId = "java.util.ArrayList";
        actualId = new ClassNameIdResolver(null, typeFactory).idFromValue(list);
        assertEquals(expectedId, actualId);

        // Test with a Map
        Map<String, String> map = new HashMap<>();
        map.put("Key1", "Value1");
        expectedId = "java.util.EnumMap<java.lang.String, java.lang.String>";
        actualId = new ClassNameIdResolver(null, typeFactory).idFromValue(map);
        assertEquals(expectedId, actualId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValueWithNullValue() {
        new ClassNameIdResolver(null, typeFactory).idFromValue(null);
    }

    // Add more test cases for other methods in ClassNameIdResolver
}