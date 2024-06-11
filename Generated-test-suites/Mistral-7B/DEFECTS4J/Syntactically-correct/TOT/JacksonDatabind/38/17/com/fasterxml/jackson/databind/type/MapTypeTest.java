import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, null, null, null, keyType, valueType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithBindingsAndSupertypes() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType superClass = JavaType.constructType(LinkedHashMap.class);
        JavaType[] superInterfaces = new JavaType[]{Collection.class, Map.class};
        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertFalse(mapType.isStatic());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInterfaces, mapType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithTypeBase() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        TypeBase typeBase = new TypeBase() {
            @Override
            public JavaType getContentType() {
                return JavaType.constructType(LinkedHashMap.class);
            }

            @Override
            public JavaType getRawClass() {
                return mapClass;
            }

            @Override
            public boolean isMapLikeType() {
                return true;
            }

            @Override
            public JavaType getKeyType() {
                return keyType;
            }

            @Override
            public JavaType getValueType() {
                return valueType;
            }
        };
        MapType mapType = MapType.construct(typeBase, keyType, valueType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertFalse(mapType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidRawType() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Class<Object> rawType = Object.class;
        MapType.construct(rawType, null, null, null, keyType, valueType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType newMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertFalse(mapType.isStatic());
        assertTrue(newMapType.getTypeHandler() instanceof Object);
        assertEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType contentTypeHandler = JavaType.constructType(LinkedHashMap.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertTrue(newValueType.getTypeHandler() instanceof Object);
        assertEquals(contentTypeHandler, newValueType.getTypeHandler());
        assertFalse(mapType.isStatic());
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType newMapType = mapType.withValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertFalse(mapType.isStatic());
        assertTrue(newMapType.isStatic());
        assertTrue(newMapType.getValueHandler() instanceof Object);
        assertEquals(handler, newMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType contentValueHandler = JavaType.constructType(LinkedHashMap.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertTrue(newValueType.getValueHandler() instanceof Object);
        assertEquals(contentValueHandler, newValueType.getValueHandler());
        assertFalse(mapType.isStatic());
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertTrue(newMapType.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType contentType = JavaType.constructType(LinkedHashMap.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertEquals(contentType, newValueType);
        assertFalse(mapType.isStatic());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType1 = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType1, valueType);
        JavaType keyType2 = JavaType.constructType(StringBuffer.class);
        MapType newMapType = mapType.withKeyType(keyType2);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(keyType2, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertFalse(mapType.isStatic());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        JavaType refinedMapType = mapType.refine(Map.class, null, null, null);
        assertEquals(mapClass, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
        assertFalse(mapType.isStatic());
        assertFalse(refinedMapType.isStatic());
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(mapClass, null, null, null, keyType, valueType);
        String result = mapType.toString();
        String expected = "[map type; class "+mapClass.getName()+", "+keyType+" -> "+valueType+"]";
        assertEquals(expected, result);
    }
}