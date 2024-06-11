import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.DEFAULT;
        JavaType superClass = JavaType.valueOf(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT);

        assertEquals("Expected map type instance", mapType, mapTypeInstance.getRawClass());
        assertEquals("Expected key type", keyT, mapTypeInstance.getKeyType());
        assertEquals("Expected value type", valueT, mapTypeInstance.getValueType());
    }
}