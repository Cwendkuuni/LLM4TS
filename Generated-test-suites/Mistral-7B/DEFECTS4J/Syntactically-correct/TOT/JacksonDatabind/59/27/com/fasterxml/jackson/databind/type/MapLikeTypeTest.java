package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test construction from JavaType
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());

        // Test construction from TypeBase
        TypeBase baseType = new TypeBase() {
            @Override
            public JavaType getRawType() {
                return Map.class;
            }

            @Override
            public JavaType getGenericType() {
                return TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
            }
        };
        MapLikeType mapLikeTypeFromBase = new MapLikeType(baseType, String.class, Integer.class);
        assertEquals(MapLikeType.class, mapLikeTypeFromBase.getClass());
        assertEquals(String.class, mapLikeTypeFromBase.getKeyType());
        assertEquals(Integer.class, mapLikeTypeFromBase.getValueType());
    }

    @Test
    public void testMapLikeTypeCreationFromInvalidBaseType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String.class, Integer.class);
        try {
            MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testKeyTypeChange() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(Long.class);
        assertEquals(Long.class, newMapLikeType.getKeyType());
        assertEquals(Integer.class, newMapLikeType.getValueType());
    }

    @Test
    public void testValueTypeChange() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(Double.class);
        assertEquals(String.class, newMapLikeType.getKeyType());
        assertEquals(Double.class, newMapLikeType.getValueType());
    }

    @Test
    public void testTypeHandlerChange() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(new TestTypeHandler());
        assertTrue(newMapLikeType.hasHandlers());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(javaType1, String.class, Integer.class);
        MapLikeType mapLikeType2 = new MapLikeType(javaType2, String.class, Integer.class);
        MapLikeType mapLikeType3 = new MapLikeType(javaType1, String.class, String.class);

        assertTrue(mapLikeType1.equals(mapLikeType1));
        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        String result = mapLikeType.toString();
        String expected = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, result);
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(staticMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        JavaType refinedType = mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(refinedType.getClass().isAssignableFrom(HashMap.class));
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        MapLikeType mapLikeTypeWithHandler = mapLikeType.withKeyTypeHandler(new TestKeyTypeHandler());
        assertTrue(mapLikeTypeWithHandler.hasHandlers());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, String.class, Integer.class);
        MapLikeType mapLikeTypeWithHandler = mapLikeType.withKeyValueHandler(new TestKeyValueHandler());
        assertTrue(mapLikeTypeWithHandler.hasHandlers());
    }
}

class TestTypeHandler {}

class TestKeyTypeHandler {}

class TestKeyValueHandler {}