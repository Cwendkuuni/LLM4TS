package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        assertNotNull(mapTypeInstance);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testDeprecatedMapTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, null, null, null, keyType, valueType);
        assertNotNull(mapTypeInstance);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        MapType mapTypeInstanceWithTypeHandler = mapTypeInstance.withTypeHandler(null);
        assertNotNull(mapTypeInstanceWithTypeHandler);
        assertNull(mapTypeInstanceWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        JavaType contentTypeHandler = JavaType.constructRawUnsafe(String[].class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, contentTypeHandler, false);
        MapType mapTypeInstanceWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(null);
        assertNotNull(mapTypeInstanceWithContentTypeHandler);
        assertEquals(contentTypeHandler, mapTypeInstanceWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        MapType mapTypeInstanceWithValueHandler = mapTypeInstance.withValueHandler(null);
        assertNotNull(mapTypeInstanceWithValueHandler);
        assertNull(mapTypeInstanceWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        JavaType contentValueHandler = JavaType.constructRawUnsafe(Object[].class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);
        assertNotNull(mapTypeInstanceWithContentValueHandler);
        assertEquals(contentValueHandler, mapTypeInstanceWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithStaticTyping = mapTypeInstance.withStaticTyping();
        assertNotNull(mapTypeInstanceWithStaticTyping);
        assertTrue(mapTypeInstanceWithStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        JavaType contentType = JavaType.constructRawUnsafe(String[].class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithContentType = mapTypeInstance.withContentType(contentType);
        assertNotNull(mapTypeInstanceWithContentType);
        assertEquals(contentType, mapTypeInstanceWithContentType.getContentType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        JavaType newKeyType = JavaType.constructRawUnsafe(Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithNewKeyType = mapTypeInstance.withKeyType(newKeyType);
        assertNotNull(mapTypeInstanceWithNewKeyType);
        assertEquals(newKeyType, mapTypeInstanceWithNewKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        JavaType refinedMapType = JavaType.constructRawUnsafe(HashMap.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType refinedMapTypeInstance = mapTypeInstance.refine(refinedMapType.getRawClass(), bindings, null, null);
        assertNotNull(refinedMapTypeInstance);
        assertEquals(refinedMapType, refinedMapTypeInstance.getRawClass());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        Object keyTypeHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithKeyTypeHandler = mapTypeInstance.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(mapTypeInstanceWithKeyTypeHandler);
        assertNotNull(mapTypeInstanceWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructRawUnsafe(String.class);
        JavaType valueType = JavaType.constructRawUnsafe(Integer.class);
        Object keyValueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithKeyValueHandler = mapTypeInstance.withKeyValueHandler(keyValueHandler);
        assertNotNull(mapTypeInstanceWithKeyValueHandler);
        assertNotNull(mapTypeInstanceWithKeyValueHandler.getKeyValueHandler());
    }
}