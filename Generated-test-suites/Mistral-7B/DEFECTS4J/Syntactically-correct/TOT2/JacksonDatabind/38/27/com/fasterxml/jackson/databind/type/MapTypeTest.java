import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType expectedMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(expectedMapType, actualMapType);
    }
}