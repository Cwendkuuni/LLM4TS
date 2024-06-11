package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType)
                .withValueHandler(valueHandler);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithBogusSuperclass() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, null, _bogusSuperClass(mapClass), null, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType subclass = Map.class.asSubclass(HashMap.class);
        MapType parentMapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        JavaType childMapType = parentMapType.narrow(subclass);
        assertEquals(MapType.class, childMapType.getClass());
        assertEquals(subclass, childMapType.getRawClass());
        assertEquals(keyType, childMapType.getKeyType());
        assertEquals(valueType, childMapType.getValueType());
        assertEquals(parentMapType.getValueHandler(), childMapType.getValueHandler());
        assertEquals(parentMapType.isStatic(), childMapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object valueHandler = new Object() {};
        JavaType newMapType = mapType.withTypeHandler(valueHandler);
        assertEquals(MapType.class, newMapType.getClass());
        assertEquals(mapClass, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertEquals(false, newMapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withStaticTyping();
        assertEquals(MapType.class, newMapType.getClass());
        assertEquals(mapClass, newMapType.getRawClass());
        assertEquals(keyType.withStaticTyping(), newMapType.getKeyType());
        assertEquals(valueType.withStaticTyping(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(true, newMapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType contentTypeHandler = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(MapType.class, newMapType.getClass());
        assertEquals(mapClass, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertEquals(contentTypeHandler, newValueType.getValueHandler());
        assertEquals(false, newMapType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyTypeHandler = new Object() {};
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(MapType.class, newMapType.getClass());
        assertEquals(mapClass, newMapType.getRawClass());
        JavaType newKeyType = newMapType.getKeyType();
        assertEquals(keyType, newKeyType);
        assertEquals(keyTypeHandler, newKeyType.getValueHandler());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(false, newMapType.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyValueHandler = new Object() {};
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(MapType.class, newMapType.getClass());
        assertEquals(mapClass, newMapType.getRawClass());
        JavaType newKeyType = newMapType.getKeyType();
        assertEquals(keyType, newKeyType);
        assertEquals(keyValueHandler, newKeyType.getValueHandler());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(false, newMapType.isStatic());
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        String toString = mapType.toString();
        assertTrue(toString.startsWith("[map type; class "));
        assertTrue(toString.endsWith("]"));
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        // This is just a placeholder, as long as it's not MapType or its superclass
        return String.class;
    }

}