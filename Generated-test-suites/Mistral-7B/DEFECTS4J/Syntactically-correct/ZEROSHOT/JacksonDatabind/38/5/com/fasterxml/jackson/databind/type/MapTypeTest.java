package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandlers() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType, valueHandler, typeHandler);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = MapType.construct(rawType, null, null, null,
                new TypeReference<String>() {}.getType(),
                new TypeReference<Integer>() {}.getType());
        JavaType subclass = Map.class.asSubclass(Map.class);
        JavaType narrowedType = mapType.narrow(subclass);
        assertEquals(MapType.class, narrowedType.getClass());
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedType.getValueType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object typeHandler = new Object() {};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withTypeHandler = mapType.withTypeHandler(typeHandler);
        assertEquals(MapType.class, withTypeHandler.getClass());
        assertEquals(rawType, withTypeHandler.getRawClass());
        assertEquals(keyType, withTypeHandler.getKeyType());
        assertEquals(valueType, withTypeHandler.getValueType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertNull(withTypeHandler.getValueHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object contentTypeHandler = new Object() {};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(MapType.class, withContentTypeHandler.getClass());
        assertEquals(rawType, withContentTypeHandler.getRawClass());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        JavaType valueTypeWithHandler = withContentTypeHandler.getValueType();
        assertEquals(valueType, valueTypeWithHandler.getRawType());
        assertEquals(contentTypeHandler, valueTypeWithHandler.getTypeHandler());
        assertNull(withContentTypeHandler.getValueHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(MapType.class, withValueHandler.getClass());
        assertEquals(rawType, withValueHandler.getRawClass());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertNull(withValueHandler.getTypeHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object contentValueHandler = new Object() {};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(MapType.class, withContentValueHandler.getClass());
        assertEquals(rawType, withContentValueHandler.getRawClass());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        JavaType valueTypeWithHandler = withContentValueHandler.getValueType();
        assertEquals(valueType, valueTypeWithHandler.getRawType());
        assertEquals(contentValueHandler, valueTypeWithHandler.getValueHandler());
        assertNull(withContentValueHandler.getTypeHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withStaticTyping = mapType.withStaticTyping();
        assertEquals(MapType.class, withStaticTyping.getClass());
        assertEquals(rawType, withStaticTyping.getRawClass());
        assertEquals(keyType.withStaticTyping(), withStaticTyping.getKeyType());
        assertEquals(valueType.withStaticTyping(), withStaticTyping.getValueType());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType contentType = new TypeReference<String>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withContentType = mapType.withContentType(contentType);
        assertEquals(MapType.class, withContentType.getClass());
        assertEquals(rawType, withContentType.getRawClass());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
        assertNull(withContentType.getValueHandler());
        assertNull(withContentType.getTypeHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType newKeyType = new TypeReference<String>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withNewKeyType = mapType.withKeyType(newKeyType);
        assertEquals(MapType.class, withNewKeyType.getClass());
        assertEquals(rawType, withNewKeyType.getRawClass());
        assertEquals(newKeyType, withNewKeyType.getKeyType());
        assertEquals(valueType, withNewKeyType.getValueType());
        assertNull(withNewKeyType.getValueHandler());
        assertNull(withNewKeyType.getTypeHandler());
        assertFalse(withNewKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType refinedType = mapType.refine(Map.class, null, null, null);
        assertEquals(MapType.class, refinedType.getClass());
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyTypeHandler = new Object() {};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(MapType.class, withKeyTypeHandler.getClass());
        assertEquals(rawType, withKeyTypeHandler.getRawClass());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertNull(withKeyTypeHandler.getValueHandler());
        assertEquals(keyTypeHandler, withKeyTypeHandler.getTypeHandler());
        assertFalse(withKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyValueHandler = new Object() {};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(MapType.class, withKeyValueHandler.getClass());
        assertEquals(rawType, withKeyValueHandler.getRawClass());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertNull(withKeyValueHandler.getValueHandler());
        assertEquals(keyValueHandler, withKeyValueHandler.getTypeHandler());
        assertFalse(withKeyValueHandler.isStatic());
    }
}