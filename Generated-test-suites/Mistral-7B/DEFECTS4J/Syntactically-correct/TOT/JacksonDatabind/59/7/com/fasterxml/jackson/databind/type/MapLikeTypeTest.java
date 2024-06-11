package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    // Test case for constructor with Class, TypeBindings, JavaType[]
    @Test
    public void testMapLikeTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, superInts, keyT, valueT);
        assertEquals("Map<java.lang.String, java.lang.Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    // Test case for constructor with TypeBase, JavaType, JavaType
    @Test
    public void testUpgradeFromJavaType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);
        assertEquals("Map<java.lang.String, java.lang.Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    // Test case for factory method "withKeyType"
    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = TypeFactory.defaultInstance().constructType(mapType);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyT, valueT);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(TypeFactory.defaultInstance().constructType(Long.class));
        assertEquals("Map<java.lang.Long, java.lang.Integer>", newMapLikeType.toString());
        assertEquals(Long.class, newMapLikeType.getKeyType());
        assertEquals(Integer.class, newMapLikeType.getContentType());
    }

    // Test case for factory method "withContentType"
    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = TypeFactory.defaultInstance().constructType(mapType);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyT, valueT);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Float.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newContentType);
        assertEquals("Map<java.lang.String, java.lang.Float>", newMapLikeType.toString());
        assertEquals(String.class, newMapLikeType.getKeyType());
        assertEquals(Float.class, newMapLikeType.getContentType());
    }

    // Test case for factory method "withTypeHandler"
    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = TypeFactory.defaultInstance().constructType(mapType);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyT, valueT);
        Object valueHandler = new Object() { /* timeout handler implementation */ };
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(valueHandler);
        assertNotNull(newMapLikeType.getValueHandler());
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
    }

    // Test case for factory method "withContentTypeHandler"
    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = TypeFactory.defaultInstance().constructType(mapType);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyT, valueT);
        Object contentTypeHandler = new Object() { /* custom contentTypeHandler implementation */ };
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newMapLikeType.getTypeHandler());
        assertEquals(contentTypeHandler, newMapLikeType.getTypeHandler());
    }

    // Test case for factory method "withKeyTypeHandler"
    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = TypeFactory.defaultInstance().constructType(mapType);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyT, valueT);
        Object keyTypeHandler = new Object() { /* custom keyTypeHandler implementation */ };
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(newMapLikeType.getKeyHandler());
        assertEquals(keyTypeHandler, newMapLikeType.getKeyHandler());
    }

    // Test case for factory method "withKeyValueHandler"
    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = TypeFactory.defaultInstance().constructType(mapType);
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyT, valueT);
        Object keyValueHandler = new Object() { /* custom keyValueHandler implementation */ };
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(keyValueHandler);
        assertNotNull(newMapLikeType.getKeyValueHandler());
        assertEquals(keyValueHandler, newMapLikeType.getKeyValueHandler());
    }

    // Test case for method "isTrueMapType"
    @Test
    public void testIsTrueMapType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType mapLikeType = TypeFactory.defaultInstance().constructType(mapType);
        assertTrue(MapLikeType.class.isAssignableFrom(mapLikeType.getClass()));
        assertTrue(MapLikeType.class.isInstance(mapLikeType));
        assertTrue(MapLikeType.class.isInstance(new MapLikeType(mapType, null, null)));
        assertTrue(MapLikeType.class.isInstance(new MapLikeType(TypeFactory.defaultInstance().constructType(HashMap.class), null, null)));
        assertFalse(Map.class.isInstance(mapLikeType));
        assertFalse(List.class.isInstance(mapLikeType));
    }
}