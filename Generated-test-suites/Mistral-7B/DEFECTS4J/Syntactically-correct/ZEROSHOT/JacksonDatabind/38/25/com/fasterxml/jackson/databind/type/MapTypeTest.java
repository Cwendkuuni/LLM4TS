package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        JavaType subclassType = new TypeReference<HashMap<String, Integer>>() {}.getType();
        JavaType narrowedType = mapType.narrow(subclassType.getRawClass());
        assertEquals(MapType.class, narrowedType.getClass());
        assertEquals(subclassType.getRawClass(), narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Object handler = new Object() {};
        JavaType handledMapType = mapType.withTypeHandler(handler);
        assertEquals(MapType.class, handledMapType.getClass());
        assertEquals(mapClass, handledMapType.getRawClass());
        assertEquals(keyType, handledMapType.getKeyType());
        assertEquals(valueType, handledMapType.getValueType());
        assertEquals(handler, handledMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Object handler = new Object() {};
        JavaType handledMapType = mapType.withContentTypeHandler(handler);
        assertEquals(MapType.class, handledMapType.getClass());
        assertEquals(mapClass, handledMapType.getRawClass());
        assertEquals(keyType, handledMapType.getKeyType());
        JavaType handledValueType = handledMapType.getValueType();
        assertEquals(valueType, handledValueType);
        assertEquals(handler, handledMapType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Object handler = new Object() {};
        JavaType handledMapType = mapType.withValueHandler(handler);
        assertEquals(MapType.class, handledMapType.getClass());
        assertEquals(mapClass, handledMapType.getRawClass());
        assertEquals(keyType, handledMapType.getKeyType());
        JavaType handledKeyType = handledMapType.getKeyType();
        assertEquals(keyType, handledKeyType);
        assertEquals(handler, handledMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Object handler = new Object() {};
        JavaType handledMapType = mapType.withContentValueHandler(handler);
        assertEquals(MapType.class, handledMapType.getClass());
        assertEquals(mapClass, handledMapType.getRawClass());
        JavaType handledKeyType = handledMapType.getKeyType();
        assertEquals(keyType, handledKeyType);
        JavaType handledValueType = handledMapType.getValueType();
        assertEquals(valueType, handledValueType);
        assertEquals(handler, handledMapType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        JavaType staticMapType = mapType.withStaticTyping();
        assertTrue(staticMapType.isStatic());
        assertFalse(mapType.isStatic());
        assertSame(mapType.getClass(), staticMapType.getClass());
        assertSame(mapClass, staticMapType.getRawClass());
        assertSame(keyType, staticMapType.getKeyType());
        assertSame(valueType, staticMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        JavaType newValueType = new TypeReference<String>() {}.getType();
        JavaType newMapType = mapType.withContentType(newValueType);
        assertEquals(MapType.class, newMapType.getClass());
        assertSame(mapClass, newMapType.getRawClass());
        assertSame(keyType, newMapType.getKeyType());
        assertNotSame(valueType, newMapType.getValueType());
        assertSame(newValueType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        JavaType newKeyType = new TypeReference<StringBuilder>() {}.getType();
        JavaType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(MapType.class, newMapType.getClass());
        assertSame(mapClass, newMapType.getRawClass());
        assertNotSame(keyType, newMapType.getKeyType());
        assertSame(newKeyType, newMapType.getKeyType());
        assertSame(valueType, newMapType.getValueType());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Class<HashMap<String, Integer>> hashMapClass = HashMap.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refinedType = mapType.refine(hashMapClass, bindings, null, null);
        assertEquals(MapType.class, refinedType.getClass());
        assertSame(mapClass, refinedType.getRawClass());
        assertSame(keyType, refinedType.getKeyType());
        assertSame(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Object handler = new Object() {};
        JavaType handledMapType = mapType.withKeyTypeHandler(handler);
        assertEquals(MapType.class, handledMapType.getClass());
        assertSame(mapClass, handledMapType.getRawClass());
        assertEquals(keyType, handledMapType.getKeyType());
        assertEquals(valueType, handledMapType.getValueType());
        assertEquals(handler, handledMapType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, keyType, valueType);
        Object handler = new Object() {};
        JavaType handledMapType = mapType.withKeyValueHandler(handler);
        assertEquals(MapType.class, handledMapType.getClass());
        assertSame(mapClass, handledMapType.getRawClass());
        JavaType handledKeyType = handledMapType.getKeyType();
        assertEquals(keyType, handledKeyType);
        JavaType handledValueType = handledMapType.getValueType();
        assertEquals(valueType, handledValueType);
        assertEquals(handler, handledMapType.getKeyValueHandler());
    }
}