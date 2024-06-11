package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
        assertSame(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new Object() {};

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithHandler = mapTypeInstance.withTypeHandler(valueHandler);

        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
        assertSame(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeWithHandler.getKeyType());
        assertEquals(valueType, mapTypeWithHandler.getValueType());
        assertNotNull(mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        Class<Map<String, Long>> refinedMapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType refinedKeyType = String.class;
        JavaType refinedValueType = Long.class;

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType refinedMapTypeInstance = mapTypeInstance.refine(refinedMapType, bindings, superClass, superInts);

        assertEquals("map type; class Map<java.lang.String, java.lang.Long>", refinedMapTypeInstance.toString());
        assertSame(refinedMapType, refinedMapTypeInstance.getRawClass());
        assertEquals(refinedKeyType, refinedMapTypeInstance.getKeyType());
        assertEquals(refinedValueType, refinedMapTypeInstance.getValueType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new Object() {};
        Object contentTypeHandler = new Object() {};

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);

        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
        assertSame(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeWithContentTypeHandler.getKeyType());
        assertEquals(valueType, mapTypeWithContentTypeHandler.getValueType());
        assertNotNull(mapTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructWithInvalidType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        JavaType invalidType = new TypeReference<List<String>>() {}.getType();

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType mapTypeWithInvalidType = mapTypeInstance.withContentType(invalidType);

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        ObjectMapper objectMapper = new ObjectMapper();

        String mapTypeString = mapTypeInstance.toString();
        String mapTypeStringDeserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(mapTypeString), MapType.class).toString();

        assertEquals(mapTypeString, mapTypeStringDeserialized);
    }
}