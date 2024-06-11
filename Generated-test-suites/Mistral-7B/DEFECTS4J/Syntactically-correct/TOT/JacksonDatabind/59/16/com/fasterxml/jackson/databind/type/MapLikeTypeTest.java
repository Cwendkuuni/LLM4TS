import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Integer.class);

        MapLikeType mapLikeType = mapType;
        assertEquals("Expected MapLikeType instance", mapLikeType.getClass(), MapLikeType.class);
        assertEquals("Key type should be String", String.class, mapLikeType.getKeyType());
        assertEquals("Value type should be Integer", Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeKeyTypeChange() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Integer.class);

        MapLikeType mapLikeType = mapType;
        JavaType newKeyType = JavaType.constructParametricType(HashMap.class, Integer.class, String.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals("Expected MapLikeType instance after keyType change", newMapLikeType.getClass(), MapLikeType.class);
        assertEquals("Key type should be Integer now", Integer.class, newMapLikeType.getKeyType());
        assertEquals("Value type should stay the same", Integer.class, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeValueTypeChange() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Integer.class);

        MapLikeType mapLikeType = mapType;
        JavaType newValueType = JavaType.constructParametricType(ArrayList.class, Double.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals("Expected MapLikeType instance after valueType change", newMapLikeType.getClass(), MapLikeType.class);
        assertEquals("Key type should stay the same", String.class, newMapLikeType.getKeyType());
        assertEquals("Value type should be Double now", Double.class, newMapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidBaseType() {
        JavaType baseType = JavaType.constructParametricType(List.class, Integer.class);
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        fail("Should have thrown an IllegalArgumentException");
    }

    @Test
    public void testMapLikeTypeWithHandlers() {
        Class<Map<String, Double>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Double.class);

        MapLikeType mapLikeType = mapType;
        Object valueHandler = new Object() {};
        Object valueTypeHandler = new Object() {};
        MapLikeType handlerMapLikeType = mapLikeType.withValueHandler(valueHandler).withTypeHandler(valueTypeHandler);

        assertNotSame("ValueHandler should not be the same instance", valueHandler, mapLikeType.getValueHandler());
        assertNotSame("TypeHandler should not be the same instance", valueTypeHandler, mapLikeType.getTypeHandler());
        assertEquals("ValueHandler should be the same instance after creation", valueHandler, handlerMapLikeType.getValueHandler());
        assertEquals("TypeHandler should be the same instance after creation", valueTypeHandler, handlerMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Integer.class);

        MapLikeType mapLikeType = mapType;
        assertTrue("Expected true map type", mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Integer.class);

        MapLikeType mapLikeType = mapType;
        assertTrue("Expected container type", mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.upgradeFrom(mapClass, String.class, Integer.class);

        MapLikeType mapLikeType = mapType;
        assertTrue("Expected map-like type", mapLikeType.isMapLikeType());
    }
}