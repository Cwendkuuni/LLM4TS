package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstructWithAllArguments() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        assertNotNull(mapTypeInstance);
        assertEquals(Map.class, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testConstructWithKeyTypeAndValueType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, null, null, null, keyType, valueType);

        assertNotNull(mapTypeInstance);
        assertEquals(Map.class, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        JavaType refinedMapType = mapTypeInstance.refine(HashMap.class, bindings, null, new JavaType[0]);

        assertNotNull(refinedMapType);
        assertEquals(HashMap.class, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        Object handler = new Object();

        MapType handlerAddedMapTypeInstance = mapTypeInstance.withTypeHandler(handler);

        assertEquals(mapTypeInstance.getRawClass(), handlerAddedMapTypeInstance.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), handlerAddedMapTypeInstance.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), handlerAddedMapTypeInstance.getValueType());
        assertEquals(handler, handlerAddedMapTypeInstance.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(new TypeReference<Map<String, List<Integer>>>() {}.getType());

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        Object handler = new Object();

        MapType handlerAddedMapTypeInstance = mapTypeInstance.withContentTypeHandler(handler);

        assertEquals(mapTypeInstance.getRawClass(), handlerAddedMapTypeInstance.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), handlerAddedMapTypeInstance.getKeyType());
        JavaType valueTypeWithHandler = handlerAddedMapTypeInstance.getValueType();
        assertEquals(valueType, valueTypeWithHandler.getType());
        assertEquals(handler, valueTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        Object handler = new Object();

        MapType handlerAddedMapTypeInstance = mapTypeInstance.withValueHandler(handler);

        assertEquals(mapTypeInstance.getRawClass(), handlerAddedMapTypeInstance.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), handlerAddedMapTypeInstance.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), handlerAddedMapTypeInstance.getValueType());
        assertEquals(handler, handlerAddedMapTypeInstance.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        Object handler = new Object();

        MapType handlerAddedMapTypeInstance = mapTypeInstance.withContentValueHandler(handler);

        assertEquals(mapTypeInstance.getRawClass(), handlerAddedMapTypeInstance.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), handlerAddedMapTypeInstance.getKeyType());
        JavaType valueTypeWithHandler = handlerAddedMapTypeInstance.getValueType();
        assertEquals(valueType, valueTypeWithHandler.getType());
        assertEquals(handler, valueTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        MapType staticMapTypeInstance = mapTypeInstance.withStaticTyping();

        assertTrue(staticMapTypeInstance._asStatic);
        assertEquals(mapTypeInstance.getRawClass(), staticMapTypeInstance.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), staticMapTypeInstance.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), staticMapTypeInstance.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        Object handler = new Object();

        MapType handlerAddedMapTypeInstance = mapTypeInstance.withKeyTypeHandler(handler);

        assertEquals(mapTypeInstance.getRawClass(), handlerAddedMapTypeInstance.getRawClass());
        JavaType keyTypeWithHandler = handlerAddedMapTypeInstance.getKeyType();
        assertEquals(keyType, keyTypeWithHandler.getType());
        assertEquals(handler, keyTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        Object handler = new Object();

        MapType handlerAddedMapTypeInstance = mapTypeInstance.withKeyValueHandler(handler);

        assertEquals(mapTypeInstance.getRawClass(), handlerAddedMapTypeInstance.getRawClass());
        JavaType keyTypeWithHandler = handlerAddedMapTypeInstance.getKeyType();
        assertEquals(keyType, keyTypeWithHandler.getType());
        assertEquals(handler, keyTypeWithHandler.getValueHandler());
    }
}