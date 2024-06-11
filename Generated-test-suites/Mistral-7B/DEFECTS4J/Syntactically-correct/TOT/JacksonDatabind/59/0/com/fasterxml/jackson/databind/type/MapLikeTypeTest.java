package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.type.WritableTypeId;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), bindings, null, null, null, null, null, null, false);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeCreationFromTypeBase() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBase baseType = TypeFactory.defaultInstance().constructType(mapType);
        MapLikeType mapLikeType = new MapLikeType((TypeBase) baseType, keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeCreationFromJavaType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(mapType, keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeCreationFromNonTypeBase() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(Long.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, keyType, null);
        assertEquals("Map<Long, Integer>", mapLikeType.toString());
        assertEquals(Long.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Float.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, valueType);
        assertEquals("Map<String, Float>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Float.class, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        Map<String, Integer> map = new HashMap<>();
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, map, null, false);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
        assertEquals(map, mapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        Map<String, Integer> map = new HashMap<>();
        Map<String, String> handlerMap = new HashMap<>();
        handlerMap.put("1", "one");
        handlerMap.put("2", "two");
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, handlerMap, false);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
        assertEquals(handlerMap, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                WritableTypeId.get("com.TestMap"), Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, null, false);
        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
        assertEquals("Map<String, Integer>", staticMapLikeType.toString());
        assertTrue(staticMapLikeType.isStaticTyping());
        assertFalse(mapLikeType.isStaticTyping());
    }
}