import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MapTypeTest {
    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType superClass = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] superInterfaces = new JavaType[0];
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }
}