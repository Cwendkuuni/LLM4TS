package com.example.junit;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = JavaType.constructType(HashMap.class);
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);

        assertEquals("java.util.HashMap", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(JavaType.constructType(Long.class));

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(Long.class, newMapLikeType.getKeyType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withContentType(JavaType.constructType(Float.class));

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(Float.class, newMapLikeType.getContentType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(new Object());

        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(new Object());

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(new Object(), newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(new Object());

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(new Object(), newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(new Object());

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(new Object(), newMapLikeType.getKeyValueHandler());
    }

    @Test
    public void testIsContainerType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null,
                keyType, valueType);

        assertTrue(mapLikeType.isTrueMapType());
    }
}