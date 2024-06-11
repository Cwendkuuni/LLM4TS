import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapTypeTest {

    @Test
    public void testConstructBasic() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, new JavaType[0], keyType, valueType);

        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithTypeHandlers() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyHandler = new Object() {};
        Object valueHandler = new Object() {};

        MapType mapType = MapType.construct(rawType, bindings, null, new JavaType[0], keyType, valueType, keyHandler, valueHandler);

        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(keyHandler, mapType.getKeyTypeHandler());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refinedType = MapType.construct(Map.class, bindings, null, new JavaType[0], keyType, valueType).refine(HashMap.class, bindings, null, new JavaType[0]);

        assertNotNull(refinedType);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidSuperType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, Object.class, new JavaType[0], keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullBindings() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, null, null, new JavaType[0], keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, null, new JavaType[0], null, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullValueType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapType = MapType.construct(rawType, bindings, null, new JavaType[0], keyType, null);
    }
}