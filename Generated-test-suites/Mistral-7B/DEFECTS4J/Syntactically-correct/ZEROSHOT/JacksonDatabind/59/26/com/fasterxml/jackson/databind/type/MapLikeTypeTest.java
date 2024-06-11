package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testNewInstance() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertEquals("Object", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(Map.class, Integer.class,
                String.class);
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);

        assertEquals(baseType, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                null, null, null, null, false);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(keyType);

        assertEquals(mapLikeType.getRawClass(), newMapLikeType.getRawClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertFalse(newMapLikeType.isTrueMapType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        JavaType newValueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(mapLikeType.getRawClass(), newMapLikeType.getRawClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertFalse(newMapLikeType.isTrueMapType());
    }

    @Test
    public void testIsContainerType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        StringBuilder expected = new StringBuilder("[map-like type; class Object, ");
        expected.append(keyType.getRawClass().getName()).append(" -> ");
        expected.append(valueType.getRawClass().getName());
        expected.append("]");

        StringBuilder actual = mapLikeType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Object.class, null, null, null,
                keyType, valueType, null, null, false);

        StringBuilder expected = new StringBuilder("[map-like type; class Object<K, V>, ");
        expected.append("K extends java.lang.Object, V extends java.lang.Object>");
        expected.append("{K, V}");
        expected.append(" -> {K, V}]");

        StringBuilder actual = mapLikeType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = JavaType.construct(int[].class, int.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);

        assertTrue(mapLikeType.isTrueMapType());
    }
}