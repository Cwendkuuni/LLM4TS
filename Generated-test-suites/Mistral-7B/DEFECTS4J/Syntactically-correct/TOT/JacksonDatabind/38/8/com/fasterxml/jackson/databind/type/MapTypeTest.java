import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        assertEquals(mapType, mapTypeInstance.getType());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testDeprecatedConstructors() {
        try {
            Class<Map<String, Integer>> mapType = Map.class;
            JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
            JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
            MapType mapTypeInstance = MapType.construct(mapType, keyType, valueType); // deprecated
            fail("Should not reach this point.");
        } catch (Exception e) {
            // Expected
        }

        try {
            Class<Map<String, Integer>> mapType = Map.class;
            MapType mapTypeInstance = MapType.construct(mapType); // deprecated
            fail("Should not reach this point.");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refinedMapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType).refine(Map.class, bindings, null, null);
        assertEquals(mapType, refinedMapType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object(){}; // Dummy value handler
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withValueHandler = mapTypeInstance.withTypeHandler(valueHandler);
        assertEquals(mapTypeInstance.getValueType(), withValueHandler.getValueType());
        assertNotEquals(valueHandler, withValueHandler.getTypeHandler());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentTypeHandler = TypeFactory.defaultInstance().constructType(String[].class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        assertEquals(valueType, withContentTypeHandler.getValueType());
        assertNotEquals(contentTypeHandler, withContentTypeHandler.getTypeHandler());
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object(){}; // Dummy value handler
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withValueHandler = mapTypeInstance.withValueHandler(valueHandler);
        assertEquals(valueType, withValueHandler.getValueType());
        assertNotEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentValueHandler = TypeFactory.defaultInstance().constructType(String[].class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);
        assertEquals(valueType, withContentValueHandler.getValueType());
        assertNotEquals(contentValueHandler, withContentValueHandler.getValueHandler());
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withStaticTyping = mapTypeInstance.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withNewKeyType = mapTypeInstance.withKeyType(newKeyType);
        assertEquals(newKeyType, withNewKeyType.getKeyType());
        assertNotEquals(keyType, withNewKeyType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Double.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType);
        MapType withNewContentType = mapTypeInstance.withContentType(newContentType);
        assertEquals(newContentType, withNewContentType.getValueType());
        assertNotEquals(valueType, withNewContentType.getValueType());
    }
}